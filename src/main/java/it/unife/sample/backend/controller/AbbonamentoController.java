package it.unife.sample.backend.controller;

import it.unife.sample.backend.model.Abbonamento;
import it.unife.sample.backend.service.AbbonamentoService;
import it.unife.sample.backend.dto.response.TipoAbbonamentoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * Controller per la visualizzazione degli Abbonamenti e dei Tipi (listino)
 * Espone API per visualizzare il listino degli abbonamenti acquistabili e
 * lo storico degli abbonamenti acquistati
 * NOTA: Gli abbonamenti acquistati non si creano manualmente ma vengono
 * generati tramite SottoscrizioneController
 * 
 * API Esposte:
 * - GET /api/abbonamenti -> Elenco di tutti gli abbonamenti venduti
 * - GET /api/abbonamenti/tipi -> Listino dei pacchetti abbonamento (prezzi)
 * - GET /api/abbonamenti/{id} -> Dettaglio singolo abbonamento venduto
 */
@RestController
@RequestMapping("/api/abbonamenti")
public class AbbonamentoController {

    @Autowired
    private AbbonamentoService service;

    @GetMapping
    public List<Abbonamento> getAll() {
        return service.findAll();
    }

    // Funzionalità: Visualizza il listino dei tipi di abbonamento disponibili per
    // l'acquisto
    @GetMapping("/tipi")
    public List<TipoAbbonamentoDTO> getTipiAbbonamento() {
        return service.getTipiAbbonamento();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Abbonamento> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}