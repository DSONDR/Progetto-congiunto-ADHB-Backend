package it.unife.sample.backend.controller; // Cartella controller

// Devi importare sia il Model che il Repository
import it.unife.sample.backend.model.Atleta;
import it.unife.sample.backend.service.AtletaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;


@RestController
@RequestMapping("/api/atleti")
public class AtletaController {

    @Autowired
    private AtletaService service;

    @GetMapping
    public List<Atleta> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Atleta> getById(@PathVariable String id) { // String, non UUID
        Optional<Atleta> atleta = service.findById(id);
        return atleta.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Atleta create(@RequestBody Atleta atleta) {
        return service.save(atleta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Atleta> update(@PathVariable String id, @RequestBody Atleta atleta) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        atleta.setCodiceFiscale(id); //Imposta il CF come ID
        return ResponseEntity.ok(service.save(atleta));
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
