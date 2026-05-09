package it.unife.sample.backend.controller;

import it.unife.sample.backend.model.Abbonamento;
import it.unife.sample.backend.model.Sottoscrizione;
import it.unife.sample.backend.model.Utente;
import it.unife.sample.backend.service.AbbonamentoService;
import it.unife.sample.backend.service.SottoscrizioneService;
import it.unife.sample.backend.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/sottoscrizioni")
public class SottoscrizioneController {

    @Autowired
    private SottoscrizioneService service;

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private AbbonamentoService abbonamentoService;

    @GetMapping
    public List<Sottoscrizione> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sottoscrizione> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Sottoscrizione create(@RequestBody Sottoscrizione sottoscrizione) {
        return service.save(sottoscrizione);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sottoscrizione> update(@PathVariable Long id, @RequestBody Sottoscrizione sottoscrizione) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        sottoscrizione.setId(id);
        return ResponseEntity.ok(service.save(sottoscrizione));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/utente/{cf}")
    public List<Sottoscrizione> getByUtente(@PathVariable String cf) {
        return service.getStoricoUtente(cf);
    }

    @GetMapping("/{id}/valida")
    public ResponseEntity<Boolean> isValid(@PathVariable Long id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.isValida(id));
    }
    
    @PostMapping("/sottoscrivi")
    public ResponseEntity<Sottoscrizione> sottoscrivi(@RequestBody SottoscrizioneRequest request) {
		Optional<Utente> utente = utenteService.findById(request.utenteCf());
		Optional<Abbonamento> abbonamento = abbonamentoService.findById(request.abbonamentoId());
		if (!utente.isPresent() || !abbonamento.isPresent()) {
		     return ResponseEntity.notFound().build();
		}
		Sottoscrizione s = service.sottoscrivi(utente.get(),abbonamento.get(),request.metodo());
		return ResponseEntity.ok(s);
	}
}
