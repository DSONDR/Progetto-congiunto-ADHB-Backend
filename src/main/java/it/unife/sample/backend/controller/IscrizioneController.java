package it.unife.sample.backend.controller;

import it.unife.sample.backend.dto.request.IscrizioneRequest;
import it.unife.sample.backend.dto.request.UsaAbbonamentoRequest;
import it.unife.sample.backend.model.Abbonamento;
import it.unife.sample.backend.model.Attivita;
import it.unife.sample.backend.model.IscrSingolaId;
import it.unife.sample.backend.model.Iscrizione;
import it.unife.sample.backend.model.Utente;
import it.unife.sample.backend.model.UsaAbb;
import it.unife.sample.backend.service.AbbonamentoService;
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

    @Autowired
    private AbbonamentoService abbonamentoService;

    @GetMapping
    public List<Iscrizione> getAll() {
        return service.findAll();
    }

    @GetMapping("/{codiceAtt}/{idPagamento}/{cf}")
    public ResponseEntity<Iscrizione> getById(@PathVariable Long codiceAtt,
                                              @PathVariable Long idPagamento,
                                              @PathVariable String cf) {
        IscrSingolaId id = new IscrSingolaId(codiceAtt, idPagamento, cf);
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Iscrizione create(@RequestBody Iscrizione iscrizione) {
        return service.save(iscrizione);
    }

    @PutMapping("/{codiceAtt}/{idPagamento}/{cf}")
    public ResponseEntity<Iscrizione> update(@PathVariable Long codiceAtt,
                                             @PathVariable Long idPagamento,
                                             @PathVariable String cf,
                                             @RequestBody Iscrizione iscrizione) {
        IscrSingolaId id = new IscrSingolaId(codiceAtt, idPagamento, cf);
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.save(iscrizione));
    }

    @DeleteMapping("/{codiceAtt}/{idPagamento}/{cf}")
    public ResponseEntity<Void> delete(@PathVariable Long codiceAtt,
                                       @PathVariable Long idPagamento,
                                       @PathVariable String cf) {
        IscrSingolaId id = new IscrSingolaId(codiceAtt, idPagamento, cf);
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.cancellaIscrizione(id);
        return ResponseEntity.noContent().build();
    }

    //Iscrizione --> TODO DTO Iscrizione
    @PostMapping("/iscrivi")
    public ResponseEntity<Iscrizione> iscrivi(@RequestBody IscrizioneRequest request) {
        Optional<Utente> utente = utenteService.findById(request.utenteCf());
        Optional<Attivita> attivita = attivitaService.findById(request.attivitaId());
        if (!utente.isPresent() || !attivita.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Iscrizione iscrizione = service.iscriviSingola(utente.get(), attivita.get(), request.importo(), request.metodo());
        return ResponseEntity.ok(iscrizione);
    }

    @PostMapping("/usa-abbonamento")
    public ResponseEntity<UsaAbb> usaAbbonamento(@RequestBody UsaAbbonamentoRequest request) {
        Optional<Utente> utente = utenteService.findById(request.utenteCf());
        Optional<Attivita> attivita = attivitaService.findById(request.attivitaId());
        Optional<Abbonamento> abbonamento = abbonamentoService.findById(request.abbonamentoId());
        if (!utente.isPresent() || !attivita.isPresent() || !abbonamento.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        UsaAbb uso = service.iscriviConAbbonamento(utente.get(), attivita.get(), abbonamento.get());
        return ResponseEntity.ok(uso);
    }
    
    // Visualizza tutte le attività a cui si è iscritto un utente
    @GetMapping("/utente/{cf}")
    public List<Iscrizione> getByUtente(@PathVariable String cf) {
        return service.getStoricoUtente(cf);
    }
}
