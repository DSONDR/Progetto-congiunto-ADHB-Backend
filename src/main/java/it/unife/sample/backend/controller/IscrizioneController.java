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
import it.unife.sample.backend.dto.response.UserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * Controller per la gestione delle Iscrizioni alle attività
 * Espone API dedicate per iscriversi a pagamento singolo o tramite abbonamento,
 * e per visualizzare lo storico/eliminare iscrizioni abbonamento
 * Mappato lato frontend in: iscrizione.service.ts
 * 
 * API Esposte:
 * - GET /api/iscrizioni -> Recupera l'elenco di tutte le iscrizioni singole [Nessun component specifico]
 * - GET /api/iscrizioni/{codiceAtt}/{idPagamento}/{cf} -> Recupera il dettaglio di un singolo elemento [Nessun component specifico]
 * - DELETE /api/iscrizioni/{codiceAtt}/{idPagamento}/{cf} -> Cancellazione iscrizione singola [CalendarioComponent]
 * - POST /api/iscrizioni/iscrivi -> Iscrizione singola (a pagamento) per una determinata attività. [CalendarioComponent / Corsi IstruttoreComponent / EventiComponent]
 * - POST /api/iscrizioni/usa-abbonamento -> Iscrizione scalandola dal proprio abbonamento (tempo o [EventiComponent]
 * - GET /api/iscrizioni/utente/{cf} -> Visualizza tutte le attività a cui si è iscritto l'atleta. [CalendarioComponent]
 * - GET /api/iscrizioni/usi-abbonamento/utente/{cf} -> Visualizza tutte le attività a cui si è iscritto l'atleta. [CalendarioComponent]
 * - GET /api/iscrizioni/attivita/{codiceAtt} -> Visualizza tutti gli utenti iscritti a un'attività (utile per l'istruttore) [Corsi IstruttoreComponent]
 * - GET /api/iscrizioni -> Recupera l'elenco di tutte le iscrizioni singole [Nessun component specifico]
 */
@RestController
@RequestMapping("/api/iscrizioni")
@CrossOrigin(origins = "http://localhost:4200")
public class IscrizioneController {

    @Autowired
    private IscrizioneService service;

    @Autowired
    private AtletaService atletaService;

    @Autowired
    private AttivitaService attivitaService;

    @Autowired
    private AbbonamentoService abbonamentoService;

    // Funzionalità: Recupera l'elenco di tutte le iscrizioni singole
    @GetMapping
    public List<Iscrizione> getAll() {
        return service.findAll();
    }

    // Funzionalità: Recupera il dettaglio di un singolo elemento
    @GetMapping("/{codiceAtt}/{idPagamento}/{cf}")
    public ResponseEntity<Iscrizione> getById(@PathVariable Long codiceAtt,
            @PathVariable Long idPagamento,
            @PathVariable String cf) {
        IscrSingolaId id = new IscrSingolaId(codiceAtt, idPagamento, cf);
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // (occhio nel service al vincolo di preavviso)
    // Funzionalità: Cancellazione iscrizione singola
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

    // Funzionalità: Iscrizione scalandola dal proprio abbonamento (tempo o
    // ingressi)
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

    // Funzionalità: Esegue l'operazione di getUsiAbbonamentoByUtente
    @GetMapping("/usi-abbonamento/utente/{cf}")
    public List<UsaAbb> getUsiAbbonamentoByUtente(@PathVariable String cf) {
        return service.getStoricoUsiAbbonamentoUtente(cf);
    }

    // Funzionalità: Visualizza tutti gli utenti iscritti a un'attività (utile per
    // l'istruttore)
    @GetMapping("/attivita/{codiceAtt}")
    public ResponseEntity<List<UserResponseDTO>> getIscrittiByAttivita(@PathVariable Long codiceAtt) {
        return ResponseEntity.ok(service.getIscrittiByAttivita(codiceAtt));
    }
}
