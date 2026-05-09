package it.unife.sample.backend.controller;

import it.unife.sample.backend.model.Attivita;
import it.unife.sample.backend.service.AttivitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/attivita")
public class AttivitaController {

    @Autowired
    private AttivitaService service;

    @GetMapping
    public List<Attivita> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Attivita> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Attivita create(@RequestBody Attivita attivita) {
        return service.save(attivita);
    }

    @GetMapping("/calendario")
    public List<Attivita> getCalendario(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inizio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fine) {
        return service.getCalendario(inizio, fine);
    }

    @GetMapping("/filtra")
    public List<Attivita> filtra(
            @RequestParam(required = false) Long idImpianto,
            @RequestParam(required = false) Double prezzo,
            @RequestParam(required = false) String target,
            @RequestParam(required = false) String tipoEvento) {
        return service.filtra(idImpianto, prezzo, target, tipoEvento);
    }

    @GetMapping("/{id}/posti-disponibili")
    public ResponseEntity<Boolean> isPostoDisponibile(@PathVariable Long id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.isPostoDisponibile(id));
    }

    @GetMapping("/{id}/iscritti")
    public ResponseEntity<Integer> getNumeroIscritti(@PathVariable Long id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.getNumeroIscritti(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Attivita> update(@PathVariable Long id, @RequestBody Attivita attivita) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        attivita.setCodiceAtt(id);
        return ResponseEntity.ok(service.save(attivita));
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