package it.unife.sample.backend.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unife.sample.backend.model.Atleta;
import it.unife.sample.backend.repository.AtletaRepository;

@Service
public class AtletaService {
    @Autowired
    private AtletaRepository repository;

    // Crud base
    public List<Atleta> findAll() {
        return repository.findAll();
    }

    public Optional<Atleta> findById(String id) {
        return repository.findById(id);
    }

    public Atleta save(Atleta atleta) {
        return repository.save(atleta);
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }

    // Altre funzionalità in utente e lui le eredita
}
