package it.unife.sample.backend.controller;

import it.unife.sample.backend.dto.response.AttivitaResponseDTO;
import it.unife.sample.backend.model.Istruttore;
import it.unife.sample.backend.service.AttivitaService;
import it.unife.sample.backend.service.IstruttoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/istruttori")
public class IstruttoreController {

    @Autowired
    private IstruttoreService service;

    @Autowired
    private AttivitaService attivitaService;

    @GetMapping
    public List<Istruttore> getAll() {
        return service.findAll();
    }

    @GetMapping("/{cf}")
    public ResponseEntity<Istruttore> getByCf(@PathVariable String cf) {
        return service.findById(cf)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Istruttore create(@RequestBody Istruttore istruttore) {
        return service.save(istruttore);
    }

    @PutMapping("/{cf}")
    public ResponseEntity<Istruttore> update(@PathVariable String cf, @RequestBody Istruttore istruttore) {
        if (!service.findById(cf).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        istruttore.setCf(cf);
        return ResponseEntity.ok(service.save(istruttore));
    }

    @DeleteMapping("/{cf}")
    public ResponseEntity<Void> delete(@PathVariable String cf) {
        if (!service.findById(cf).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(cf);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{cf}/attivita")
    public ResponseEntity<List<AttivitaResponseDTO>> getAttivita(@PathVariable String cf) {
        if (!service.findById(cf).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(attivitaService.findByIstruttoreCf(cf));
    }
}
