package it.unife.sample.backend.controller; // Cartella controller

// Devi importare sia il Model che il Repository
import it.unife.sample.backend.model.Allenatore;
import it.unife.sample.backend.service.AllenatoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * Essendo personale interno, gli allenatori vengono creati direttamente tramite
 * questo controller
 * Mappato lato frontend in: allenatore.service.ts
 * 
 * API Esposte:
 * - GET /api/allenatori -> Recupera elenco degli allenatori [Gestione SquadreComponent / Gestione UtentiComponent]
 * - GET /api/allenatori/{cf} -> Dettagli di un allenatore specifico [Gestione SquadreComponent / Gestione UtentiComponent]
 * - POST /api/allenatori -> Crea nuovo allenatore [Gestione SquadreComponent / Gestione UtentiComponent]
 * - PUT /api/allenatori/{cf} -> Modifica allenatore esistente [Gestione UtentiComponent]
 * - DELETE /api/allenatori/{cf} -> Cancella allenatore [Nessun component specifico]
 * - GET /api/allenatori/search -> Ricerca allenatore per grado, URL: /api/allenatori/search?grado=# [Nessun component specifico]
 * - GET /api/allenatori/filter -> /api/allenatori/filter?minGrado=#&maxGrado=# [Nessun component specifico]
 */
@RestController
@RequestMapping("/api/allenatori")
@CrossOrigin(origins = "http://localhost:4200")
public class AllenatoreController {

    @Autowired
    private AllenatoreService service;

    // Funzionalità: Recupera elenco degli allenatori
    @GetMapping
    public List<Allenatore> getAll() {
        return service.findAll();
    }

    // Funzionalità: Dettagli di un allenatore specifico
    @GetMapping("/{cf}")
    public ResponseEntity<Allenatore> getByCf(@PathVariable String cf) {
        return service.findById(cf)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    // Funzionalità: Crea nuovo allenatore
    @PostMapping
    public Allenatore create(@RequestBody Allenatore allenatore) {
        return service.save(allenatore);
    }

    // Funzionalità: Modifica allenatore esistente
    @PutMapping("/{cf}")
    public ResponseEntity<Allenatore> update(@PathVariable String cf, @RequestBody Allenatore allenatore) {
        if (!service.findById(cf).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        allenatore.setCf(cf);
        return ResponseEntity.ok(service.save(allenatore));
    }

    // Funzionalità: Cancella allenatore
    @DeleteMapping("/{cf}")
    public ResponseEntity<Void> delete(@PathVariable String cf) {
        if (!service.findById(cf).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(cf);
        return ResponseEntity.noContent().build();
    }

    // Funzionalità: Ricerca allenatore per grado, URL: /api/allenatori/search?grado=#
    @GetMapping("/search")
    public List<Allenatore> getByGrado(@RequestParam Integer grado) {
        return service.findByGrado(grado);
    }

    // Filtro allenatori tra due gradi, URL:
    // Funzionalità: /api/allenatori/filter?minGrado=#&maxGrado=#
    @GetMapping("/filter")
    public List<Allenatore> getByGradoBetween(@RequestParam Integer min, @RequestParam Integer max) {
        return service.findByGradoBetween(min, max);
    }
}
