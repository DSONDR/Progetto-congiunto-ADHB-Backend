package it.unife.sample.backend.controller;

import it.unife.sample.backend.dto.request.AttivitaRequestDTO;
import it.unife.sample.backend.dto.response.AttivitaResponseDTO;
import it.unife.sample.backend.service.AttivitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

/**
 * Controller per la gestione delle attività (SOLO ISTRUTTORE).
 * Consente creazione, modifica e cancellazione di attività.
 * 
 * API:
 * - POST   /api/attivita-gestione         → crea nuova attività
 * - PUT    /api/attivita-gestione/{id}    → modifica attività
 * - DELETE /api/attivita-gestione/{id}    → cancella attività
 */
@RestController
@RequestMapping("/api/attivita-gestione")
public class AttivitaGestioneController {

    @Autowired
    private AttivitaService service;

    @PostMapping
    public ResponseEntity<AttivitaResponseDTO> create(@Valid @RequestBody AttivitaRequestDTO dto) {
        // NB: Il frontend dovrà inviare le credenziali istruttore nel JSON
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttivitaResponseDTO> update(@PathVariable Long id, @Valid @RequestBody AttivitaRequestDTO dto) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
