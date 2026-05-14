package it.unife.sample.backend.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unife.sample.backend.model.ComposizioneSquadra;
import it.unife.sample.backend.model.ComposizioneSquadraId;
import it.unife.sample.backend.repository.ComposizioneSquadraRepository;

@Service
public class ComposizioneSquadraService {

    @Autowired
    private ComposizioneSquadraRepository repository;

    public List<ComposizioneSquadra> findAll() {
        return repository.findAll();
    }

    public Optional<ComposizioneSquadra> findById(ComposizioneSquadraId id) {
        return repository.findById(id);
    }

    public ComposizioneSquadra save(ComposizioneSquadra composizioneSquadra) {
        return repository.save(composizioneSquadra);
    }

    public void deleteById(ComposizioneSquadraId id) {
        repository.deleteById(id);
    }

    public List<ComposizioneSquadra> findBySquadraId(Long squadraId) {
        return repository.findBySquadraId(squadraId);
    }

    public List<ComposizioneSquadra> findByAtletaCf(String atletaCf) {
        return repository.findByAtletaCf(atletaCf);
    }
}
