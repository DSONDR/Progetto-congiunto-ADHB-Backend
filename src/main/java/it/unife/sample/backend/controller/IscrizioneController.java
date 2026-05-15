package it.unife.sample.backend.controller;

import it.unife.sample.backend.dto.request.IscrizioneRequest;
import it.unife.sample.backend.dto.request.UsaAbbonamentoRequest;
import it.unife.sample.backend.model.Abbonamento;
import it.unife.sample.backend.model.Attivita;
import it.unife.sample.backend.model.IscrSingolaId;
import it.unife.sample.backend.model.Iscrizione;
import it.unife.sample.backend.model.Atleta;
import it.unife.sample.backend.model.UsaAbb;
import it.unife.sample.backend.service.AbbonamentoService;
import it.unife.sample.backend.service.AttivitaService;
import it.unife.sample.backend.service.IscrizioneService;
import it.unife.sample.backend.service.AtletaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * Controller per la gestione delle Iscrizioni alle attività
 * Espone API dedicate per iscriversi a pagamento singolo o tramite abbonamento,
 * e per visualizzare lo storico/eliminare iscrizioni (disiscrizione)
 * 
 * API Esposte:
 * - GET /api/iscrizioni -> Elenco tutte le iscrizioni
 * - GET /api/iscrizioni/{id} -> Dettaglio iscrizione
 * - DELETE /api/iscrizioni/{id} -> Cancella iscrizione
 * - GET /api/iscrizioni/utente/{cf} -> Storico iscrizioni utente
 * - POST /api/iscrizioni/iscrivi -> Crea iscrizione con pagamento singolo
 * - POST /api/iscrizioni/usa-abbonamento -> Crea iscrizione scalando
 * abbonamento
 */
@RestController
@RequestMapping("/api/iscrizioni")
public class IscrizioneController {

    @Autowired
    private IscrizioneService service;

    @Autowired
    private AtletaService atletaService;

    @Autowired
    private AttivitaService attivitaService;

    @Autowired
    private AbbonamentoService abbonamentoService;

    // Recupera l'elenco di tutte le iscrizioni singole
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

    // Funzionalità: Cancellazione iscrizione singola
    // (occhio nel service al vincolo di preavviso)
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

    // Funzionalità: Iscrizione singola (a pagamento) per una determinata attività.
    @PostMapping("/iscrivi")
    public ResponseEntity<Iscrizione> iscriviSingola(@RequestBody IscrizioneRequest request) {
        Optional<Atleta> atleta = atletaService.findById(request.getAtletaCf());
        Optional<Attivita> attivita = attivitaService.findById(request.getAttivitaId());
        if (!atleta.isPresent() || !attivita.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Iscrizione iscrizione = service.iscriviSingola(atleta.get(), attivita.get(), request.getImporto(),
                request.getMetodo());
        return ResponseEntity.ok(iscrizione);
    }

    // Funzionalità: Iscrizione scalandola (usando) dal proprio abbonamento (tempo o
    // ingressi).
    @PostMapping("/usa-abbonamento")
    public ResponseEntity<UsaAbb> usaAbbonamento(@RequestBody UsaAbbonamentoRequest request) {
        Optional<Atleta> atleta = atletaService.findById(request.getAtletaCf());
        Optional<Attivita> attivita = attivitaService.findById(request.getAttivitaId());
        Optional<Abbonamento> abbonamento = abbonamentoService.findById(request.getAbbonamentoId());
        if (!atleta.isPresent() || !attivita.isPresent() || !abbonamento.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        UsaAbb uso = service.iscriviConAbbonamento(atleta.get(), attivita.get(), abbonamento.get());
        return ResponseEntity.ok(uso);
    }

    // Funzionalità: Visualizza tutte le attività a cui si è iscritto l'atleta.
    @GetMapping("/utente/{cf}")
    public List<Iscrizione> getByUtente(@PathVariable String cf) {
        return service.getStoricoUtente(cf);
    }
}
