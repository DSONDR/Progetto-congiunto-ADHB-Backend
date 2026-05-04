package it.unife.sample.backend.controller; // Cartella controller

// Devi importare sia il Model che il Repository
import it.unife.sample.backend.model.Squadra;
import it.unife.sample.backend.service.SquadraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/squadre")
public class SquadraController {

    @Autowired
    private SquadraService service;

    @GetMapping
    public List<Squadra> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Squadra> getById(@PathVariable Long id) {
        Optional<Squadra> squadra = service.findById(id);
        return squadra.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Squadra create(@RequestBody Squadra squadra) {
        return service.save(squadra);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Squadra> update(@PathVariable Long id, @RequestBody Squadra squadra) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        utente.setCodiceFiscale(id); //Imposta il CF come ID
        return ResponseEntity.ok(service.save(squadra));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
 
    @GetMapping("/per-allenatore/{cf}")
    public List<Squadra> findByAllenatoreCf(@PathVariable String cf) {
        return service.findByAllenatoreCf(cf);
    }

    @GetMapping("/per-atleta/{cf}")
    public List<Squadra> findByAtletaCf(@PathVariable String cf) {
        return service.findByAtletaCf(cf);
    }
    
    // URL: POST /api/squadre/id/atleti/RSSMRA80...
    @PostMapping("/{id}/atleti/{atletaCf}")
    public ResponseEntity<Void> addAtleta(@PathVariable Long id, @PathVariable String atletaCf) {
        service.aggiungiAtletaASquadra(id, atletaCf);
        return ResponseEntity.ok().build();	//Se è okay non ritorno tutto ma solo il codice 200
    }
    
    //Ottengo tutti gli atleti di una squadra   
    @GetMapping("/{id}/atleti")
    public List<Atleta> getAtleti(@PathVariable Long id) {
        return service.getAtletiBySquadra(id);
    }

    //Tutti gli atleti con la visita medica scaduta
    @GetMapping("/{id}/atleti/scaduti")
    public List<Atleta> getAtletiScaduti(@PathVariable Long id) {
        return service.getAtletiScadutiBySquadra(id);
    }
}
