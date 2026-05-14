package it.unife.sample.backend.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unife.sample.backend.model.Sponsorizzazione;
import it.unife.sample.backend.repository.SponsorizzazioneRepository;

@Service
public class SponsorizzazioneService {

    @Autowired
    private SponsorizzazioneRepository repository;

    public List<Sponsorizzazione> findAll() {
        return repository.findAll();
    }

    public Optional<Sponsorizzazione> findById(Long id) {
        return repository.findById(id);
    }

    public Sponsorizzazione save(Sponsorizzazione sponsorizzazione) {
        return repository.save(sponsorizzazione);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public List<Sponsorizzazione> findBySponsorPIva(String pIva) {
        return repository.findBySponsorPIva(pIva);
    }

    public List<Sponsorizzazione> findBySquadraId(Long idSquadra) {
        return repository.findBySquadraId(idSquadra);
    }

    public List<Sponsorizzazione> findByImpiantoId(Long idImpianto) {
        return repository.findByImpiantoId(idImpianto);
    }
}