package com.insurance.customer;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clienti")
@CrossOrigin(origins = "*")
@SuppressWarnings("null")
public class ClienteController {

    private final ClienteRepository repository;

    public ClienteController(ClienteRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Cliente> trovaTutti(@RequestParam(required = false) String cognome) {
        if (cognome == null || cognome.isBlank()) {
            return repository.findAll();
        }

        return repository.findByCognomeContainingIgnoreCase(cognome);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> trovaPerId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Cliente> crea(@Valid @RequestBody Cliente cliente) {

        cliente.setId(null);

        Cliente salvato = repository.save(cliente);

        return ResponseEntity
                .created(URI.create("/api/clienti/" + salvato.getId()))
                .body(salvato);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> aggiorna(
            @PathVariable Long id,
            @Valid @RequestBody Cliente cliente) {

        return repository.findById(id)
                .map(esistente -> {

                    esistente.setNome(cliente.getNome());
                    esistente.setCognome(cliente.getCognome());
                    esistente.setEmail(cliente.getEmail());
                    esistente.setTelefono(cliente.getTelefono());
                    esistente.setNumeroPolizza(cliente.getNumeroPolizza());

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