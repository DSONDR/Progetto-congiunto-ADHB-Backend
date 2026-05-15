package it.unife.sample.backend.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unife.sample.backend.model.Impianto;
import it.unife.sample.backend.model.Attivita;

import it.unife.sample.backend.repository.ImpiantoRepository;
import it.unife.sample.backend.repository.AttivitaRepository;

@Service
public class ImpiantoService {

    @Autowired
    private ImpiantoRepository irepo;

    @Autowired
    private AttivitaRepository arepo;

    // CRUD BASE
    public List<Impianto> findAll() {
        return irepo.findAll();
    }

    public Optional<Impianto> findById(Long id) {
        return irepo.findById(id);
    }

    public Impianto save(Impianto impianto) {
        return irepo.save(impianto);
    }

    public void deleteById(Long id) {
        irepo.deleteById(id);
    }

    // Filtra per tipo di impianto
    public List<Impianto> findByTipoImpianto(String tipoImpianto) {
        return irepo.findByTipoImpianto(tipoImpianto);
    }

    // Filtra per stato impianto
    public List<Impianto> findByStato(String stato) {
        return irepo.findByStato(stato);
    }

    // RELAZIONE CON ATTIVITA

    // Associo attività a impianto
    // TODO, da usare ancora
    // Utile se allenatore mette attività perchè nasce già con un impianto,
    // non istruttore ce gliel'associa se impianto vuoto
    public void aggiungiAttivitaAImpianto(Long impiantoId, Long attivitaId) {

        Impianto impianto = irepo.findById(impiantoId).orElseThrow();

        Attivita attivita = arepo.findById(attivitaId).orElseThrow();

        impianto.getAttivita().add(attivita);
        irepo.save(impianto);
    }

    // Lista attività di un impianto
    // TODO, da usare ancora
    public List<Attivita> getAttivitaByImpianto(Long impiantoId) {

        return irepo.findById(impiantoId)
                .map(Impianto::getAttivita)
                .orElse(new ArrayList<>());
    }

}