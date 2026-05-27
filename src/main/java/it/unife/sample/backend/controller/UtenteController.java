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
 * Permette visualizzazione, modifica e cancellazione
 * NOTA: La creazione di un Utente avviene ESCLUSIVAMENTE tramite AuthController
 * pertanto questo controller non espone chiamate POST di creazione
 * Mappato lato frontend in: utente.service.ts
 * 
 * API Esposte:
 * - GET /api/utenti -> CRUD base [Gestione UtentiComponent]
 * - GET /api/utenti/{id} -> Recupera il dettaglio di un singolo elemento [Gestione UtentiComponent]
 * - POST /api/utenti -> Crea un nuovo elemento [Gestione UtentiComponent]
 * - PUT /api/utenti/{id} -> Aggiorna un elemento esistente [Nessun component specifico]
 * - DELETE /api/utenti/{id} -> Elimina un elemento [Nessun component specifico]
 */
 
@RestController
@RequestMapping("/api/utenti")
@CrossOrigin(origins = "http://localhost:4200")
public class UtenteController {

    @Autowired
    private UtenteService service;

    // Funzionalità: CRUD base
    @GetMapping
    public List<Utente> getAll() {
        return service.findAll();
    }

    // Funzionalità: Recupera il dettaglio di un singolo elemento
    @GetMapping("/{id}")
    public ResponseEntity<Utente> getById(@PathVariable String id) {
        Optional<Utente> utente = service.findById(id);
        return utente.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Funzionalità: Crea un nuovo elemento
    @PostMapping
    public Utente create(@RequestBody Utente utente) {
        return service.save(utente);
    }

    // Funzionalità: Aggiorna un elemento esistente
    @PutMapping("/{id}")
    public ResponseEntity<Utente> update(@PathVariable String id, @RequestBody Utente utente) {

        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        utente.setCf(id);
        return ResponseEntity.ok(service.save(utente));
    }

    // Funzionalità: Elimina un elemento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
