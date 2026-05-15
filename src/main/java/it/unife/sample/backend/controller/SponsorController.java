package it.unife.sample.backend.controller;

import it.unife.sample.backend.model.Sponsor;
import it.unife.sample.backend.model.Squadra;
import it.unife.sample.backend.model.Impianto;
import it.unife.sample.backend.service.SponsorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * TODO completamente da fare
 * Controller per la gestione degli Sponsor.
 * Espone API per il CRUD degli sponsor e l'associazione con Squadre e Impianti.
 * 
 * API Esposte:
 * - GET /api/sponsor -> Elenco sponsor
 * - GET /api/sponsor/{id} -> Dettaglio sponsor
 * - POST /api/sponsor -> Crea sponsor
 * - PUT /api/sponsor/{id} -> Modifica sponsor
 * - DELETE /api/sponsor/{id} -> Cancella sponsor
 * - GET /api/sponsor/azienda/{azienda} -> Cerca per nome azienda
 * - GET /api/sponsor/piva/{pIva} -> Cerca per partita IVA
 * - POST /api/sponsor/{id}/squadre/{idSquadra} -> Associa sponsor a squadra
 * - GET /api/sponsor/{id}/squadre -> Visualizza squadre sponsorizzate
 * - POST /api/sponsor/{id}/impianti/{idImpianto} -> Associa sponsor a impianto
 * - GET /api/sponsor/{id}/impianti -> Visualizza impianti sponsorizzati
 */
@RestController
@RequestMapping("/api/sponsor")
public class SponsorController {

    @Autowired
    private SponsorService service;

    @GetMapping
    public List<Sponsor> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sponsor> getById(@PathVariable String id) {
        Optional<Sponsor> sponsor = service.findById(id);
        return sponsor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Sponsor create(@RequestBody Sponsor sponsor) {
        return service.save(sponsor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sponsor> update(@PathVariable String id, @RequestBody Sponsor sponsor) {

        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        sponsor.setPIva(id);
        return ResponseEntity.ok(service.save(sponsor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {

        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Ricerca sponsor per azienda
    @GetMapping("/azienda/{azienda}")
    public List<Sponsor> findByAzienda(@PathVariable String azienda) {
        return service.findByAzienda(azienda);
    }

    // Ricerca sponsor per partita iva
    @GetMapping("/piva/{pIva}")
    public List<Sponsor> findByPIva(@PathVariable String pIva) {
        return service.findByPIva(pIva);
    }

    // RELAZIONE CON SQUADRA

    // Associa uno sponsor a una determinata squadra.
    @PostMapping("/{id}/squadre/{idSquadra}")
    public ResponseEntity<Void> addSquadra(@PathVariable String id, @PathVariable Long idSquadra) {

        service.aggiungiSponsorASquadra(id, idSquadra);
        return ResponseEntity.ok().build();
    }

    // Ottengo tutte le squadre sponsorizzate
    @GetMapping("/{id}/squadre")
    public List<Squadra> getSquadre(@PathVariable String id) {

        return service.getSquadreBySponsor(id);
    }

    // RELAZIONE CON IMPIANTO

    // Associa uno sponsor a un determinato impianto.
    @PostMapping("/{id}/impianti/{idImpianto}")
    public ResponseEntity<Void> addImpianto(@PathVariable String id,
            @PathVariable Long idImpianto) {

        service.aggiungiSponsorAImpianto(id, idImpianto);

        return ResponseEntity.ok().build();
    }

    // Ottengo tutti gli impianti sponsorizzati
    @GetMapping("/{id}/impianti")
    public List<Impianto> getImpianti(@PathVariable String id) {

        return service.getImpiantiBySponsor(id);
    }

}