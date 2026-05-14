package it.unife.sample.backend.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unife.sample.backend.model.PartecipazioneSq;
import it.unife.sample.backend.model.PartecipazioneSqId;
import it.unife.sample.backend.repository.PartecipazioneSqRepository;

@Service
public class PartecipazioneSqService {

    @Autowired
    private PartecipazioneSqRepository repository;

    public List<PartecipazioneSq> findAll() {
        return repository.findAll();
    }

    public Optional<PartecipazioneSq> findById(PartecipazioneSqId id) {
        return repository.findById(id);
    }

    public PartecipazioneSq save(PartecipazioneSq partecipazione) {
        return repository.save(partecipazione);
    }

    public void deleteById(PartecipazioneSqId id) {
        repository.deleteById(id);
    }

    public List<PartecipazioneSq> findBySquadraId(Long squadraId) {
        return repository.findBySquadraId(squadraId);
    }

    public List<PartecipazioneSq> findByAttivitaId(Long attivitaId) {
        return repository.findByAttivitaCodiceAtt(attivitaId);
    }
}
