package it.unife.sample.backend.service;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unife.sample.backend.model.Utente;
import it.unife.sample.backend.repository.UtenteRepository;

@Service
public class UtenteService {
     @Autowired
    private UtenteRepository repository;

    public List<Utente> findAll() {
        return repository.findAll();
    }

    public Optional<Utente> findById(String id) {
        return repository.findById(id);
    }

    public Utente save(Utente utente) {
        return repository.save(utente);
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
