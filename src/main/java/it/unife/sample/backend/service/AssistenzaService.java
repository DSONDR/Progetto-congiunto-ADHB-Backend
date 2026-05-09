package it.unife.sample.backend.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unife.sample.backend.model.Assistenza;
import it.unife.sample.backend.model.Utente;

import it.unife.sample.backend.repository.AssistenzaRepository;
import it.unife.sample.backend.repository.UtenteRepository;

@Service
public class AssistenzaService {

    @Autowired
    private AssistenzaRepository arepo;

    @Autowired
    private UtenteRepository urepo;

    public List<Assistenza> findAll() {
        return arepo.findAll();
    }

    public Optional<Assistenza> findById(Long id) {
        return arepo.findById(id);
    }

    public Assistenza save(Assistenza assistenza) {
        return arepo.save(assistenza);
    }

    public void deleteById(Long id) {
        arepo.deleteById(id);
    }

    // Ricerca ticket per stato
    public List<Assistenza> findByStato(String stato) {
        return arepo.findByStato(stato);
    }

    // Ricerca ticket per tipo assistenza
    public List<Assistenza> findByTipoAss(String tipoAss) {
        return arepo.findByTipoAss(tipoAss);
    }

    // Ricerca ticket per soddisfazione
    public List<Assistenza> findBySoddisfazione(String soddisfazione) {
        return arepo.findBySoddisfazione(soddisfazione);
    }

    // RELAZIONE CON UTENTE

    // Associo ticket a utente
    public void associaUtente(Long ticketId, String cf) {

        Assistenza assistenza = arepo.findById(ticketId).orElseThrow();

        Utente utente = urepo.findById(cf).orElseThrow();

        assistenza.setUtente(utente);
        arepo.save(assistenza);
    }

    // Ottengo utente associato al ticket
    public Utente getUtenteByTicket(Long ticketId) {

        Assistenza assistenza = arepo.findById(ticketId).orElseThrow(() -> new RuntimeException("Ticket non trovato"));

        return assistenza.getUtente();
    }

}