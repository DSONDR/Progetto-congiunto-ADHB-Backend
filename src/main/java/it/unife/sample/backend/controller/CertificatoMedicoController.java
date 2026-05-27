package it.unife.sample.backend.controller;

import it.unife.sample.backend.model.CertificatoMedico;
import it.unife.sample.backend.service.CertificatoMedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * Controller per la gestione dei Certificati Medici degli Atleti.
 * Consente il caricamento di un nuovo certificato e la visualizzazione
 * dello storico per atleta. Modifiche e cancellazioni non sono permesse:
 * i certificati sono documenti storici immutabili.
 * Mappato lato frontend in: certificatomedico.service.ts
 * 
 * API Esposte:
 * - POST /api/certificati-medici -> Crea un nuovo elemento [BenvenutoComponent]
 * - GET /api/certificati-medici/{id} -> Recupera il dettaglio di un singolo elemento [Nessun component specifico]
 * - GET /api/certificati-medici/search -> Visualizza lo storico dei certificati medici di un atleta [Nessun component specifico]
 */
@RestController
@RequestMapping("/api/certificati-medici")
@CrossOrigin(origins = "http://localhost:4200")
public class CertificatoMedicoController {

    @Autowired
    private CertificatoMedicoService service;

    // Carica un nuovo certificato medico per un atleta
    // Il frontend invia manualmente i dati (tipo, date, medico) letti dal documento
    // Per ora inseriti a mano, ma migliorabile con tecnologia di che legga il documento
    @PostMapping
    public CertificatoMedico create(@RequestBody CertificatoMedico certificato) {
        return service.save(certificato);
    }

    // Funzionalità: Recupera il dettaglio di un singolo elemento
    @GetMapping("/{id}")
    public ResponseEntity<CertificatoMedico> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Funzionalità: Visualizza lo storico dei certificati medici di un atleta
    @GetMapping("/search")
    public List<CertificatoMedico> getByUtenteCf(@RequestParam String cf) {
        return service.findByUtenteCf(cf);
    }
}
