package it.unife.sample.backend.controller;

import it.unife.sample.backend.model.Attivita;
import it.unife.sample.backend.model.Iscrizione;
import it.unife.sample.backend.model.Utente;
import it.unife.sample.backend.service.AttivitaService;
import it.unife.sample.backend.service.IscrizioneService;
import it.unife.sample.backend.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/iscrizioni")
public class IscrizioneController {

    @Autowired
    private IscrizioneService service;

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private AttivitaService attivitaService;

    @GetMapping
    public List<Iscrizione> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Iscrizione> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Iscrizione create(@RequestBody Iscrizione iscrizione) {
        return service.save(iscrizione);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Iscrizione> update(@PathVariable Long id, @RequestBody Iscrizione iscrizione) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        iscrizione.setId(id);
        return ResponseEntity.ok(service.save(iscrizione));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.cancellaIscrizione(id);
        return ResponseEntity.noContent().build();
    }

    //Iscrizione
    @PostMapping("/iscrivi")
    public ResponseEntity<Iscrizione> iscrivi(@RequestBody IscrizioneRequest request) {
		Optional<Utente> utente = utenteService.findById(request.utenteCf());
		Optional<Attivita> attivita = attivitaService.findById(request.attivitaId());
		if (!utente.isPresent() || !attivita.isPresent()) {
		    return ResponseEntity.notFound().build();
		}
		Iscrizione iscrizione = service.iscriviUtente(utente.get(), attivita.get(), request.importo(), request.metodo());
		return ResponseEntity.ok(iscrizione);
    }
    
    // Visualizza tutte le attività a cui si è iscritto un utente
    @GetMapping("/utente/{cf}")
    public List<Iscrizione> getByUtente(@PathVariable String cf) {
        return service.getStoricoUtente(cf);
    }
}
