package it.unife.sample.backend.controller;

import it.unife.sample.backend.model.Abbonamento;
import it.unife.sample.backend.service.AbbonamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/abbonamenti")
public class AbbonamentoController {

    @Autowired
    private AbbonamentoService service;

    @GetMapping
    public List<Abbonamento> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Abbonamento> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Abbonamento create(@RequestBody Abbonamento abbonamento) {
        return service.save(abbonamento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Abbonamento> update(@PathVariable Long id, @RequestBody Abbonamento abbonamento) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        abbonamento.setNumeroAbb(id);
        return ResponseEntity.ok(service.save(abbonamento));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}