package com.insurance.ai;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/api/agente")
@CrossOrigin(origins = "*")
public class AgenteController {

    private final WebClient webClient;
    private final String claimUrl;
    private final String customerUrl;
    private final String ollamaUrl;
    private final String modelloOllama;
    private final AnalisiAiRepository analisiAiRepository;

    public AgenteController(
            WebClient.Builder builder,
            @Value("${servizi.claim-url}") String claimUrl,
            @Value("${servizi.customer-url}") String customerUrl,
            @Value("${ollama.url:http://host.docker.internal:11434}") String ollamaUrl,
            @Value("${ollama.model:llama3.2}") String modelloOllama,
            AnalisiAiRepository analisiAiRepository) {

        this.webClient = builder.build();
        this.claimUrl = claimUrl;
        this.customerUrl = customerUrl;
        this.ollamaUrl = ollamaUrl;
        this.modelloOllama = modelloOllama;
        this.analisiAiRepository = analisiAiRepository;
    }

    @PostMapping("/analizza/{sinistroId}")
    public ResponseEntity<AnalisiSinistroResponse> analizza(@PathVariable Long sinistroId) {
        SinistroDto sinistro = webClient.get()
                .uri(claimUrl + "/api/sinistri/" + sinistroId)
                .retrieve()
                .bodyToMono(SinistroDto.class)
                .block();

        if (sinistro == null) {
            return ResponseEntity.notFound().build();
        }

        ClienteDto cliente = webClient.get()
                .uri(customerUrl + "/api/clienti/" + sinistro.clienteId())
                .retrieve()
                .bodyToMono(ClienteDto.class)
                .block();

        AnalisiSinistroResponse base = analisiLocale(sinistro, cliente);
        String reportAi = chiamaOllama(sinistro, cliente, base);

        AnalisiSinistroResponse rispostaFinale;

        if (reportAi == null || reportAi.isBlank()) {
            rispostaFinale = base;
        } else {
            rispostaFinale = new AnalisiSinistroResponse(
                    base.sinistroId(),
                    base.cliente(),
                    base.riassunto(),
                    base.informazioniMancanti(),
                    base.documentiSuggeriti(),
                    base.priorita(),
                    reportAi,
                    "Ollama locale gratuito - modello " + modelloOllama
            );
        }

        salvaAnalisi(rispostaFinale);
        return ResponseEntity.ok(rispostaFinale);
    }

    @GetMapping("/storico")
    public List<AnalisiAi> storico() {
        return analisiAiRepository.findAll();
    }

    @GetMapping("/storico/{sinistroId}")
    public List<AnalisiAi> storicoPerSinistro(@PathVariable Long sinistroId) {
        return analisiAiRepository.findBySinistroIdOrderByCreataIlDesc(sinistroId);
    }

