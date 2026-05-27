package it.unife.sample.backend.controller;

import it.unife.sample.backend.dto.request.AssistenzaRequestDTO;
import it.unife.sample.backend.dto.response.AssistenzaResponseDTO;
import it.unife.sample.backend.service.AssistenzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Controller per la gestione dei Ticket di Assistenza.
 * Implementa e gestisce la logica a stati dei Ticket
 * Mappato lato frontend in: assistenza.service.ts
 * 
 * API Esposte:
 * - POST /api/assistenza/apri -> L'utente apre un nuovo ticket (Stato: APERTO) [AssistenzaComponent]
 * - PUT /api/assistenza/{id}/prendi-in-carico/{cfAssistente} -> Lo staff prende in carico un ticket aperto (Stato: IN LAVORAZIONE) [Risolvi AssistenzeComponent]
 * - PUT /api/assistenza/{id}/risolvi -> Lo staff marca il ticket come completato (Stato: RISOLTO) [Risolvi AssistenzeComponent]
 * - PUT /api/assistenza/{id}/valuta/{voto} -> L'utente assegna il voto al ticket risolto (Stato: CHIUSO) [AssistenzaComponent]
 * - GET /api/assistenza -> Ottiene tutti i ticket [Risolvi AssistenzeComponent]
 * - GET /api/assistenza/{id} -> Ottiene un ticket specifico [Risolvi AssistenzeComponent]
 * - GET /api/assistenza/stato/{stato} -> Filtra per stato (es. APERTO, IN LAVORAZIONE) [Nessun component specifico]
 * - GET /api/assistenza/utente/{cf} -> Mostra i ticket aperti da un determinato utente [Nessun component specifico]
 * - GET /api/assistenza/assistente/{cf} -> Mostra i ticket presi in carico da un determinato assistente [Nessun component specifico]
 */
@RestController
@RequestMapping("/api/assistenza")
@CrossOrigin(origins = "http://localhost:4200")
public class AssistenzaController {

    @Autowired
    private AssistenzaService service;

    // --- LOGICHE DI SCRITTURA (FLUSSO OPERATIVO) ---

    // Funzionalità: L'utente apre un nuovo ticket (Stato: APERTO)
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
    public ResponseEntity<AssistenzaResponseDTO> risolviTicket(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String risposta = body.get("risposta");
        return ResponseEntity.ok(service.risolviTicket(id, risposta));
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

    // Funzionalità: Ottiene tutti i ticket
    @GetMapping
    public ResponseEntity<List<AssistenzaResponseDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    // Funzionalità: Ottiene un ticket specifico
    @GetMapping("/{id}")
    public ResponseEntity<AssistenzaResponseDTO> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Funzionalità: Filtra per stato (es. APERTO, IN LAVORAZIONE)
    @GetMapping("/stato/{stato}")
    public ResponseEntity<List<AssistenzaResponseDTO>> getByStato(@PathVariable String stato) {
        return ResponseEntity.ok(service.findByStato(stato));
    }

    // Funzionalità: Mostra i ticket aperti da un determinato utente
    @GetMapping("/utente/{cf}")
    public ResponseEntity<List<AssistenzaResponseDTO>> getByUtente(@PathVariable String cf) {
        return ResponseEntity.ok(service.findByUtente(cf));
    }

    // Funzionalità: Mostra i ticket presi in carico da un determinato assistente
    @GetMapping("/assistente/{cf}")
    public ResponseEntity<List<AssistenzaResponseDTO>> getByAssistente(@PathVariable String cf) {
        return ResponseEntity.ok(service.findByAssistente(cf));
    }
}
