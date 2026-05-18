package it.unife.sample.backend.controller;

import it.unife.sample.backend.model.Sponsorizzazione;
import it.unife.sample.backend.service.SponsorizzazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**TODO completamente da fare
 * Controller per la gestione delle Sponsorizzazioni.
 * Espone API per il CRUD delle sponsorizzazioni (contratti con importo/durata) e la ricerca.
 * 
 * API Esposte:
 * - GET    /api/sponsorizzazioni           -> Elenco sponsorizzazioni
 * - GET    /api/sponsorizzazioni/{id}      -> Dettaglio sponsorizzazione
 * - POST   /api/sponsorizzazioni           -> Crea sponsorizzazione
 * - PUT    /api/sponsorizzazioni/{id}      -> Modifica sponsorizzazione
 * - DELETE /api/sponsorizzazioni/{id}      -> Cancella sponsorizzazione
 * - GET    /api/sponsorizzazioni/search    -> Cerca per PIVA dello sponsor
 * - GET    /api/sponsorizzazioni/squadra/{idSquadra} -> Cerca per squadra
 * - GET    /api/sponsorizzazioni/impianto/{idImpianto} -> Cerca per impianto
 */
@RestController
@RequestMapping("/api/sponsorizzazioni")
public class SponsorizzazioneController {

    @Autowired
    private SponsorizzazioneService service;

    @GetMapping
    public List<Sponsorizzazione> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sponsorizzazione> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Sponsorizzazione create(@RequestBody Sponsorizzazione sponsorizzazione) {
        return service.save(sponsorizzazione);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sponsorizzazione> update(@PathVariable Long id, @RequestBody Sponsorizzazione sponsorizzazione) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        sponsorizzazione.setIdSponsorizzazione(id);
        return ResponseEntity.ok(service.save(sponsorizzazione));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<Sponsorizzazione> getBySponsor(@RequestParam String pIva) {
        return service.findBySponsorPIva(pIva);
    }

    @GetMapping("/squadra/{idSquadra}")
    public List<Sponsorizzazione> getBySquadra(@PathVariable Long idSquadra) {
        return service.findBySquadraId(idSquadra);
    }

    @GetMapping("/impianto/{idImpianto}")
    public List<Sponsorizzazione> getByImpianto(@PathVariable Long idImpianto) {
        return service.findByImpiantoId(idImpianto);
    }
}