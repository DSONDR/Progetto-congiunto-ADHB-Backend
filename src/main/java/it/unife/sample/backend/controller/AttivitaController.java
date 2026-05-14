package it.unife.sample.backend.controller;

import it.unife.sample.backend.dto.request.AttivitaRequestDTO;
import it.unife.sample.backend.dto.response.AttivitaResponseDTO;
import it.unife.sample.backend.service.AttivitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/attivita")
public class AttivitaController {

    @Autowired
    private AttivitaService service;

    @GetMapping
    public List<AttivitaResponseDTO> getAll() {
        return service.findAllDTO();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttivitaResponseDTO> getById(@PathVariable Long id) {
        return service.findDtoById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AttivitaResponseDTO> create(@Valid @RequestBody AttivitaRequestDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/calendario")
    public List<AttivitaResponseDTO> getCalendario(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inizio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fine) {
        return service.getCalendario(inizio, fine);
    }

    @GetMapping("/filtra")
    public List<AttivitaResponseDTO> filtra(
            @RequestParam(required = false) Long idImpianto,
            @RequestParam(required = false) Double prezzo,
            @RequestParam(required = false) String target,
            @RequestParam(required = false) String tipoEvento,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inizio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fine) {
        return service.filtra(idImpianto, prezzo, target, tipoEvento, inizio, fine);
    }

    @GetMapping("/{id}/posti-disponibili")
    public ResponseEntity<Boolean> isPostoDisponibile(@PathVariable Long id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.isPostoDisponibile(id));
    }

    @GetMapping("/{id}/iscritti")
    public ResponseEntity<Integer> getNumeroIscritti(@PathVariable Long id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.getNumeroIscritti(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttivitaResponseDTO> update(@PathVariable Long id, @Valid @RequestBody AttivitaRequestDTO dto) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
