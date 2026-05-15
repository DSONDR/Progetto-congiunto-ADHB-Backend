package it.unife.sample.backend.controller;

import it.unife.sample.backend.model.Pagamento;
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
 * automaticamente in fase di iscrizione o sottoscrizione, per questo non
 * sono esposte API di creazione o modifica manuale.
 * 
 * API Esposte:
 * - GET /api/pagamenti -> Elenco di tutti i pagamenti x admin
 * - GET /api/pagamenti/{id} -> Dettaglio pagamento x admin
 * - GET /api/pagamenti/ricerca -> Filtro per data e/o importo x utente/admin
 * - GET /api/pagamenti/attivita/{id} -> Pagamenti associati a una specifica
 * attività
 */
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
