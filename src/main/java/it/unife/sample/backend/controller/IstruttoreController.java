package it.unife.sample.backend.controller; // Cartella controller

// Devi importare sia il Model che il Repository
import it.unife.sample.backend.model.Istruttore;
import it.unife.sample.backend.service.IstruttoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/istruttori")
public class IstruttoreController {

    @Autowired
    private IstruttoreService service;

    @GetMapping
    public List<Istruttore> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Istruttore> getById(@PathVariable String id) { // String, non UUID
        Optional<Istruttore> istruttore = service.findById(id);
        return istruttore.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Istruttore create(@RequestBody Istruttore istruttore) {
        return service.save(istruttore);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Istruttore> update(@PathVariable String id, @RequestBody Istruttore istruttore) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        istruttore.setCodiceFiscale(id); //Imposta il CF come ID
        return ResponseEntity.ok(service.save(istruttore));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
