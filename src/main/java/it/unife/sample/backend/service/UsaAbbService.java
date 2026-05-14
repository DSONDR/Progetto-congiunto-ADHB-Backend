package it.unife.sample.backend.service;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.unife.sample.backend.model.Attivita;
import it.unife.sample.backend.model.Sottoscrizione;
import it.unife.sample.backend.model.UsaAbb;
import it.unife.sample.backend.model.UsaAbbId;
import it.unife.sample.backend.repository.SottoscrizioneRepository;
import it.unife.sample.backend.repository.UsaAbbRepository;

@Service
public class UsaAbbService {
    @Autowired private UsaAbbRepository usaRepo;
    @Autowired private SottoscrizioneRepository sottRepo;

    //CRUD base
    public List<UsaAbb> findAll() {
        return usaRepo.findAll();
    }

    public Optional<UsaAbb> findById(UsaAbbId id) {
        return usaRepo.findById(id);
    }

    @Transactional
    public UsaAbb save(UsaAbb a) {
        return usaRepo.save(a);
    }

    @Transactional
    public void deleteById(UsaAbbId id) {
        usaRepo.deleteById(id);
    }
    
    //Funzione per registrare utilizzo, usa sottoscrizione e i due metodi del repo (?)
}
