package it.unife.sample.backend.controller;

import it.unife.sample.backend.dto.request.AttivitaRequestDTO;
import it.unife.sample.backend.dto.response.AttivitaResponseDTO;
import it.unife.sample.backend.service.AttivitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.time.LocalDateTime;

/**
 * Controller per la gestione delle attività (SOLO ISTRUTTORE).
 * Consente creazione, modifica, cancellazione e gestione delle sessioni
 * singole.
 *
 * API:
 * - POST /api/attivita-gestione -> crea nuova attività
 * - PUT /api/attivita-gestione/{id} -> modifica attività
 * - DELETE /api/attivita-gestione/{id} -> cancella attività
 * - POST /api/attivita-gestione/{id}/sessioni -> aggiunge una sessione
 * (data/ora) all'attività
 * - DELETE /api/attivita-gestione/{id}/sessioni -> rimuove una sessione
 * specifica dall'attività
 */
@RestController
@RequestMapping("/api/attivita-gestione")
public class AttivitaGestioneController {

    @Autowired
    private AttivitaService service;

    @PostMapping
    public ResponseEntity<AttivitaResponseDTO> create(@Valid @RequestBody AttivitaRequestDTO dto) {
        // NB: Il frontend dovrà inviare le credenziali istruttore nel JSON
        try {
            return ResponseEntity.ok(service.create(dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttivitaResponseDTO> update(@PathVariable Long id,
            @Valid @RequestBody AttivitaRequestDTO dto) {
        try {
            return ResponseEntity.ok(service.update(id, dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Funzionalità: Aggiunta di una singola sessione (data/orario) a un'attività
    // esistente
    // Parametro data: data e ora della sessione in formato ISO (es.
    // 2026-06-01T10:00:00)
    // Controlla automaticamente la disponibilità dell'impianto
    @PostMapping("/{id}/sessioni")
    public ResponseEntity<AttivitaResponseDTO> addSessione(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime data) {
        try {
            return ResponseEntity.ok(service.addSessione(id, data));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Funzionalità: Rimozione di una singola sessione (data/orario) da un'attività
    // esistente
    // Parametro data: data e ora della sessione da rimuovere (formato ISO)
    @DeleteMapping("/{id}/sessioni")
    public ResponseEntity<AttivitaResponseDTO> removeSessione(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime data) {
        try {
            return ResponseEntity.ok(service.removeSessione(id, data));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
