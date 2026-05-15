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
 * TODO, completamente da fare
 * Controller per la gestione degli Impianti
 * Espone API per il CRUD degli impianti e l'associazione con le Attività
 * 
 * NOTA: alcuni impianti sono già occupati, avviene automaticamene con le
 * attività che vengono create dall'istruttore in un dato impianto e dato
 * momento
 * 
 * API Esposte:
 * - GET /api/impianto -> Elenco impianti
 * - GET /api/impianto/{id} -> Dettaglio impianto
 * - POST /api/impianto -> Crea impianto
 * - PUT /api/impianto/{id} -> Modifica impianto
 * - DELETE /api/impianto/{id} -> Cancella impianto
 * - GET /api/impianto/tipo/{TipoImpianto} -> Cerca per tipo
 * - GET /api/impianto/stato/{stato} -> Cerca per stato
 * - POST /api/impianto/{id}/attivita/{codiceAtt} -> Associa attività
 * all'impianto
 * - GET /api/impianto/{id}/attivita -> Visualizza attività dell'impianto
 */
@RestController
@RequestMapping("/api/impianto")
public class ImpiantoController {

    @Autowired
    private ImpiantoService service;

    @GetMapping
    public List<Impianto> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Impianto> getById(@PathVariable Long id) {
        Optional<Impianto> impianto = service.findById(id);
        return impianto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Impianto create(@RequestBody Impianto impianto) {
        return service.save(impianto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Impianto> update(@PathVariable Long id, @RequestBody Impianto impianto) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        impianto.setId(id);
        return ResponseEntity.ok(service.save(impianto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tipo/{TipoImpianto}")
    public List<Impianto> findByTipoImpianto(@PathVariable String tipoImpianto) {
        return service.findByTipoImpianto(tipoImpianto);
    }

    @GetMapping("/stato/{stato}")
    public List<Impianto> findByStato(@PathVariable String stato) {
        return service.findByStato(stato);
    }

    // RELAZIONE CON ATTIVITA

    // Funzionalità: Associa un'attività (corso/evento) a uno specifico impianto.
    // Usato tipicamente se l'allenatore crea un'attività che nasce già con un
    // impianto associato,
    // mentre l'istruttore glielo associa se l'impianto è libero mentre crea
    // l'attività (non qua)
    @PostMapping("/{id}/attivita/{codiceAtt}")
    public ResponseEntity<Void> addAttivita(@PathVariable Long id, @PathVariable Long codiceAtt) {

        service.aggiungiAttivitaAImpianto(id, codiceAtt);
        return ResponseEntity.ok().build();
    }

    // Ottengo tutte le attività di un impianto
    @GetMapping("/{id}/attivita")
    public List<Attivita> getAttivita(@PathVariable Long id) {

        return service.getAttivitaByImpianto(id);
    }
}
