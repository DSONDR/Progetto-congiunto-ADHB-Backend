package it.unife.sample.backend.controller;

import it.unife.sample.backend.model.CertificatoMedico;
import it.unife.sample.backend.service.CertificatoMedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * Controller per la gestione dei Certificati Medici degli Atleti.
 * Espone API per la creazione, aggiornamento e interrogazione dei certificati.
 * 
 * API Esposte:
 * - GET    /api/certificati-medici         -> Elenco certificati
 * - GET    /api/certificati-medici/{id}    -> Dettaglio certificato
 * - POST   /api/certificati-medici         -> Crea certificato
 * - PUT    /api/certificati-medici/{id}    -> Modifica certificato
 * - DELETE /api/certificati-medici/{id}    -> Cancella certificato
 * - GET    /api/certificati-medici/search  -> Cerca certificati per atleta (cf)
 */
@RestController
@RequestMapping("/api/certificati-medici")
public class CertificatoMedicoController {

    @Autowired
    private CertificatoMedicoService service;

    @GetMapping
    public List<CertificatoMedico> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificatoMedico> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public CertificatoMedico create(@RequestBody CertificatoMedico certificato) {
        return service.save(certificato);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CertificatoMedico> update(@PathVariable Long id, @RequestBody CertificatoMedico certificato) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        certificato.setIdCertificato(id);
        return ResponseEntity.ok(service.save(certificato));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<CertificatoMedico> getByUtenteCf(@RequestParam String cf) {
        return service.findByUtenteCf(cf);
    }
}