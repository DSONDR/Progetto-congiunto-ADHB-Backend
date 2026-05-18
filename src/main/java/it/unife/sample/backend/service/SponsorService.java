package it.unife.sample.backend.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.unife.sample.backend.model.Sponsor;
import it.unife.sample.backend.model.Squadra;
import it.unife.sample.backend.model.Impianto;

import it.unife.sample.backend.repository.SponsorRepository;
import it.unife.sample.backend.repository.SquadraRepository;
import it.unife.sample.backend.repository.ImpiantoRepository;

@Service
public class SponsorService {

    @Autowired
    private SponsorRepository srepo;

    @Autowired
    private SquadraRepository sqrepo;

    @Autowired
    private ImpiantoRepository irepo;

    public List<Sponsor> findAll() {
        return srepo.findAll();
    }

    public Optional<Sponsor> findById(String id) {
        return srepo.findById(id);
    }

    public Sponsor save(Sponsor sponsor) {
        return srepo.save(sponsor);
    }

    public void deleteById(String id) {
        srepo.deleteById(id);
    }

    // Metodi di ricerca
    // Ricerca sponsor per azienda
    public List<Sponsor> findByAzienda(String azienda) {
        return srepo.findByAzienda(azienda);
    }

    // Ricerca sponsor per partita iva
    public List<Sponsor> findByPIva(String pIva) {
        return srepo.findByPIva(pIva);
    }

    // RELAZIONE CON SQUADRA
    // Associo sponsor a squadra
    @Transactional
    public void aggiungiSponsorASquadra(String sponsorId, Long squadraId) {
        Sponsor sponsor = srepo.findById(sponsorId)
                .orElseThrow(() -> new IllegalArgumentException("Sponsor non trovato"));

        Squadra squadra = sqrepo.findById(squadraId)
                .orElseThrow(() -> new IllegalArgumentException("Squadra non trovata"));

        sponsor.getSquadre().add(squadra);
        srepo.save(sponsor);
    }

    // Lista squadre sponsorizzate
    public List<Squadra> getSquadreBySponsor(String sponsorId) {
        return srepo.findById(sponsorId).map(Sponsor::getSquadre).orElse(new ArrayList<>());
    }

    // RELAZIONE CON IMPIANTO
    // Associo sponsor a impianto
    @Transactional
    public void aggiungiSponsorAImpianto(String sponsorId, Long impiantoId) {
        Sponsor sponsor = srepo.findById(sponsorId)
                .orElseThrow(() -> new IllegalArgumentException("Sponsor non trovato"));
        Impianto impianto = irepo.findById(impiantoId)
                .orElseThrow(() -> new IllegalArgumentException("Impianto non trovato"));

        sponsor.getImpianti().add(impianto);
        srepo.save(sponsor);
    }

    // Lista impianti sponsorizzati
    public List<Impianto> getImpiantiBySponsor(String sponsorId) {
        return srepo.findById(sponsorId).map(Sponsor::getImpianti).orElse(new ArrayList<>());
    }

    // Cerca sponsor tramite squadra sponsorizzata
    public List<Sponsor> findBySquadraId(Long idSquadra) {
        return srepo.findBySquadreId(idSquadra);
    }

    // Cerca sponsor tramite impianto sponsorizzato
    public List<Sponsor> findByImpiantoId(Long idImpianto) {
        return srepo.findByImpiantiId(idImpianto);
    }

}