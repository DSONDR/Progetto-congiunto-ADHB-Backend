package it.unife.sample.backend.controller; // Cartella controller

// Devi importare sia il Model che il Repository
import it.unife.sample.backend.model.Utente;
import it.unife.sample.backend.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * Controller per le operazioni base sugli Utenti (gestione profilo)
 * Permette visualizzazione, modifica e cancellazione (admin)
 * NOTA: La creazione di un Utente avviene ESCLUSIVAMENTE tramite AuthController
 * (register),
 * pertanto questo controller non espone chiamate POST di creazione
 * 
 * API Esposte:
 * - GET /api/utenti -> Elenco di tutti gli utenti
 * - GET /api/utenti/{id} -> Dettaglio singolo utente
 * - PUT /api/utenti/{id} -> Modifica profilo utente
 * - DELETE /api/utenti/{id} -> Cancellazione account
 */
@RestController
@RequestMapping("/api/utenti")
public class UtenteController {

    @Autowired
    private UtenteService service;

    // CRUD base
    @GetMapping
    public List<Utente> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Utente> getById(@PathVariable String id) {
        Optional<Utente> utente = service.findById(id);
        return utente.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Utente create(@RequestBody Utente utente) {
        return service.save(utente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Utente> update(@PathVariable String id, @RequestBody Utente utente) {

        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        utente.setCf(id);
        return ResponseEntity.ok(service.save(utente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
