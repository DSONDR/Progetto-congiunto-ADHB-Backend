package it.unife.sample.backend.controller;

import it.unife.sample.backend.model.DateAtt;
import it.unife.sample.backend.model.DateAttId;
import it.unife.sample.backend.service.DateAttService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * Controller per la gestione delle date delle attività.
 * Consente aggiungere, modificare e visualizzare le date di una attività.
 * 
 * API:
 * - POST   /api/date-att                          → aggiungi nuova data
 * - DELETE /api/date-att                          → elimina data (per chiave composta)
 * - GET    /api/date-att/attivita/{codiceAtt}    → ottieni tutte le date di un'attività
 */
@RestController
@RequestMapping("/api/date-att")
public class DateAttController {

    @Autowired
    private DateAttService service;

    @GetMapping
    public List<DateAtt> getAll() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<DateAtt> create(@RequestBody DateAtt dateAtt) {
        if (dateAtt.getAttivita() == null || dateAtt.getDate() == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.save(dateAtt));
    }

    @GetMapping("/attivita/{codiceAtt}")
    public List<DateAtt> getByAttivita(@PathVariable Long codiceAtt) {
        return service.findByAttivitaCodiceAtt(codiceAtt);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteByDateAttId(@RequestBody DateAttId id) {
        if (service.findById(id).isPresent()) {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
