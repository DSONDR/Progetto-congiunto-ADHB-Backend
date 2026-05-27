package it.unife.sample.backend.controller;

import it.unife.sample.backend.model.Sponsorizzazione;
import it.unife.sample.backend.service.SponsorizzazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * Controller per la gestione delle Sponsorizzazioni.
 * Espone API per il CRUD delle sponsorizzazioni (contratti con importo/durata) e la ricerca.
 * 
 * API Esposte:
 * - GET /api/sponsorizzazioni -> Recupera l'elenco di tutti gli elementi [Gestione SponsorComponent]
 * - GET /api/sponsorizzazioni/{id} -> Recupera il dettaglio di un singolo elemento [Gestione SponsorComponent]
 * - POST /api/sponsorizzazioni -> Crea un nuovo elemento [Gestione SponsorComponent]
 * - PUT /api/sponsorizzazioni/{id} -> Aggiorna un elemento esistente [Gestione SponsorComponent]
 * - DELETE /api/sponsorizzazioni/{id} -> Elimina un elemento [Cancella ProfiloComponent]
 * - GET /api/sponsorizzazioni/search -> Esegue l'operazione di getBySponsor [Nessun component specifico]
 * - GET /api/sponsorizzazioni/squadra/{idSquadra} -> Esegue l'operazione di getBySquadra [Nessun component specifico]
 * - GET /api/sponsorizzazioni/impianto/{idImpianto} -> Esegue l'operazione di getByImpianto [Nessun component specifico]
 * - GET    /api/sponsorizzazioni           -> Elenco sponsorizzazioni [Frontend: sponsorizzazione.service.ts]
 * - GET    /api/sponsorizzazioni/{id}      -> Dettaglio sponsorizzazione [Frontend: sponsorizzazione.service.ts]
 * - POST   /api/sponsorizzazioni           -> Crea sponsorizzazione [Frontend: sponsorizzazione.service.ts]
 * - PUT    /api/sponsorizzazioni/{id}      -> Modifica sponsorizzazione [Frontend: sponsorizzazione.service.ts]
 * - DELETE /api/sponsorizzazioni/{id}      -> Cancella sponsorizzazione [Frontend: sponsorizzazione.service.ts]
 * - GET    /api/sponsorizzazioni/search    -> Cerca per PIVA dello sponsor [Frontend: sponsorizzazione.service.ts]
 * - GET    /api/sponsorizzazioni/squadra/{idSquadra} -> Cerca per squadra [Frontend: sponsorizzazione.service.ts]
 * - GET    /api/sponsorizzazioni/impianto/{idImpianto} -> Cerca per impianto [Frontend: sponsorizzazione.service.ts]
 */
@RestController
@RequestMapping("/api/sponsorizzazioni")
@CrossOrigin(origins = "http://localhost:4200")
public class SponsorizzazioneController {

    @Autowired
    private SponsorizzazioneService service;

    // Funzionalità: Recupera l'elenco di tutti gli elementi
    @GetMapping
    public List<Sponsorizzazione> getAll() {
        return service.findAll();
    }

    // Funzionalità: Recupera il dettaglio di un singolo elemento
    @GetMapping("/{id}")
    public ResponseEntity<Sponsorizzazione> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Funzionalità: Crea un nuovo elemento
    @PostMapping
    public Sponsorizzazione create(@RequestBody Sponsorizzazione sponsorizzazione) {
        return service.save(sponsorizzazione);
    }

    // Funzionalità: Aggiorna un elemento esistente
    @PutMapping("/{id}")
    public ResponseEntity<Sponsorizzazione> update(@PathVariable Long id, @RequestBody Sponsorizzazione sponsorizzazione) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        sponsorizzazione.setIdSponsorizzazione(id);
        return ResponseEntity.ok(service.save(sponsorizzazione));
    }

    // Funzionalità: Elimina un elemento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Funzionalità: Cerca per PIVA dello sponsor
    @GetMapping("/search")
    public List<Sponsorizzazione> getBySponsor(@RequestParam String pIva) {
        return service.findBySponsorPartitaIva(pIva);
    }

    // Funzionalità: Cerca per squadra 
    @GetMapping("/squadra/{idSquadra}")
    public List<Sponsorizzazione> getBySquadra(@PathVariable Long idSquadra) {
        return service.findBySquadraId(idSquadra);
    }

    // Funzionalità: Cerca per impianto
    @GetMapping("/impianto/{idImpianto}")
    public List<Sponsorizzazione> getByImpianto(@PathVariable Long idImpianto) {
        return service.findByImpiantoId(idImpianto);
    }
}