    @DeleteMapping("/storico/{id}")
    public ResponseEntity<Void> eliminaAnalisi(@PathVariable Long id) {
        if (!analisiAiRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        analisiAiRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/storico")
    public ResponseEntity<Void> eliminaTuttoLoStorico() {
        analisiAiRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }

    private String chiamaOllama(SinistroDto sinistro, ClienteDto cliente, AnalisiSinistroResponse base) {
        try {
            String nomeCliente = cliente == null
                    ? "Cliente non trovato"
                    : cliente.nome() + " " + cliente.cognome();

            String prompt = """
                    Sei un liquidatore assicurativo senior italiano.
                    Devi analizzare un sinistro assicurativo e aiutare un operatore umano.

                    Regole:
                    - Rispondi solo in italiano.
                    - Usa tono professionale.
                    - Non inventare dati non presenti.
                    - Se manca un'informazione, segnalala chiaramente.
                    - Indica eventuali rischi o anomalie.
                    - Dai una raccomandazione operativa finale.

                    DATI CLIENTE
                    Cliente: %s

                    DATI SINISTRO
                    Tipo sinistro: %s
                    Descrizione: %s
                    Documenti presenti: %s
                    Importo stimato: %s

                    ANALISI TECNICA GIÀ CALCOLATA
                    Informazioni mancanti: %s
                    Documenti suggeriti: %s
                    Priorità calcolata: %s

                    Produci un report con queste sezioni:

                    1. Sintesi del caso
                    2. Completezza delle informazioni
                    3. Documenti mancanti o consigliati
                    4. Valutazione della priorità
                    5. Possibili anomalie o rischio frode
                    6. Azione consigliata per l'operatore
                    """.formatted(
                    nomeCliente,
                    sinistro.tipo(),
                    sinistro.descrizione(),
                    sinistro.documentiPresenti(),
                    sinistro.importoStimato(),
                    String.join(", ", base.informazioniMancanti()),
                    String.join(", ", base.documentiSuggeriti()),
                    base.priorita()
            );

            Map<String, Object> richiesta = new HashMap<>();
            richiesta.put("model", modelloOllama);
            richiesta.put("prompt", prompt);
            richiesta.put("stream", false);

            Map<?, ?> risposta = webClient.post()
                    .uri(ollamaUrl + "/api/generate")
                    .bodyValue(richiesta)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (risposta == null) {
                return null;
            }

            Object testo = risposta.get("response");
            return testo == null ? null : testo.toString();

        } catch (Exception ex) {
            return null;
        }
    }

    private AnalisiSinistroResponse analisiLocale(SinistroDto sinistro, ClienteDto cliente) {
        List<String> mancanti = new ArrayList<>();
        List<String> documenti = new ArrayList<>();

        if (vuoto(sinistro.descrizione())) {
            mancanti.add("Descrizione dettagliata del sinistro");
        }

        if (sinistro.importoStimato() == null || sinistro.importoStimato().compareTo(BigDecimal.ZERO) <= 0) {
            mancanti.add("Importo stimato del danno");
        }

        if (cliente == null) {
            mancanti.add("Anagrafica cliente collegata al sinistro");
        }

        String docs = normalizza(sinistro.documentiPresenti());

        if (!docs.contains("foto")) {
            documenti.add("Foto del danno");
        }

        if (!docs.contains("denuncia")) {
            documenti.add("Denuncia o dichiarazione dell'evento");
        }

        if (!docs.contains("documento")) {
            documenti.add("Documento di identità del cliente");
        }

        if ("AUTO".equalsIgnoreCase(sinistro.tipo()) && !docs.contains("cai")) {
            documenti.add("Modulo CAI, se disponibile");
        }

        if ("CASA".equalsIgnoreCase(sinistro.tipo()) && !docs.contains("preventivo")) {
            documenti.add("Preventivo di riparazione");
        }

        String priorita = calcolaPriorita(sinistro, mancanti, documenti);
        String nomeCliente = cliente == null ? "Cliente non trovato" : cliente.nome() + " " + cliente.cognome();

        String riassunto = "Sinistro " + sinistro.tipo() + " per " + nomeCliente + ": " + sinistro.descrizione();

        String report = "Analisi locale completata. Priorità " + priorita + ". "
                + (mancanti.isEmpty()
                ? "Le informazioni principali sono presenti. "
                : "Mancano informazioni: " + String.join(", ", mancanti) + ". ")
                + (documenti.isEmpty()
                ? "La documentazione sembra sufficiente."
                : "Documenti suggeriti: " + String.join(", ", documenti) + ".");

        return new AnalisiSinistroResponse(
                sinistro.id(),
                nomeCliente,
                riassunto,
                mancanti,
                documenti,
                priorita,
                report,
                "analisi locale con fallback"
        );
    }

    private void salvaAnalisi(AnalisiSinistroResponse risposta) {
        AnalisiAi analisi = new AnalisiAi(
                risposta.sinistroId(),
                risposta.cliente(),
                risposta.priorita(),
                risposta.motoreUsato(),
                risposta.reportFinale()
        );

        analisiAiRepository.save(analisi);
    }

    private String calcolaPriorita(SinistroDto sinistro, List<String> mancanti, List<String> documenti) {
        BigDecimal importo = sinistro.importoStimato() == null ? BigDecimal.ZERO : sinistro.importoStimato();

        if (importo.compareTo(new BigDecimal("5000")) >= 0) {
            return "ALTA";
        }

        if (mancanti.size() + documenti.size() >= 4) {
            return "MEDIA";
        }

        return "BASSA";
    }

    private boolean vuoto(String valore) {
        return valore == null || valore.isBlank();
    }

    private String normalizza(String valore) {
        return valore == null ? "" : valore.toLowerCase(Locale.ITALIAN);
    }
}