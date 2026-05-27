package it.unife.sample.backend.controller;

import it.unife.sample.backend.dto.response.AttivitaResponseDTO;
import it.unife.sample.backend.service.AttivitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller per la visualizzazione delle attività (ACCESSO PUBBLICO).
 * Consente ricerca, filtri e visualizzazione calendario.
 * Mappato lato frontend in: attivitavisualizzazione.service.ts
 * 
 * API Esposte:
 * - GET /api/attivita -> Recupera l'elenco di tutti gli elementi [Nessun component specifico]
 * - GET /api/attivita/{id} -> Recupera il dettaglio di un singolo elemento [Nessun component specifico]
 * - GET /api/attivita/calendario -> Filtra le attività per calendario (tra due date) [Nessun component specifico]
 * - GET /api/attivita/filtra -> Filtra le attività con parametri avanzati [Nessun component specifico]
 * - GET /api/attivita/tipi-evento -> Recupera l'elenco dei tipi di evento [Nessun component specifico]
 * - GET /api/attivita/destinatari -> Recupera l'elenco dei destinatari possibili [Nessun component specifico]
 * - GET /api/attivita/{id}/posti-disponibili -> Verifica se c'è almeno un posto disponibile [Nessun component specifico]
 * - GET /api/attivita/{id}/iscritti -> Recupera il numero totale degli iscritti all'attività [Nessun component specifico]
 */
@RestController
@RequestMapping("/api/attivita")
@CrossOrigin(origins = "http://localhost:4200")
public class AttivitaVisualizzazioneController {

    @Autowired
    private AttivitaService service;

    // Funzionalità: Recupera l'elenco di tutti gli elementi
    @GetMapping
    public List<AttivitaResponseDTO> getAll() {
        return service.findAllDTO();
    }

    // Funzionalità: Recupera il dettaglio di un singolo elemento
    @GetMapping("/{id}")
    public ResponseEntity<AttivitaResponseDTO> getById(@PathVariable Long id) {
        return service.findDtoById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Funzionalità: Filtra le attività per calendario (tra due date)
    @GetMapping("/calendario")
    public List<AttivitaResponseDTO> getCalendario(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inizio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fine) {
        return service.getCalendario(inizio, fine);
    }

    // Funzionalità: Filtra le attività con parametri avanzati
    @GetMapping("/filtra")
    public List<AttivitaResponseDTO> filtra(
            @RequestParam(required = false) Long idImpianto,
            @RequestParam(required = false) Double prezzo,
            @RequestParam(required = false) String target,
            @RequestParam(required = false) String tipoEvento,
            @RequestParam(required = false) String istruttoreCf,
            @RequestParam(required = false) Long squadraId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inizio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fine) {
        return service.filtra(idImpianto, prezzo, target, tipoEvento, istruttoreCf, squadraId, inizio, fine);
    }

    // Funzionalità: Recupera l'elenco dei tipi di evento
    @GetMapping("/tipi-evento")
    public List<String> getTipiEvento() {
        return service.getTipiEvento();
    }

    // Funzionalità: Recupera l'elenco dei destinatari possibili
    @GetMapping("/destinatari")
    public List<String> getDestinatari() {
        return service.getDestinatari();
    }

    // Funzionalità: Verifica se c'è almeno un posto disponibile
    @GetMapping("/{id}/posti-disponibili")
    public ResponseEntity<Boolean> isPostoDisponibile(@PathVariable Long id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.isPostoDisponibile(id));
    }

    // Funzionalità: Recupera il numero totale degli iscritti all'attività
    @GetMapping("/{id}/iscritti")
    public ResponseEntity<Integer> getNumeroIscritti(@PathVariable Long id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.getNumeroIscritti(id));
    }
}
