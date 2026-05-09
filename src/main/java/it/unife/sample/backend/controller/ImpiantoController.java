package it.unife.sample.backend.controller; // Cartella controller

// Devi importare sia il Model che il Repository
import it.unife.sample.backend.model.Impianto;
import it.unife.sample.backend.service.ImpiantoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;


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
        impianto.setIdImpianto(id); 
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
    
    // Associa una attività a un impianto
    @PostMapping("/{id}/attivita/{codiceAtt}")
    public ResponseEntity<Void> addAttivita(@PathVariable Long id, @PathVariable String codiceAtt) {

        service.aggiungiAttivitaAImpianto(id, codiceAtt);
        return ResponseEntity.ok().build();
    }

    // Ottengo tutte le attività di un impianto
    @GetMapping("/{id}/attivita")
    public List<Attivita> getAttivita(@PathVariable Long id) {

        return service.getAttivitaByImpianto(id);
    }
}
