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

    // CRUD base
    public List<Impianto> findAll() {
        return irepo.findAll();
    }

    // CRUD base
    public Optional<Impianto> findById(Long id) {
        return irepo.findById(id);
    }

    // CRUD base
    public Impianto save(Impianto impianto) {
        return irepo.save(impianto);
    }

    // CRUD base
    public void deleteById(Long id) {
        irepo.deleteById(id);
    }

    // Filtra per tipo di impianto
    // Usata in: ImpiantoController.findByTipoImpianto
    public List<Impianto> findByTipoImpianto(String tipoImpianto) {
        return irepo.findByTipoImpianto(tipoImpianto);
    }

    // Filtra per stato impianto
    // Usata in: ImpiantoController.findByStato
    public List<Impianto> findByStato(String stato) {
        return irepo.findByStato(stato);
    }

    // RELAZIONE CON ATTIVITA

    // Associo attività a impianto
    // Utile se allenatore mette attività perchè nasce già con un impianto,
    // non istruttore ce gliel'associa se impianto vuoto
    // Usata in: ImpiantoController.addAttivita
    // Frontend: Calendario / Eventi
    public void aggiungiAttivitaAImpianto(Long impiantoId, Long attivitaId) {

        Impianto impianto = irepo.findById(impiantoId).orElseThrow();

        Attivita attivita = arepo.findById(attivitaId).orElseThrow();

        impianto.getAttivita().add(attivita);
        irepo.save(impianto);
    }

    // Lista attività di un impianto
    // Usata in: ImpiantoController.getAttivita
    // Frontend: Calendario / Eventi
    public List<Attivita> getAttivitaByImpianto(Long impiantoId) {

        return irepo.findById(impiantoId)
                .map(Impianto::getAttivita)
                .orElse(new ArrayList<>());
    }

}
