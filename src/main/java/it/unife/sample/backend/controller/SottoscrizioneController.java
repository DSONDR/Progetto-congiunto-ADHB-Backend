package it.unife.sample.backend.controller;

import it.unife.sample.backend.dto.request.SottoscrizioneRequest;
import it.unife.sample.backend.dto.response.SottoscrizioneResponseDTO;
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
 * - GET /api/sottoscrizioni/ -> Elenco di tutti gli abbonamenti venduti
 * - GET /api/sottoscrizioni/{id} -> Dettaglio abbonamento acquistato
 * - DELETE /api/sottoscrizioni/{id} -> Cancella abbonamento acquistato
 * - GET /api/sottoscrizioni/storicoUtente/{cf} -> Storico sottoscrizioni utente
 * - POST /api/sottoscrizioni/sottoscrivi -> Acquisto nuovo abbonamento dal
 * listino
 * - POST /api/sottoscrizioni/rinnova/{id} -> Rinnovo abbonamento scaduto
 * - POST /api/sottoscrizioni/disdici/{id} -> Cancella abbonamento (impedisce
 * rinnovi)
 * - GET /api/sottoscrizioni/abbonamenti/{cf} -> Abbonamenti attivi/storici di
 * un atleta
 */
@RestController
@RequestMapping("/api/sottoscrizioni")
public class SottoscrizioneController {

    @Autowired
    private SottoscrizioneService service;

    @Autowired
    private AtletaService atletaService;

    @Autowired
    private AbbonamentoService abbonamentoService;

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
    // Il certificato medico è verificato ma NON bloccante: l'acquisto va sempre
    // a buon fine. Se manca/è scaduto, la risposta include un campo "avviso".
    @PostMapping("/sottoscrivi")
    public ResponseEntity<SottoscrizioneResponseDTO> sottoscrivi(@RequestBody SottoscrizioneRequest request) {
        Optional<Atleta> atleta = atletaService.findById(request.getAtletaCf());
        if (!atleta.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        try {
            SottoscrizioneResponseDTO response = service.sottoscrivi(
                    atleta.get(), request.getTipoAbbonamento(), request.getMetodo());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Funzionalità: Rinnovo di un abbonamento esistente (SCADUTO o ATTIVO)
    // Parametro metodo: metodo di pagamento usato per il rinnovo (es.
    // CARTA_CREDITO)
    // Crea un nuovo Pagamento e riporta l'Abbonamento ad ATTIVO con date aggiornate
    // Restituisce 404 se l'abbonamento non esiste, 400 se è già CANCELLATO
    @PostMapping("/rinnova/{numeroAbb}")
    public ResponseEntity<Abbonamento> rinnova(@PathVariable Long numeroAbb,
            @RequestParam String metodo) {
        try {
            Abbonamento rinnovato = service.rinnova(numeroAbb, metodo);
            return ResponseEntity.ok(rinnovato);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Funzionalità: Disdetta di un abbonamento
    // Imposta lo stato dell'abbonamento a CANCELLATO, impedendo rinnovi futuri
    // e l'aggiornamento automatico notturno
    @PostMapping("/disdici/{numeroAbb}")
    public ResponseEntity<Abbonamento> disdici(@PathVariable Long numeroAbb) {
        try {
            return ResponseEntity.ok(service.disdici(numeroAbb));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Funzionalità: Visualizza tutti gli abbonamenti (attivi e storici) di un
    // atleta
    // Utile per la dashboard atleta per vedere stato, scadenza e ingressi residui
    @GetMapping("/abbonamenti/{cf}")
    public ResponseEntity<List<Abbonamento>> getAbbonамentiAtleta(@PathVariable String cf) {
        if (!atletaService.findById(cf).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(abbonamentoService.findByAtletaCf(cf));
    }
}
