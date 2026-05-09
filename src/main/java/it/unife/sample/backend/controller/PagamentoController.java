package it.unife.sample.backend.controller;

import it.unife.sample.backend.model.Pagamento;
import it.unife.sample.backend.service.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/pagamenti")
public class PagamentoController {

    @Autowired
    private PagamentoService service;

    @GetMapping
    public List<Pagamento> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pagamento> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pagamento> create(@RequestBody Pagamento pagamento) {
        return ResponseEntity.ok(service.save(pagamento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pagamento> update(@PathVariable Long id, @RequestBody Pagamento pagamento) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        pagamento.setIdPagamento(id);
        return ResponseEntity.ok(service.save(pagamento));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ricerca")
    public List<Pagamento> ricerca(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime da,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime a,
            @RequestParam(required = false) Double min,
            @RequestParam(required = false) Double max) {
        return service.ricercaAvanzata(da, a, min, max);
    }

    @GetMapping("/attivita/{id}")
    public List<Pagamento> getByAttivita(@PathVariable Long id) {
        return service.getPagamentiPerAttivita(id);
    }
}
