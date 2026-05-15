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
import it.unife.sample.backend.repository.AbbonamentoRepository;
import it.unife.sample.backend.dto.response.TipoAbbonamentoDTO;

@Service
public class SottoscrizioneService {
    @Autowired
    private SottoscrizioneRepository sottRepo;
    @Autowired
    private PagamentoRepository pagRepo;
    @Autowired
    private AbbonamentoRepository abbRepo;
    @Autowired
    private AbbonamentoService abbonamentoService;

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

    // Altre funzioni del Service
    // Funzionalità Sottoscrizione
    // Sottoscrive un nuovo abbonamento per l'Atleta
    // Crea contestualmente il Pagamento, l'Abbonamento con le relative scadenze,
    // e la Sottoscrizione vera e propria che fa da legame.
    @Transactional
    public Sottoscrizione sottoscrivi(Atleta atleta, String tipoAbbonamento, String metodo) {

        TipoAbbonamentoDTO tipo = abbonamentoService.getDettagliTipo(tipoAbbonamento)
                .orElseThrow(() -> new IllegalArgumentException("Tipo di abbonamento non valido"));

        // 1. Pagamento
        Pagamento p = new Pagamento();
        p.setImporto(tipo.getPrezzo());
        p.setMetodoPag(metodo);
        p.setStatoPag("CONFERMATO");
        p.setDataPag(LocalDate.now());
        p = pagRepo.save(p);

        // 2. Abbonamento
        Abbonamento a = new Abbonamento();
        a.setAtleta(atleta);
        a.setPagamento(p);
        a.setTipoAbb(tipo.getNome());
        a.setDataInizio(LocalDate.now());
        a.setStatoAbb("ATTIVO");
        // Se l'abbonamento è a tempo, calcola la scadenza
        if ("TEMPO".equalsIgnoreCase(tipo.getTipo())) {
            a.setDataScadenza(LocalDate.now().plusMonths(tipo.getDurataMesi()));
            a.setIngressiEffettuati(null);
        } else {
            // Se l'abbonamento è a scalare, non ha scadenza
            a.setDataScadenza(null);
            a.setIngressiEffettuati(0);
        }
        // Salva l'abbonamento
        a = abbRepo.save(a);

        // 3. Sottoscrizione
        Sottoscrizione s = new Sottoscrizione();
        s.setAtleta(atleta);
        s.setAbbonamento(a);
        s.setPagamento(p);

        return sottRepo.save(s);
    }

    // Recupera lo storico delle sottoscrizioni di un Atleta
    // TODO, da usare ancora
    public List<Sottoscrizione> getStoricoUtente(String cf) {
        return sottRepo.findByAtletaCf(cf);
    }
}
