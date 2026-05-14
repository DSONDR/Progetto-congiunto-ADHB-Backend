package it.unife.sample.backend.service;

import java.util.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.unife.sample.backend.model.Sottoscrizione;
import it.unife.sample.backend.model.Atleta;
import it.unife.sample.backend.model.Utente;
import it.unife.sample.backend.model.Abbonamento;
import it.unife.sample.backend.model.Pagamento;
import it.unife.sample.backend.model.SottoscrizioneId;
import it.unife.sample.backend.repository.SottoscrizioneRepository;
import it.unife.sample.backend.repository.PagamentoRepository;

@Service
public class SottoscrizioneService {
    @Autowired
    private SottoscrizioneRepository sottRepo;
    @Autowired
    private PagamentoRepository pagRepo;

    // CRUD base
    public List<Sottoscrizione> findAll() {
        return sottRepo.findAll();
    }

    public Optional<Sottoscrizione> findById(SottoscrizioneId id) {
        return sottRepo.findById(id);
    }

    public Sottoscrizione save(Sottoscrizione sottoscrizione) {
        return sottRepo.save(sottoscrizione);
    }

    public void deleteById(SottoscrizioneId id) {
        sottRepo.deleteById(id);
    }

    // Altre funzioni
    //Sottoscrizione
    @Transactional
    public Sottoscrizione sottoscrivi(Utente utente, Abbonamento abb, String metodo) {
        if (!(utente instanceof Atleta atleta)) {
            throw new IllegalArgumentException("Solo gli atleti possono sottoscrivere un abbonamento");
        }

        // 1. Pagamento
        Pagamento p = new Pagamento();
        p.setImporto(0.0); // Calcola il prezzo dell'abbonamento se disponibile
        p.setMetodoPag(metodo);
        p.setStatoPag("CONFERMATO");
        p = pagRepo.save(p);

        // 2. Sottoscrizione
        Sottoscrizione s = new Sottoscrizione();
        s.setAtleta(atleta);
        s.setAbbonamento(abb);
        s.setPagamento(p);

        return sottRepo.save(s);
    }

    // Verifica validità
    public boolean isValida(SottoscrizioneId id) {
        Sottoscrizione s = sottRepo.findById(id).orElseThrow(() -> new RuntimeException("Sottoscrizione non trovata"));
        if (s.getAbbonamento() == null) return false;
        return s.getAbbonamento().getStatoAbb() != null && s.getAbbonamento().getStatoAbb().equalsIgnoreCase("ATTIVO");
    }

    // Storico personale
    public List<Sottoscrizione> getStoricoUtente(String cf) {
        return sottRepo.findByAtletaCf(cf);
    }
}
