package it.unife.sample.backend.controller;

import it.unife.sample.backend.dto.response.PagamentoResponseDTO;
import it.unife.sample.backend.service.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Controller per la visualizzazione dei Pagamenti (ACCESSO IN LETTURA)
 * NOTA: I pagamenti sono un registro immutabile e vengono creati
 * automaticamente in fase di iscrizione o sottoscrizione.
 * 
 * API Esposte:
 * - GET /api/pagamenti -> Elenco di tutti i pagamenti x admin
 * - GET /api/pagamenti/{id} -> Dettaglio pagamento
 * - GET /api/pagamenti/ricerca -> Filtro per data e/o importo
 * - GET /api/pagamenti/attivita/{id} -> Pagamenti associati a una specifica attività
 * - GET /api/pagamenti/utente/{cf} -> Storico transazioni di un utente
 * - GET /api/pagamenti/{id}/ricevuta -> Stampa ricevuta testuale
 */
@RestController
@RequestMapping("/api/pagamenti")
public class PagamentoController {

    @Autowired
    private PagamentoService service;

    // Funzionalità: Ottiene la lista completa dei pagamenti (admin)
    @GetMapping
    public ResponseEntity<List<PagamentoResponseDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    // Funzionalità: Dettaglio di un singolo pagamento tramite ID
    @GetMapping("/{id}")
    public ResponseEntity<PagamentoResponseDTO> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Funzionalità: Ricerca avanzata pagamenti filtrando per data o range di importo
    @GetMapping("/ricerca")
    public ResponseEntity<List<PagamentoResponseDTO>> ricerca(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime da,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime a,
            @RequestParam(required = false) Double min,
            @RequestParam(required = false) Double max) {
        return ResponseEntity.ok(service.ricercaAvanzata(da, a, min, max));
    }

    // Funzionalità: Storico dei pagamenti legati alle iscrizioni per una specifica attività
    @GetMapping("/attivita/{id}")
    public ResponseEntity<List<PagamentoResponseDTO>> getByAttivita(@PathVariable Long id) {
        return ResponseEntity.ok(service.getPagamentiPerAttivita(id));
    }

    // Funzionalità: Recupera lo storico transazioni completo di un determinato utente (Iscrizioni + Abbonamenti)
    @GetMapping("/utente/{cf}")
    public ResponseEntity<List<PagamentoResponseDTO>> getStoricoUtente(@PathVariable String cf) {
        return ResponseEntity.ok(service.getStoricoTransazioni(cf));
    }

    // Funzionalità: Genera e restituisce una ricevuta testuale formattata per un pagamento
    @GetMapping(value = "/{id}/ricevuta", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> getRicevuta(@PathVariable Long id) {
        try {
            String ricevuta = service.generaRicevuta(id);
            return ResponseEntity.ok(ricevuta);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
