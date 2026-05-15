package it.unife.sample.backend.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unife.sample.backend.model.Istruttore;
import it.unife.sample.backend.repository.IstruttoreRepository;

@Service
public class IstruttoreService {
    @Autowired
    private IstruttoreRepository repository;

    // CRUD base
    public List<Istruttore> findAll() {
        return repository.findAll();
    }

    public Optional<Istruttore> findById(String id) {
        return repository.findById(id);
    }

    public Istruttore save(Istruttore istruttore) {
        return repository.save(istruttore);
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
