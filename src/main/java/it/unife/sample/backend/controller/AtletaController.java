package it.unife.sample.backend.controller; // Cartella controller

// Devi importare sia il Model che il Repository
import it.unife.sample.backend.model.Atleta;
import it.unife.sample.backend.service.AtletaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * Controller per la gestione dell'Atleta (ACCESSO IN LETTURA)
 * Permette la visualizzazione dell'elenco degli atleti e del dettaglio singolo
 * NOTA: La creazione, cancellazione e aggiornamento dell'account avvengono
 * tramite i flussi di registrazione e gestione in AuthController
 * Mappato lato frontend in: atleta.service.ts
 * 
 * API Esposte:
 * - GET /api/atleti -> Recupera l'elenco di tutti gli atleti [AttivitaComponent]
 * - GET /api/atleti/{cf} -> Recupera il dettaglio di un singolo atleta [AttivitaComponent]
 * - POST /api/atleti/{cf}/spendi-punti -> Spende i punti gamification dell'atleta [AttivitaComponent]
 */
@RestController
@RequestMapping("/api/atleti")
@CrossOrigin(origins = "http://localhost:4200")
public class AtletaController {

    @Autowired
    private AtletaService service;

    // Funzionalità: Recupera l'elenco di tutti gli atleti
    @GetMapping
    public List<Atleta> getAll() {
        return service.findAll();
    }

    // Funzionalità: Recupera il dettaglio di un singolo atleta
    @GetMapping("/{cf}")
    public ResponseEntity<Atleta> getByCf(@PathVariable String cf) {
        return service.findById(cf)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Funzionalità: Spende i punti gamification dell'atleta
    @PostMapping("/{cf}/spendi-punti")
    public ResponseEntity<Atleta> spendiPunti(@PathVariable String cf, @RequestParam Integer punti) {
        try {
            Atleta aggiornato = service.spendiPunti(cf, punti);
            return ResponseEntity.ok(aggiornato);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
