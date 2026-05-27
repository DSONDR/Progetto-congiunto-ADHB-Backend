package it.unife.sample.backend.controller;

import it.unife.sample.backend.model.Abbonamento;
import it.unife.sample.backend.service.AbbonamentoService;
import it.unife.sample.backend.dto.response.TipoAbbonamentoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * Controller per la visualizzazione degli Abbonamenti e dei Tipi
 * Espone API per visualizzare il listino degli abbonamenti acquistabili e
 * lo storico degli abbonamenti acquistati
 * Ovviamente gli abbonamenti acquistati non si creano manualmente ma vengono
 * generati all'acquisto tramite SottoscrizioneController
 * Mappato lato frontend in: abbonamento.service.ts
 * 
 * API Esposte:
 * - GET /api/abbonamenti -> Crud base [Nessun component specifico]
 * - GET /api/abbonamenti/utente/{cf} -> Recupera lo storico di un utente specifico [Nessun component specifico]
 * - GET /api/abbonamenti/tipi -> Visualizza il listino dei tipi di abbonamento [Nessun component specifico]
 * - GET /api/abbonamenti/{id} -> Dettaglio singolo abbonamento venduto [Nessun component specifico]
 */
@RestController
@RequestMapping("/api/abbonamenti")
@CrossOrigin(origins = "http://localhost:4200")
public class AbbonamentoController {

    @Autowired
    private AbbonamentoService service;

    // Funzionalità: Crud base
    @GetMapping
    public List<Abbonamento> getAll() {
        return service.findAll();
    }

    // Funzionalità: Recupera lo storico di un utente specifico
    @GetMapping("/utente/{cf}")
    public ResponseEntity<List<Abbonamento>> getStoricoUtente(@PathVariable String cf) {
        return ResponseEntity.ok(service.findByAtletaCf(cf));
    }   

    // Funzionalità: Visualizza il listino dei tipi di abbonamento
    @GetMapping("/tipi")
    public List<TipoAbbonamentoDTO> getTipiAbbonamento() {
        return service.getTipiAbbonamento();
    }
	
    // Funzionalità: Dettaglio singolo abbonamento venduto
    @GetMapping("/{id}")
    public ResponseEntity<Abbonamento> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
