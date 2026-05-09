package it.unife.sample.backend.controller;

import it.unife.sample.backend.model.Attivita;
import it.unife.sample.backend.model.Sottoscrizione;
import it.unife.sample.backend.model.UsaAbb;
import it.unife.sample.backend.service.AttivitaService;
import it.unife.sample.backend.service.SottoscrizioneService;
import it.unife.sample.backend.service.UsaAbbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/usa-abb")
public class UsaAbbController {

    @Autowired
    private UsaAbbService service;

    @Autowired
    private SottoscrizioneService sottoscrizioneService;

    @Autowired
    private AttivitaService attivitaService;

    @GetMapping
    public List<UsaAbb> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsaAbb> getById(@PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public UsaAbb create(@RequestBody UsaAbb usaAbb) {
        return service.save(usaAbb);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsaAbb> update(@PathVariable Long id, @RequestBody UsaAbb usaAbb) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        usaAbb.setId(id);
        return ResponseEntity.ok(service.save(usaAbb));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    //@PostMapping("/utilizza")
    
}
