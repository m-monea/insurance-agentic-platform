package com.insurance.claim;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/sinistri")
@CrossOrigin(origins = "*")
@SuppressWarnings("null")
public class SinistroController {

    private final SinistroRepository repository;

    public SinistroController(SinistroRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Sinistro> trovaTutti(@RequestParam(required = false) Long clienteId) {

        if (clienteId == null) {
            return repository.findAll();
        }

        return repository.findByClienteId(clienteId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sinistro> trovaPerId(@PathVariable Long id) {

        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Sinistro> crea(@RequestBody Sinistro sinistro) {

        Sinistro salvato = repository.save(sinistro);

        return ResponseEntity
                .created(URI.create("/api/sinistri/" + salvato.getId()))
                .body(salvato);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sinistro> aggiorna(
            @PathVariable Long id,
            @RequestBody Sinistro sinistro) {

        return repository.findById(id)
                .map(esistente -> {

                    esistente.setClienteId(sinistro.getClienteId());
                    esistente.setTipo(sinistro.getTipo());
                    esistente.setDescrizione(sinistro.getDescrizione());
                    esistente.setDocumentiPresenti(sinistro.getDocumentiPresenti());
                    esistente.setImportoStimato(sinistro.getImportoStimato());

                    return ResponseEntity.ok(repository.save(esistente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> elimina(@PathVariable Long id) {

        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> eliminaTutti() {

        repository.deleteAll();

        return ResponseEntity.noContent().build();
    }
}