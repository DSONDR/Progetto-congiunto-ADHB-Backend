package it.unife.sample.backend.controller;

import it.unife.sample.backend.model.PartecipazioneSq;
import it.unife.sample.backend.model.PartecipazioneSqId;
import it.unife.sample.backend.service.PartecipazioneSqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * TODO completamente da fare
 * Probabilmente da eliminare, i controller e i service ponte non servono
 */

@RestController
@RequestMapping("/api/partecipazioni-sq")
public class PartecipazioneSqController {

    @Autowired
    private PartecipazioneSqService service;

    @GetMapping
    public List<PartecipazioneSq> getAll() {
        return service.findAll();
    }

    @GetMapping("/squadra/{squadraId}/attivita/{attivitaId}")
    public ResponseEntity<PartecipazioneSq> getById(@PathVariable Long squadraId, @PathVariable Long attivitaId) {
        PartecipazioneSqId id = new PartecipazioneSqId(squadraId, attivitaId);
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public PartecipazioneSq create(@RequestBody PartecipazioneSq partecipazione) {
        return service.save(partecipazione);
    }

    @PutMapping("/squadra/{squadraId}/attivita/{attivitaId}")
    public ResponseEntity<PartecipazioneSq> update(
            @PathVariable Long squadraId,
            @PathVariable Long attivitaId,
            @RequestBody PartecipazioneSq partecipazione) {
        PartecipazioneSqId id = new PartecipazioneSqId(squadraId, attivitaId);
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.save(partecipazione));
    }

    @DeleteMapping("/squadra/{squadraId}/attivita/{attivitaId}")
    public ResponseEntity<Void> delete(@PathVariable Long squadraId, @PathVariable Long attivitaId) {
        PartecipazioneSqId id = new PartecipazioneSqId(squadraId, attivitaId);
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
