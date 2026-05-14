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
 * 
 * API:
 * - GET    /api/attivita                  → lista tutte le attività
 * - GET    /api/attivita/{id}             → dettagli attività
 * - GET    /api/attivita/calendario       → attività tra due date
 * - GET    /api/attivita/filtra           → filtro per data/tipo/destinatario/prezzo
 * - GET    /api/attivita/{id}/posti-disponibili → verifica disponibilità
 * - GET    /api/attivita/{id}/iscritti    → numero iscritti
 */
@RestController
@RequestMapping("/api/attivita")
public class AttivitaVisualizzazioneController {

    @Autowired
    private AttivitaService service;

    @GetMapping
    public List<AttivitaResponseDTO> getAll() {
        return service.findAllDTO();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttivitaResponseDTO> getById(@PathVariable Long id) {
        return service.findDtoById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/calendario")
    public List<AttivitaResponseDTO> getCalendario(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inizio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fine) {
        return service.getCalendario(inizio, fine);
    }

    @GetMapping("/filtra")
    public List<AttivitaResponseDTO> filtra(
            @RequestParam(required = false) Long idImpianto,
            @RequestParam(required = false) Double prezzo,
            @RequestParam(required = false) String target,
            @RequestParam(required = false) String tipoEvento,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inizio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fine) {
        return service.filtra(idImpianto, prezzo, target, tipoEvento, inizio, fine);
    }

    @GetMapping("/{id}/posti-disponibili")
    public ResponseEntity<Boolean> isPostoDisponibile(@PathVariable Long id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.isPostoDisponibile(id));
    }

    @GetMapping("/{id}/iscritti")
    public ResponseEntity<Integer> getNumeroIscritti(@PathVariable Long id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.getNumeroIscritti(id));
    }
}
