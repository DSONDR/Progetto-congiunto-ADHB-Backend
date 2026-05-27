package it.unife.sample.backend.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unife.sample.backend.model.Allenatore;
import it.unife.sample.backend.repository.AllenatoreRepository;

@Service
public class AllenatoreService {

    @Autowired
    private AllenatoreRepository repository;

    // CRUD base
    public List<Allenatore> findAll() {
        return repository.findAll();
    }

    // CRUD base
    public Optional<Allenatore> findById(String id) {
        return repository.findById(id);
    }

    // CRUD base
    public Allenatore save(Allenatore allenatore) {
        // TODO: qui da aggiungere controlli sui gradi, ma se servono chi li chiede?
        return repository.save(allenatore);
    }

    // CRUD base
    public void deleteById(String id) {
        repository.deleteById(id);
    }
    
    // Trova allenatori con certo grado
    // Usata in: AllenatoreController.getByGrado
    public List<Allenatore> findByGrado(Integer grado) {
        return repository.findByGrado(grado);
    }

    // Trova gli allenatori tra due gradi
    // Usata in: AllenatoreController.getByGradoBetween
    public List<Allenatore> findByGradoBetween(Integer min, Integer max) {
        if (min > max) {
            return repository.findByGradoBetween(max, min);
        }
        return repository.findByGradoBetween(min, max);
    }
}
