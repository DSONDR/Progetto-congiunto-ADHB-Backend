package it.unife.sample.backend.service;

import java.time.LocalDate;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.unife.sample.backend.model.Abbonamento;
import it.unife.sample.backend.repository.AbbonamentoRepository;
import it.unife.sample.backend.dto.response.TipoAbbonamentoDTO;

@Service
public class AbbonamentoService {
    // Richiami alle funzioni dei repository di:
    @Autowired
    private AbbonamentoRepository repo;

    // CRUD base
    public List<Abbonamento> findAll() {
        return repo.findAll();
    }

    // CRUD base
    public Optional<Abbonamento> findById(Long id) {
        return repo.findById(id);
    }

    // CRUD base
    public Abbonamento save(Abbonamento abbonamento) {
        return repo.save(abbonamento);
    }

    // CRUD base
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    // Recupera tutti gli abbonamenti di un atleta specifico
    // Usato da SottoscrizioneController.disdici nella visualizzazione abbonamenti
    // dell'atleta e in AbbonamentoController.getStoricoUtente
    // Frontend: Login / Utenti / Dashboard / Acquisto
    public List<Abbonamento> findByAtletaCf(String cf) {
        return repo.findByAtletaCf(cf);
    }

    // Task schedulato: ogni giorno a mezzanotte controlla gli abbonamenti TEMPO
    // con data di scadenza passata e li segna come SCADUTO automaticamente
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void aggiornaScaduti() {
        // 1. TEMPO: scaduti per data
        List<Abbonamento> scadutiPerData = repo.findByStatoAbbAndDataScadenzaBefore("ATTIVO", LocalDate.now());
        for (Abbonamento a : scadutiPerData) {
            a.setStatoAbb("SCADUTO");
            repo.save(a);
        }

        // 2. INGRESSI: esauriti per numero di accessi
        // Cerca abbonamenti ATTIVO senza dataScadenza (proxy per tipo INGRESSI)
        // e verifica ingressiEffettuati rispetto al maxIngressi del listino
        List<Abbonamento> candidatiIngressi = repo.findByStatoAbbAndDataScadenzaIsNull("ATTIVO");
        for (Abbonamento a : candidatiIngressi) {
            if (a.getIngressiEffettuati() == null)
                continue;
            getDettagliTipo(a.getTipoAbb()).ifPresent(tipo -> {
                if (tipo.getMaxIngressi() != null && a.getIngressiEffettuati() >= tipo.getMaxIngressi()) {
                    a.setStatoAbb("SCADUTO");
                    repo.save(a);
                }
            });
        }
    }

    // Metodo di Gestione tipi abbonamento - Crea una lista (finta)
    // di TipiAbbonamento tra cui scegliere al momento dell'acquisto
    // Usato nella funzionalità di sottoscrizione degli abbonamenti
    // tramite AbbonamentoController.getTipiAbbonamento
    // Frontend: Dashboard / Acquisto
    public List<TipoAbbonamentoDTO> getTipiAbbonamento() {
        return List.of(
                new TipoAbbonamentoDTO("Mensile", "TEMPO", 50.0, 1, null),
                new TipoAbbonamentoDTO("Trimestrale", "TEMPO", 135.0, 3, null),
                new TipoAbbonamentoDTO("Annuale", "TEMPO", 450.0, 12, null),
                new TipoAbbonamentoDTO("10 Ingressi", "INGRESSI", 70.0, null, 10),
                new TipoAbbonamentoDTO("20 Ingressi", "INGRESSI", 130.0, null, 20),
                new TipoAbbonamentoDTO("30 Ingressi", "INGRESSI", 180.0, null, 30));
    }

    // Metodo per ritrovare i dettagli di un tipo di abbonamento tramite il suo
    // nomeDto
    // Usato nella funzionalità di sottoscrizione degli abbonamenti (.rinnova e .sottoscrivi)
    // o l'uso dell'abbonamento nell'iscrizione (IscrizioneService.iscriviConAbbonamento)
    // Frontend: Iscrizione Evento / Dashboard / Acquisto
    public Optional<TipoAbbonamentoDTO> getDettagliTipo(String nomeTipoDto) {
        return getTipiAbbonamento().stream()
                .filter(t -> t.getNome().equalsIgnoreCase(nomeTipoDto))
                .findFirst();
    }
}
