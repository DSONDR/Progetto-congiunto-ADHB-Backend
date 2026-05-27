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

    @Autowired
    private it.unife.sample.backend.repository.SponsorRepository sponsorRepository;

    // CRUD base
    public List<Sponsorizzazione> findAll() {
        return repository.findAll();
    }

    // CRUD base
    public Optional<Sponsorizzazione> findById(Long id) {
        return repository.findById(id);
    }

    // Funzionalità di salvataggio inrobustita per problemi lato frontend
    // CRUD base
    public Sponsorizzazione save(Sponsorizzazione sponsorizzazione) {
        if (sponsorizzazione.getSponsor() != null && sponsorizzazione.getSponsor().getPartitaIva() != null) {
            sponsorRepository.findById(sponsorizzazione.getSponsor().getPartitaIva())
                    .ifPresent(sponsorizzazione::setSponsor);
        }
        return repository.save(sponsorizzazione);
    }

    // CRUD base
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    // --- Metodi di filtraggio ---
    // Funzionalità: Recupera i dati filtrando per SponsorPartitaIva
    // Usata in: SponsorizzazioneController.getBySponsor
    // Frontend: Gestione Sponsor
    public List<Sponsorizzazione> findBySponsorPartitaIva(String partitaIva) {
        return repository.findBySponsorPartitaIva(partitaIva);
    }

    // Funzionalità: Recupera i dati filtrando per SquadraId
    // Usata in: SponsorizzazioneController.getBySquadra
    // Frontend: Gestione Sponsor
    public List<Sponsorizzazione> findBySquadraId(Long idSquadra) {
        return repository.findBySquadraId(idSquadra);
    }

    // Funzionalità: Recupera i dati filtrando per ImpiantoId
    // Usata in: SponsorizzazioneController.getByImpianto
    // Frontend: Gestione Sponsor
    public List<Sponsorizzazione> findByImpiantoId(Long idImpianto) {
        return repository.findByImpiantoId(idImpianto);
    }
}
