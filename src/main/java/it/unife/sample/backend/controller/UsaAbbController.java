package it.unife.sample.backend.controller;

import it.unife.sample.backend.model.Attivita;
import it.unife.sample.backend.model.Sottoscrizione;
import it.unife.sample.backend.model.UsaAbb;
import it.unife.sample.backend.model.UsaAbbId;
import it.unife.sample.backend.service.AttivitaService;
import it.unife.sample.backend.service.SottoscrizioneService;
import it.unife.sample.backend.service.UsaAbbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/usa-abb")
public class UsaAbbController {

    @Autowired
    private UsaAbbService service;

    @Autowired
    private SottoscrizioneService sottoscrizioneService;

    @Autowired
    private AttivitaService attivitaService;

    @GetMapping
    public List<UsaAbb> getAll() {
        return service.findAll();
    }

    @GetMapping("/{numeroAbb}/{codiceAtt}/{cf}/{dataUso}")
    public ResponseEntity<UsaAbb> getById(@PathVariable Long numeroAbb,
                                          @PathVariable Long codiceAtt,
                                          @PathVariable String cf,
                                          @PathVariable String dataUso) {
        UsaAbbId id = new UsaAbbId(numeroAbb, codiceAtt, cf, java.time.LocalDate.parse(dataUso));
        return service.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public UsaAbb create(@RequestBody UsaAbb usaAbb) {
        return service.save(usaAbb);
    }

    @DeleteMapping("/{numeroAbb}/{codiceAtt}/{cf}/{dataUso}")
    public ResponseEntity<Void> delete(@PathVariable Long numeroAbb,
                                       @PathVariable Long codiceAtt,
                                       @PathVariable String cf,
                                       @PathVariable String dataUso) {
        UsaAbbId id = new UsaAbbId(numeroAbb, codiceAtt, cf, java.time.LocalDate.parse(dataUso));
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    //@PostMapping("/utilizza")
    
}
