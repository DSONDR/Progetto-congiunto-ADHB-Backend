package it.unife.sample.backend.controller;

import it.unife.sample.backend.model.ComposizioneSquadra;
import it.unife.sample.backend.model.ComposizioneSquadraId;
import it.unife.sample.backend.service.ComposizioneSquadraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/composizioni-squadra")
public class ComposizioneSquadraController {

    @Autowired
    private ComposizioneSquadraService service;

    @GetMapping
    public List<ComposizioneSquadra> getAll() {
        return service.findAll();
    }

    @GetMapping("/squadra/{squadraId}/atleta/{atletaCf}")
    public ResponseEntity<ComposizioneSquadra> getById(@PathVariable Long squadraId, @PathVariable String atletaCf) {
        ComposizioneSquadraId id = new ComposizioneSquadraId(squadraId, atletaCf);
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ComposizioneSquadra create(@RequestBody ComposizioneSquadra composizione) {
        return service.save(composizione);
    }

    @PutMapping("/squadra/{squadraId}/atleta/{atletaCf}")
    public ResponseEntity<ComposizioneSquadra> update(
            @PathVariable Long squadraId,
            @PathVariable String atletaCf,
            @RequestBody ComposizioneSquadra composizione) {
        ComposizioneSquadraId id = new ComposizioneSquadraId(squadraId, atletaCf);
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.save(composizione));
    }

    @DeleteMapping("/squadra/{squadraId}/atleta/{atletaCf}")
    public ResponseEntity<Void> delete(@PathVariable Long squadraId, @PathVariable String atletaCf) {
        ComposizioneSquadraId id = new ComposizioneSquadraId(squadraId, atletaCf);
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
