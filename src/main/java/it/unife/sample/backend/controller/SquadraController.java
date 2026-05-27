package it.unife.sample.backend.controller;

import it.unife.sample.backend.dto.request.SquadraRequestDTO;
import it.unife.sample.backend.dto.response.SquadraResponseDTO;
import it.unife.sample.backend.dto.response.UserResponseDTO;
import it.unife.sample.backend.service.SquadraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

/**
 * Controller per la gestione delle Squadre.
 * Segue il pattern di Security by Design, omettendo CRUD con Entità e
 * affidandosi ai DTO per l'inserimento/modifica.
 * Mappato lato frontend in: squadra.service.ts
 * 
 * API Esposte:
 * - GET /api/squadre -> Recupera l'elenco di tutti gli elementi [Allenamenti AllenatoreComponent / Gestione AttivitaComponent / Gestione SponsorComponent / Gestione SquadreComponent / SquadreComponent]
 * - GET /api/squadre/{id} -> Recupera il dettaglio di un singolo elemento [Allenamenti AllenatoreComponent / Gestione AttivitaComponent / Gestione SponsorComponent / Gestione SquadreComponent / SquadreComponent]
 * - GET /api/squadre/per-allenatore/{cf} -> Recupera le squadre gestite da un allenatore [SquadreComponent / Allenamenti AllenatoreComponent]
 * - GET /api/squadre/per-atleta/{cf} -> Recupera le squadre a cui partecipa un atleta [SquadreComponent]
 * - POST /api/squadre -> Creazione di una nuova squadra. [Allenamenti AllenatoreComponent / Gestione AttivitaComponent / Gestione SponsorComponent / Gestione SquadreComponent]
 * - PUT /api/squadre/{id} -> Modifica dei dati di una squadra (es. nome o allenatore). [Gestione SquadreComponent]
 * - DELETE /api/squadre/{id} -> Scioglimento di una squadra. [Gestione SquadreComponent]
 * - POST /api/squadre/{id}/atleti/{atletaCf} -> Iscrizione di un atleta a una squadra. [Gestione SquadreComponent]
 * - DELETE /api/squadre/{id}/atleti/{atletaCf} -> Rimozione di un atleta dal roster della squadra. [Gestione SquadreComponent]
 * - GET /api/squadre/{id}/atleti -> Restituisce l'elenco completo degli atleti che compongono la squadra. [SquadreComponent / Gestione SquadreComponent]
 * - GET /api/squadre/{id}/atleti/scaduti -> Filtra e restituisce solo gli atleti della squadra che non hanno un certificato medico valido. [Nessun component specifico]
 */
@RestController
@RequestMapping("/api/squadre")
@CrossOrigin(origins = "http://localhost:4200")
public class SquadraController {

    @Autowired
    private SquadraService service;

    // --- LETTURA SQUADRE ---

    // Funzionalità: Recupera l'elenco di tutti gli elementi
    @GetMapping
    public ResponseEntity<List<SquadraResponseDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    // Funzionalità: Recupera il dettaglio di un singolo elemento
    @GetMapping("/{id}")
    public ResponseEntity<SquadraResponseDTO> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Funzionalità: Recupera le squadre gestite da un allenatore
    @GetMapping("/per-allenatore/{cf}")
    public ResponseEntity<List<SquadraResponseDTO>> findByAllenatoreCf(@PathVariable String cf) {
        return ResponseEntity.ok(service.findByAllenatoreCf(cf));
    }

    // Funzionalità: Recupera le squadre a cui partecipa un atleta
    @GetMapping("/per-atleta/{cf}")
    public ResponseEntity<List<SquadraResponseDTO>> findByAtletaCf(@PathVariable String cf) {
        return ResponseEntity.ok(service.findByAtletaCf(cf));
    }

    // --- GESTIONE SQUADRA ---

    // Funzionalità: Creazione di una nuova squadra.
    @PostMapping
    public ResponseEntity<SquadraResponseDTO> create(@RequestBody @Valid SquadraRequestDTO request) {
        return ResponseEntity.ok(service.creaSquadra(request));
    }

    // Funzionalità: Modifica dei dati di una squadra (es. nome o allenatore).
    @PutMapping("/{id}")
    public ResponseEntity<SquadraResponseDTO> update(@PathVariable Long id, @RequestBody @Valid SquadraRequestDTO request) {
        try {
            return ResponseEntity.ok(service.modificaSquadra(id, request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Funzionalità: Scioglimento di una squadra.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // --- GESTIONE ROSTER ATLETI ---

    // Funzionalità: Iscrizione di un atleta a una squadra.
    @PostMapping("/{id}/atleti/{atletaCf}")
    public ResponseEntity<Void> addAtleta(@PathVariable Long id, @PathVariable String atletaCf) {
        service.aggiungiAtletaASquadra(id, atletaCf);
        return ResponseEntity.ok().build();
    }

    // Funzionalità: Rimozione di un atleta dal roster della squadra.
    @DeleteMapping("/{id}/atleti/{atletaCf}")
    public ResponseEntity<Void> removeAtleta(@PathVariable Long id, @PathVariable String atletaCf) {
        service.rimuoviAtletaDaSquadra(id, atletaCf);
        return ResponseEntity.ok().build();
    }

    // Funzionalità: Restituisce l'elenco completo degli atleti che compongono la squadra.
    @GetMapping("/{id}/atleti")
    public ResponseEntity<List<UserResponseDTO>> getAtleti(@PathVariable Long id) {
        return ResponseEntity.ok(service.getAtletiBySquadra(id));
    }

    // Funzionalità: Filtra e restituisce solo gli atleti della squadra che non hanno un certificato medico valido.
    @GetMapping("/{id}/atleti/scaduti")
    public ResponseEntity<List<UserResponseDTO>> getAtletiScaduti(@PathVariable Long id) {
        return ResponseEntity.ok(service.getAtletiScadutiBySquadra(id));
    }
}
