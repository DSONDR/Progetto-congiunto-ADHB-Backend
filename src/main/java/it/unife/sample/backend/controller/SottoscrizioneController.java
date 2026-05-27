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
 * e per visualizzare lo storico/eliminare/rinnovare l'abbonamento acquistato.
 * Mappato lato frontend in: sottoscrizione.service.ts
 * 
 * API Esposte:
 * - GET /api/sottoscrizioni -> Recupera tutte le sottoscrizioni (per tutti) [EventiComponent]
 * - GET /api/sottoscrizioni/{numeroAbb}/{idPagamento}/{cf} -> Recupera una sottoscrizione specifica (cf, numeroAbb, idPagamento) [EventiComponent]
 * - DELETE /api/sottoscrizioni/{numeroAbb}/{idPagamento}/{cf} -> Elimina un elemento [Nessun component specifico]
 * - GET /api/sottoscrizioni/storicoUtente/{cf} -> Recupera lo storico di un utente specifico [Nessun component specifico]
 * - POST /api/sottoscrizioni/sottoscrivi -> Acquisto di un nuovo abbonamento scegliendolo dal listino [Nessun component specifico]
 * - POST /api/sottoscrizioni/rinnova/{numeroAbb} -> Rinnovo di un abbonamento esistente (SCADUTO o ATTIVO) [Nessun component specifico]
 * - POST /api/sottoscrizioni/disdici/{numeroAbb} -> Disdetta di un abbonamento [AbbonamentiComponent]
 * - GET /api/sottoscrizioni/abbonamenti/{cf} -> Visualizza tutti gli abbonamenti (attivi e storici) di un [Nessun component specifico]
 */
@RestController
@RequestMapping("/api/sottoscrizioni")
@CrossOrigin(origins = "http://localhost:4200")
public class SottoscrizioneController {

    @Autowired
    private SottoscrizioneService service;

    @Autowired
    private AtletaService atletaService;

    @Autowired
    private AbbonamentoService abbonamentoService;

    // Funzionalità: Recupera tutte le sottoscrizioni (per tutti)
    @GetMapping
    public List<Sottoscrizione> getAll() {
        return service.findAll();
    }

    // Funzionalità: Recupera una sottoscrizione specifica (cf, numeroAbb, idPagamento)
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
    // Funzionalità: Elimina un elemento
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

    // Funzionalità: Recupera lo storico di un utente specifico
    @GetMapping("/storicoUtente/{cf}")
    public List<Sottoscrizione> getStoricoUtente(@PathVariable String cf) {
        return service.getStoricoUtente(cf);
    }

    // Il certificato medico è verificato ma NON bloccante: l'acquisto va sempre
    // a buon fine. Se manca/è scaduto, la risposta include un campo "avviso".
    // Funzionalità: Acquisto di un nuovo abbonamento scegliendolo dal listino
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

    // Parametro metodo: metodo di pagamento usato per il rinnovo (es. CARTA_CREDITO)
    // Crea un nuovo Pagamento e riporta l'Abbonamento ad ATTIVO con date aggiornate
    // Restituisce 404 se l'abbonamento non esiste, 400 se è già CANCELLATO
    // Funzionalità: Rinnovo di un abbonamento esistente (SCADUTO o ATTIVO)
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

    // Imposta lo stato dell'abbonamento a CANCELLATO, impedendo rinnovi futuri
    // e l'aggiornamento automatico notturno
    // Funzionalità: Disdetta di un abbonamento
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

    // Utile per la dashboard atleta per vedere stato, scadenza e ingressi residui
    // Funzionalità: Visualizza tutti gli abbonamenti (attivi e storici) di un atleta
    @GetMapping("/abbonamenti/{cf}")
    public ResponseEntity<List<Abbonamento>> getAbbonамentiAtleta(@PathVariable String cf) {
        if (!atletaService.findById(cf).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(abbonamentoService.findByAtletaCf(cf));
    }
}
