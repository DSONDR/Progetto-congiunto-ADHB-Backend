package it.unife.sample.backend.controller;

import it.unife.sample.backend.model.Sponsorizzazione;
import it.unife.sample.backend.service.SponsorizzazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/sponsorizzazioni")
public class SponsorizzazioneController {

    @Autowired
    private SponsorizzazioneService service;

    @GetMapping
    public List<Sponsorizzazione> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sponsorizzazione> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Sponsorizzazione create(@RequestBody Sponsorizzazione sponsorizzazione) {
        return service.save(sponsorizzazione);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sponsorizzazione> update(@PathVariable Long id, @RequestBody Sponsorizzazione sponsorizzazione) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        sponsorizzazione.setIdSponsorizzazione(id);
        return ResponseEntity.ok(service.save(sponsorizzazione));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<Sponsorizzazione> getBySponsor(@RequestParam String pIva) {
        return service.findBySponsorPIva(pIva);
    }
}