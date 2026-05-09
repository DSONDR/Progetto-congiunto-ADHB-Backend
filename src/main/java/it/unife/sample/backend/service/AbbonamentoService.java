package it.unife.sample.backend.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.unife.sample.backend.model.Abbonamento;
import it.unife.sample.backend.repository.AbbonamentoRepository;

@Service
public class AbbonamentoService {
    @Autowired
    private AbbonamentoRepository repo;
    
    // CRUD base
    public List<Abbonamento> findAll() {
        return repo.findAll();
    }
    
    public Optional<Abbonamento> findById(Long id) {
        return repo.findById(id);
    }

    public Abbonamento save(Abbonamento abbonamento) {
        return repo.save(abbonamento);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}
