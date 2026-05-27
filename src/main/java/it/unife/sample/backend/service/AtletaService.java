package it.unife.sample.backend.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.unife.sample.backend.model.Atleta;
import it.unife.sample.backend.repository.AtletaRepository;

@Service
public class AtletaService {
    @Autowired
    private AtletaRepository repository;

    // CRUD base
    public List<Atleta> findAll() {
        return repository.findAll();
    }

    // CRUD base
    public Optional<Atleta> findById(String id) {
        return repository.findById(id);
    }

    // CRUD base
    public Atleta save(Atleta atleta) {
        return repository.save(atleta);
    }

    // CRUD base
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    // Altre funzionalità in utente e lui le eredita

    // Gamification: spesa dei punti
    // Usata in: AtletaController.spendiPunti
    @Transactional
    public Atleta spendiPunti(String cf, Integer puntiDaSpendere) {
        Atleta atleta = repository.findById(cf)
                .orElseThrow(() -> new IllegalArgumentException("Atleta non trovato"));

        int puntiAttuali = (atleta.getPuntiGamification() != null) ? atleta.getPuntiGamification() : 0;
        
        if (puntiAttuali < puntiDaSpendere) {
            throw new IllegalStateException("Punti insufficienti per il premio selezionato");
        }
        
        atleta.setPuntiGamification(puntiAttuali - puntiDaSpendere);
        return repository.save(atleta);
    }
}
