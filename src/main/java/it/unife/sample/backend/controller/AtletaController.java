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

    @GetMapping("/{cf}")
    public ResponseEntity<Atleta> getByCf(@PathVariable String cf) {
        return service.findById(cf)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Atleta create(@RequestBody Atleta atleta) {
        return service.save(atleta);
    }

    @PutMapping("/{cf}")
    public ResponseEntity<Atleta> update(@PathVariable String cf, @RequestBody Atleta atleta) {
        if (!service.findById(cf).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        atleta.setCf(cf);
        return ResponseEntity.ok(service.save(atleta));
    }

    @DeleteMapping("/{cf}")
    public ResponseEntity<Void> delete(@PathVariable String cf) {
        if (!service.findById(cf).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(cf);
        return ResponseEntity.noContent().build();
    }
}
