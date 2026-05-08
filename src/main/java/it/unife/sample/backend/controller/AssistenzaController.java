package it.unife.sample.backend.controller;

import it.unife.sample.backend.model.Assistenza;
import it.unife.sample.backend.model.Utente;
import it.unife.sample.backend.service.AssistenzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/assistenza")
public class AssistenzaController {

    @Autowired
    private AssistenzaService service;

    @GetMapping
    public List<Assistenza> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Assistenza> getById(@PathVariable Long id) {
        Optional<Assistenza> assistenza = service.findById(id);
        return assistenza.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Assistenza create(@RequestBody Assistenza assistenza) {
        return service.save(assistenza);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Assistenza> update(@PathVariable Long id, @RequestBody Assistenza assistenza) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        assistenza.setIdTicket(id);
        return ResponseEntity.ok(service.save(assistenza));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Ricerca ticket per stato
    @GetMapping("/stato/{stato}")
    public List<Assistenza> findByStato(@PathVariable String stato) {
        return service.findByStato(stato);
    }

    // Ricerca ticket per tipo assistenza
    @GetMapping("/tipo/{tipoAss}")
    public List<Assistenza> findByTipoAss(@PathVariable String tipoAss) {
        return service.findByTipoAss(tipoAss);
    }

    // Ricerca ticket per soddisfazione
    @GetMapping("/soddisfazione/{soddisfazione}")
    public List<Assistenza> findBySoddisfazione(@PathVariable String soddisfazione) {
        return service.findBySoddisfazione(soddisfazione);
    }

    // RELAZIONE CON UTENTE

    // Associa un ticket a un utente
    @PostMapping("/{id}/utente/{cf}")
    public ResponseEntity<Void> addUtente(@PathVariable Long id,
                                          @PathVariable String cf) {

        service.associaUtente(id, cf);

        return ResponseEntity.ok().build();
    }

    // Ottengo l'utente associato al ticket
    @GetMapping("/{id}/utente")
    public Utente getUtente(@PathVariable Long id) {

        return service.getUtenteByTicket(id);
    }

}