package it.unife.sample.backend.controller; // Cartella controller

// Devi importare sia il Model che il Repository
import it.unife.sample.backend.model.Allenatore;
import it.unife.sample.backend.service.AllenatoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/allenatori")
public class AllenatoreController {

    @Autowired
    private AllenatoreService service;
    
    //CRUD cf univoco
    @GetMapping
    public List<Allenatore> getAll() {
        return service.findAll();
    }

    @GetMapping("/{cf}")
    public ResponseEntity<Allenatore> getByCf(@PathVariable String cf) {
        return service.findById(cf)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Allenatore create(@RequestBody Allenatore allenatore) {
        return service.save(allenatore);
    }

    @PutMapping("/{cf}")
    public ResponseEntity<Allenatore> update(@PathVariable String cf, @RequestBody Allenatore allenatore) {
        if (!service.findById(cf).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        allenatore.setCf(cf);
        return ResponseEntity.ok(service.save(allenatore));
    }

    @DeleteMapping("/{cf}")
    public ResponseEntity<Void> delete(@PathVariable String cf) {
        if (!service.findById(cf).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(cf);
        return ResponseEntity.noContent().build();
    }
    
    
    //Ricerca allenatore per grado, URL: /api/allenatori/search?grado=#
    @GetMapping("/search")
    public List<Allenatore> getByGrado(@RequestParam Integer grado){
 	return service.findByGrado(grado);   
    }    
    //Ricerca allenatore tra due gradi, URL: /api/allenatori/filter?min=#&max=#
    @GetMapping("/filter")
    public List<Allenatore> getByGradoBetween(@RequestParam Integer min, @RequestParam Integer max){
        return service.findByGradoBetween(min, max);
    }
 
    
}
