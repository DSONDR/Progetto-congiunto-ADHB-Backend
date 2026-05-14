package it.unife.sample.backend.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unife.sample.backend.model.DateAtt;
import it.unife.sample.backend.model.DateAttId;
import it.unife.sample.backend.repository.DateAttRepository;

@Service
public class DateAttService {

    @Autowired
    private DateAttRepository repository;

    public List<DateAtt> findAll() {
        return repository.findAll();
    }

    public Optional<DateAtt> findById(DateAttId id) {
        return repository.findById(id);
    }

    public DateAtt save(DateAtt dateAtt) {
        return repository.save(dateAtt);
    }

    public void deleteById(DateAttId id) {
        repository.deleteById(id);
    }

    public List<DateAtt> findByAttivitaCodiceAtt(Long codiceAtt) {
        return repository.findByAttivitaCodiceAtt(codiceAtt);
    }
}