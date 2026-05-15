package it.unife.sample.backend.controller; // Cartella controller

// Devi importare sia il Model che il Repository
import it.unife.sample.backend.model.Atleta;
import it.unife.sample.backend.service.AtletaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * Controller per la gestione dell'Atleta (ACCESSO IN LETTURA)
 * Permette la visualizzazione dell'elenco degli atleti e del dettaglio singolo
 * NOTA: La creazione, cancellazione e aggiornamento dell'account avvengono
 * tramite i flussi di registrazione e gestione in AuthController
 * 
 * API Esposte:
 * - GET /api/atleti -> Elenco di tutti gli atleti
 * - GET /api/atleti/{cf} -> Dettaglio singolo atleta
 * Nota:
 * La parte di gestione (update, delete) viene effettuata tramite AuthController
 */
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
}
