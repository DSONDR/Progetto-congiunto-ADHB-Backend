package it.unife.sample.backend.controller; // Cartella controller

// Devi importare sia il Model che il Repository
import it.unife.sample.backend.model.Impianto;
import it.unife.sample.backend.service.ImpiantoService;
import it.unife.sample.backend.model.Attivita;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * Controller per la gestione degli Impianti
 * Espone API per gli impianti e l'associazione con le Attività
 * NOTA: alcuni impianti sono già occupati, avviene automaticamene con le
 * attività che vengono create dall'istruttore in un dato impianto e dato
 * momento all'impianto
 * Mappato lato frontend in: impianto.service.ts
 * 
 * API Esposte:
 * - GET /api/impianto -> Recupera l'elenco di tutti gli elementi [Allenamenti AllenatoreComponent / CalendarioComponent / Corsi IstruttoreComponent / EventiComponent / Gestione AttivitaComponent / Gestione ImpiantiComponent / Gestione SponsorComponent]
 * - GET /api/impianto/{id} -> Recupera il dettaglio di un singolo elemento [Allenamenti AllenatoreComponent / CalendarioComponent / Corsi IstruttoreComponent / EventiComponent / Gestione AttivitaComponent / Gestione ImpiantiComponent / Gestione SponsorComponent]
 * - POST /api/impianto -> Crea un nuovo elemento [Allenamenti AllenatoreComponent / Corsi IstruttoreComponent / Gestione AttivitaComponent / Gestione ImpiantiComponent / Gestione SponsorComponent]
 * - PUT /api/impianto/{id} -> Aggiorna un elemento esistente [Gestione ImpiantiComponent]
 * - DELETE /api/impianto/{id} -> Elimina un elemento [Gestione ImpiantiComponent]
 * - GET /api/impianto/tipo/{TipoImpianto} -> Esegue l'operazione di findByTipoImpianto [Nessun component specifico]
 * - GET /api/impianto/stato/{stato} -> Esegue l'operazione di findByStato [Nessun component specifico]
 * - POST /api/impianto/{id}/attivita/{codiceAtt} -> Associa un'attività (corso/evento) a uno specifico impianto. [Nessun component specifico]
 * - GET /api/impianto/{id}/attivita -> Ottengo tutte le attività di un impianto [Nessun component specifico]
 */
@RestController
@RequestMapping("/api/impianto")
@CrossOrigin(origins = "http://localhost:4200")
public class ImpiantoController {

    @Autowired
    private ImpiantoService service;

    // Funzionalità: Recupera l'elenco di tutti gli elementi
    @GetMapping
    public List<Impianto> getAll() {
        return service.findAll();
    }

    // Funzionalità: Recupera il dettaglio di un singolo elemento
    @GetMapping("/{id}")
    public ResponseEntity<Impianto> getById(@PathVariable Long id) {
        Optional<Impianto> impianto = service.findById(id);
        return impianto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Funzionalità: Crea un nuovo elemento
    @PostMapping
    public Impianto create(@RequestBody Impianto impianto) {
        return service.save(impianto);
    }

    // Funzionalità: Aggiorna un elemento esistente
    @PutMapping("/{id}")
    public ResponseEntity<Impianto> update(@PathVariable Long id, @RequestBody Impianto impianto) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        impianto.setId(id);
        return ResponseEntity.ok(service.save(impianto));
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

    // Funzionalità: Esegue l'operazione di findByTipoImpianto
    @GetMapping("/tipo/{TipoImpianto}")
    public List<Impianto> findByTipoImpianto(@PathVariable String tipoImpianto) {
        return service.findByTipoImpianto(tipoImpianto);
    }

    // Funzionalità: Esegue l'operazione di findByStato
    @GetMapping("/stato/{stato}")
    public List<Impianto> findByStato(@PathVariable String stato) {
        return service.findByStato(stato);
    }

    // RELAZIONE CON ATTIVITA

    // Usato tipicamente se l'allenatore crea un'attività che nasce già con un
    // impianto associato,
    // mentre l'istruttore glielo associa se l'impianto è libero mentre crea
    // l'attività (non qua)
    // Funzionalità: Associa un'attività (corso/evento) a uno specifico impianto.
    @PostMapping("/{id}/attivita/{codiceAtt}")
    public ResponseEntity<Void> addAttivita(@PathVariable Long id, @PathVariable Long codiceAtt) {

        service.aggiungiAttivitaAImpianto(id, codiceAtt);
        return ResponseEntity.ok().build();
    }

    // Funzionalità: Ottengo tutte le attività di un impianto
    @GetMapping("/{id}/attivita")
    public List<Attivita> getAttivita(@PathVariable Long id) {

        return service.getAttivitaByImpianto(id);
    }
}
