package it.unife.sample.backend.controller;

import it.unife.sample.backend.dto.request.SottoscrizioneRequest;
import it.unife.sample.backend.model.Abbonamento;
import it.unife.sample.backend.model.Sottoscrizione;
import it.unife.sample.backend.model.SottoscrizioneId;
import it.unife.sample.backend.model.Atleta;
import it.unife.sample.backend.service.AbbonamentoService;
import it.unife.sample.backend.service.SottoscrizioneService;
import it.unife.sample.backend.service.AtletaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * Controller per la gestione delle Sottoscrizioni (acquisto abbonamenti)
 * Espone API dedicate per acquistare un tipo di abbonamento dal listino,
 * e per visualizzare lo storico/eliminare l'abbonamento acquistato.
 * 
 * API Esposte:
 * - GET /api/sottoscrizioni/ -> Elenco di tutti gli abbonamenti
 * venduti
 * - GET /api/sottoscrizioni/{id} -> Dettaglio abbonamento acquistato
 * - DELETE /api/sottoscrizioni/{id} -> Cancella abbonamento acquistato
 * - GET /api/sottoscrizioni/storicoUtente/{cf} -> Storico abbonamenti utente
 * - POST /api/sottoscrizioni/sottoscrivi -> Acquisto nuovo abbonamento dal
 * listino
 */
@RestController
@RequestMapping("/api/sottoscrizioni")
public class SottoscrizioneController {

    @Autowired
    private SottoscrizioneService service;

    @Autowired
    private AtletaService atletaService;

    // Recupera tutte le sottoscrizioni (per tutti)
    @GetMapping
    public List<Sottoscrizione> getAll() {
        return service.findAll();
    }

    // Recupera una sottoscrizione specifica (cf, numeroAbb, idPagamento)
    @GetMapping("/{numeroAbb}/{idPagamento}/{cf}")
    public ResponseEntity<Sottoscrizione> getById(@PathVariable Long numeroAbb,
            @PathVariable Long idPagamento,
            @PathVariable String cf) {
        SottoscrizioneId id = new SottoscrizioneId(numeroAbb, idPagamento, cf);
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Cancella una sottoscrizione specifica (cf, numeroAbb, idPagamento)
    // TODO: Chi puo cancellare le sottoscrizioni, cosa succede dopo?
    @DeleteMapping("/{numeroAbb}/{idPagamento}/{cf}")
    public ResponseEntity<Void> delete(@PathVariable Long numeroAbb,
            @PathVariable Long idPagamento,
            @PathVariable String cf) {
        SottoscrizioneId id = new SottoscrizioneId(numeroAbb, idPagamento, cf);
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Visualizza lo storico degli abbonamenti sottoscritti da un atleta
    @GetMapping("/storicoUtente/{cf}")
    public List<Sottoscrizione> getStoricoUtente(@PathVariable String cf) {
        return service.getStoricoUtente(cf);
    }

    // Funzionalità: Acquisto di un nuovo abbonamento scegliendolo dal listino
    // (mensile, ingressi, ecc).
    @PostMapping("/sottoscrivi")
    public ResponseEntity<Sottoscrizione> sottoscrivi(@RequestBody SottoscrizioneRequest request) {
        Optional<Atleta> atleta = atletaService.findById(request.getAtletaCf());
        if (!atleta.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        try {
            Sottoscrizione s = service.sottoscrivi(atleta.get(), request.getTipoAbbonamento(), request.getMetodo());
            return ResponseEntity.ok(s);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
