package it.unife.sample.backend.controller;

import it.unife.sample.backend.dto.response.AttivitaResponseDTO;
import it.unife.sample.backend.model.Istruttore;
import it.unife.sample.backend.service.AttivitaService;
import it.unife.sample.backend.service.IstruttoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * Controller per la gestione degli Istruttori
 * Espone API per il CRUD degli istruttori e l'interrogazione delle attività a
 * loro assegnate
 * Essendo personale interno, gli istruttori vengono creati direttamente tramite
 * questo controller
 * NOTA: l'istruttore deve anche creare le attività, ma questa funzionalità è
 * gestita dal Controller delle Attività (AttivitaController) dall'istruttore
 * Mappato lato frontend in: istruttore.service.ts
 * 
 * API Esposte:
 * - GET /api/istruttori -> Recupera l'elenco di tutti gli elementi [Corsi IstruttoreComponent / Gestione AttivitaComponent / Gestione UtentiComponent]
 * - GET /api/istruttori/{cf} -> Recupera il dettaglio di un singolo elemento [Corsi IstruttoreComponent / Gestione AttivitaComponent / Gestione UtentiComponent]
 * - POST /api/istruttori -> Crea un nuovo elemento [Corsi IstruttoreComponent / Gestione AttivitaComponent / Gestione UtentiComponent]
 * - PUT /api/istruttori/{cf} -> Aggiorna un elemento esistente [Gestione UtentiComponent]
 * - DELETE /api/istruttori/{cf} -> Elimina un elemento [Nessun component specifico]
 * - GET /api/istruttori/{cf}/attivita -> Visualizza l'elenco delle attività di cui l'istruttore [Nessun component specifico]
 */
@RestController
@RequestMapping("/api/istruttori")
@CrossOrigin(origins = "http://localhost:4200")
public class IstruttoreController {

    @Autowired
    private IstruttoreService service;

    @Autowired
    private AttivitaService attivitaService;

    // Funzionalità: Recupera l'elenco di tutti gli elementi
    @GetMapping
    public List<Istruttore> getAll() {
        return service.findAll();
    }

    // Funzionalità: Recupera il dettaglio di un singolo elemento
    @GetMapping("/{cf}")
    public ResponseEntity<Istruttore> getByCf(@PathVariable String cf) {
        return service.findById(cf)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Funzionalità: Crea un nuovo elemento
    @PostMapping
    public Istruttore create(@RequestBody Istruttore istruttore) {
        return service.save(istruttore);
    }

    // Funzionalità: Aggiorna un elemento esistente
    @PutMapping("/{cf}")
    public ResponseEntity<Istruttore> update(@PathVariable String cf, @RequestBody Istruttore istruttore) {
        if (!service.findById(cf).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        istruttore.setCf(cf);
        return ResponseEntity.ok(service.save(istruttore));
    }

    // Funzionalità: Elimina un elemento
    @DeleteMapping("/{cf}")
    public ResponseEntity<Void> delete(@PathVariable String cf) {
        if (!service.findById(cf).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(cf);
        return ResponseEntity.noContent().build();
    }

    // Funzionalità: Visualizza l'elenco delle attività di cui l'istruttore è responsabile
    @GetMapping("/{cf}/attivita")
    public ResponseEntity<List<AttivitaResponseDTO>> getAttivita(@PathVariable String cf) {
        if (!service.findById(cf).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(attivitaService.findByIstruttoreCf(cf));
    }
}
