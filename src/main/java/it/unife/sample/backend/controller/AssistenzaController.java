package it.unife.sample.backend.controller;

import it.unife.sample.backend.dto.request.AssistenzaRequestDTO;
import it.unife.sample.backend.dto.response.AssistenzaResponseDTO;
import it.unife.sample.backend.service.AssistenzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

/**
 * Controller per la gestione dei Ticket di Assistenza.
 * Il controller aderisce alle direttive Security by Design, omettendo
 * i CRUD classici generici e instradando le operazioni tramite le
 * specifiche transizioni di stato.
 * 
 * API Esposte:
 * - POST /api/assistenza/apri -> L'utente apre il ticket
 * - PUT /api/assistenza/{id}/prendi-in-carico/{cf} -> L'admin prende in carico
 * - PUT /api/assistenza/{id}/risolvi -> L'admin risolve il ticket
 * - PUT /api/assistenza/{id}/valuta/{voto} -> L'utente assegna il feedback e lo chiude
 * 
 * Letture (GET):
 * - /api/assistenza -> Tutti (admin)
 * - /api/assistenza/utente/{cf} -> Filtra per utente
 * - /api/assistenza/assistente/{cf} -> Filtra per assistente
 * - /api/assistenza/stato/{stato} -> Filtra per stato
 */
@RestController
@RequestMapping("/api/assistenza")
public class AssistenzaController {

    @Autowired
    private AssistenzaService service;

    // --- LOGICHE DI SCRITTURA (FLUSSO OPERATIVO) ---

    // Funzionalità: L'utente apre un nuovo ticket (Stato: IN ATTESA)
    @PostMapping("/apri")
    public ResponseEntity<AssistenzaResponseDTO> apriTicket(@RequestBody @Valid AssistenzaRequestDTO request) {
        return ResponseEntity.ok(service.apriTicket(request));
    }

    // Funzionalità: Lo staff prende in carico un ticket aperto (Stato: IN LAVORAZIONE)
    @PutMapping("/{id}/prendi-in-carico/{cfAssistente}")
    public ResponseEntity<AssistenzaResponseDTO> prendiInCarico(@PathVariable Long id, @PathVariable String cfAssistente) {
        return ResponseEntity.ok(service.prendiInCarico(id, cfAssistente));
    }

    // Funzionalità: Lo staff marca il ticket come completato (Stato: RISOLTO)
    @PutMapping("/{id}/risolvi")
    public ResponseEntity<AssistenzaResponseDTO> risolviTicket(@PathVariable Long id) {
        return ResponseEntity.ok(service.risolviTicket(id));
    }

    // Funzionalità: L'utente assegna il voto al ticket risolto (Stato: CHIUSO)
    @PutMapping("/{id}/valuta/{voto}")
    public ResponseEntity<?> valutaTicket(@PathVariable Long id, @PathVariable Integer voto) {
        try {
            return ResponseEntity.ok(service.valutaTicket(id, voto));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- LOGICHE DI LETTURA ---

    // Ottiene tutti i ticket
    @GetMapping
    public ResponseEntity<List<AssistenzaResponseDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    // Ottiene un ticket specifico
    @GetMapping("/{id}")
    public ResponseEntity<AssistenzaResponseDTO> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Filtra per stato (es. IN ATTESA, IN LAVORAZIONE)
    @GetMapping("/stato/{stato}")
    public ResponseEntity<List<AssistenzaResponseDTO>> getByStato(@PathVariable String stato) {
        return ResponseEntity.ok(service.findByStato(stato));
    }

    // Mostra i ticket aperti da un determinato utente
    @GetMapping("/utente/{cf}")
    public ResponseEntity<List<AssistenzaResponseDTO>> getByUtente(@PathVariable String cf) {
        return ResponseEntity.ok(service.findByUtente(cf));
    }

    // Mostra i ticket presi in carico da un determinato assistente
    @GetMapping("/assistente/{cf}")
    public ResponseEntity<List<AssistenzaResponseDTO>> getByAssistente(@PathVariable String cf) {
        return ResponseEntity.ok(service.findByAssistente(cf));
    }
}
