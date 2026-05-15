package it.unife.sample.backend.service;

import java.time.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.unife.sample.backend.model.Abbonamento;
import it.unife.sample.backend.model.Attivita;
import it.unife.sample.backend.model.Atleta;
import it.unife.sample.backend.model.DateAtt;
import it.unife.sample.backend.model.IscrSingolaId;
import it.unife.sample.backend.model.Iscrizione;
import it.unife.sample.backend.model.Pagamento;
import it.unife.sample.backend.model.Utente;
import it.unife.sample.backend.model.UsaAbb;
import it.unife.sample.backend.model.UsaAbbId;
import it.unife.sample.backend.repository.IscrizioneRepository;
import it.unife.sample.backend.repository.PagamentoRepository;
import it.unife.sample.backend.repository.UsaAbbRepository;
import it.unife.sample.backend.service.AbbonamentoService;
import it.unife.sample.backend.service.CertificatoMedicoService;
import it.unife.sample.backend.dto.response.TipoAbbonamentoDTO;

@Service
public class IscrizioneService {
    // Tutti i repository da cui pesca
    @Autowired
    private IscrizioneRepository iscRepo;
    @Autowired
    private PagamentoRepository pagRepo;
    @Autowired
    private UsaAbbRepository usaAbbRepo;
    @Autowired
    private AbbonamentoService abbonamentoService;
    @Autowired
    private CertificatoMedicoService certService;

    // CRUD base
    public List<Iscrizione> findAll() {
        return iscRepo.findAll();
    }

    public Optional<Iscrizione> findById(IscrSingolaId id) {
        return iscRepo.findById(id);
    }

    public Iscrizione save(Iscrizione iscrizione) {
        return iscRepo.save(iscrizione);
    }

    public void deleteById(IscrSingolaId id) {
        iscRepo.deleteById(id);
    }

    // Funzionalità di Iscrizione singola:
    // Esegue l'iscrizione singola a un'attività pagando la quota sul momento
    // Genera quindi un nuovo Pagamento e l'Iscrizione collegata all'Atleta e
    // all'Attività
    @Transactional
    public Iscrizione iscriviSingola(Atleta atleta, Attivita attivita, Double importo, String metodo) {

        // Controlli preliminari di idoneità:
        // Certificato medico valido
        // Destinatario compatibile
        // Posto disponibile
        if (!hasValidCertificato(atleta)) {
            throw new IllegalStateException("Certificato medico non valido o scaduto");
        }
        if (!isDestinatarioCompatibile(atleta, attivita)) {
            throw new IllegalStateException("Atleta non compatibile con il destinatario dell'attività");
        }
        if (!isPostoDisponibile(attivita)) {
            throw new IllegalStateException("Non ci sono posti disponibili nell'attività");
        }

        // Gestione (e effettivo) del pagamento
        Pagamento p = new Pagamento();
        p.setImporto(importo);
        p.setMetodoPag(metodo);
        p.setStatoPag("CONFERMATO");
        p.setDataPag(LocalDate.now());
        p = pagRepo.save(p);

        // Creazione dell'Iscrizione singola: collega Atleta, Attività e Pagamento
        Iscrizione i = new Iscrizione();
        i.setUtente(atleta);
        i.setAttivita(attivita);
        i.setPagamento(p);
        i.setDataIscr(LocalDate.now());
        i.setQrCode(UUID.randomUUID().toString());

        return iscRepo.save(i);
    }

    // Iscrizione con Abbonamento: di un Atleta a un'attività "scalandola" dal
    // totale se previsto nel tipo di Abbonamento
    // Esegue i controlli su stato, scadenza e capienza prima di concedere l'uso
    @Transactional
    public UsaAbb iscriviConAbbonamento(Atleta atleta, Attivita attivita, Abbonamento abbonamento) {
        // Controlli preliminari: Abbonamento presente, attivo e di appartenenza
        // all'Atleta
        if (abbonamento == null) {
            throw new IllegalArgumentException("Abbonamento obbligatorio");
        }
        if (abbonamento.getAtleta() == null || !abbonamento.getAtleta().getCf().equals(atleta.getCf())) {
            throw new IllegalArgumentException("L'abbonamento non appartiene all'atleta");
        }
        if (abbonamento.getStatoAbb() == null || !abbonamento.getStatoAbb().equalsIgnoreCase("ATTIVO")) {
            throw new IllegalStateException("Abbonamento non attivo");
        }
        // Controlli su certificato medico, destinatario compatibile e capienza
        if (!hasValidCertificato(atleta)) {
            throw new IllegalStateException("Certificato medico non valido o scaduto");
        }
        if (!isDestinatarioCompatibile(atleta, attivita)) {
            throw new IllegalStateException("Atleta non compatibile con il destinatario dell'attività");
        }
        if (!isPostoDisponibile(attivita)) {
            throw new IllegalStateException("Non ci sono posti disponibili nell'attività");
        }

        // Passati i primi controlli, si prosegue con l'uso dell'abbonamento
        // Definisce il tipo di abbonamento (Tempo o Ingressi)
        TipoAbbonamentoDTO tipo = abbonamentoService.getDettagliTipo(abbonamento.getTipoAbb())
                .orElseThrow(() -> new IllegalStateException("Tipo abbonamento sconosciuto"));

        // Se è un abbonamento TEMPO, controlla la scadenza temporale
        if ("TEMPO".equalsIgnoreCase(tipo.getTipo())) {
            if (abbonamento.getDataScadenza() == null || abbonamento.getDataScadenza().isBefore(LocalDate.now())) {
                abbonamento.setStatoAbb("SCADUTO");
                abbonamentoService.save(abbonamento);
                throw new IllegalStateException("Abbonamento scaduto temporalmente");
            }
            // Se l'abbonamento è ancora valido, crea l'uso
            // Se è un abbonamento a ingressi, controlla il numero di ingressi effettuati
        } else if ("INGRESSI".equalsIgnoreCase(tipo.getTipo())) {
            int ingressiEffettuati = abbonamento.getIngressiEffettuati() == null ? 0
                    : abbonamento.getIngressiEffettuati();
            if (tipo.getMaxIngressi() != null && ingressiEffettuati >= tipo.getMaxIngressi()) {
                abbonamento.setStatoAbb("SCADUTO");
                abbonamentoService.save(abbonamento);
                throw new IllegalStateException("Ingressi previsti dall'abbonamento esauriti");
            } // se non è esaurito, si incrementa il contatore
            abbonamento.setIngressiEffettuati(ingressiEffettuati + 1);
            // Se l'abbonamento ha un numero massimo di ingressi e questo è stato raggiunto,
            // lo imposta come scaduto
            if (tipo.getMaxIngressi() != null && abbonamento.getIngressiEffettuati() >= tipo.getMaxIngressi()) {
                abbonamento.setStatoAbb("SCADUTO");
            }
            abbonamentoService.save(abbonamento);
        }

        // Registra l'uso effettivo dell'abbonamento
        UsaAbb uso = new UsaAbb();
        uso.setAbbonamento(abbonamento);
        uso.setAttivita(attivita);
        uso.setUtente(atleta);
        uso.setDataUso(LocalDate.now());
        uso.setQrCode(UUID.randomUUID().toString());

        return usaAbbRepo.save(uso);
    }

    // Cancella un'iscrizione singola, se manca ancora il giusto preavviso
    // all'evento
    @Transactional
    public void cancellaIscrizione(IscrSingolaId idIscrizione) {
        Iscrizione isc = iscRepo.findById(idIscrizione)
                .orElseThrow(() -> new RuntimeException("Iscrizione non trovata"));
        LocalDateTime now = LocalDateTime.now();
        Optional<LocalDateTime> nextDate = isc.getAttivita().getDateAtts().stream()
                .map(DateAtt::getDate)
                .filter(d -> d.isAfter(now))
                .min(LocalDateTime::compareTo);
        // TODO: modifica qui se vuoi aggiungere o togliere il preavviso per la
        // cancellazione
        if (nextDate.isPresent() && nextDate.get().isBefore(now.plusHours(24))) {
            throw new RuntimeException("Troppo tardi per cancellarsi!");
        }

        iscRepo.delete(isc);
    }

    // Controlla se l'utente possiede almeno un certificato medico attualmente
    // valido
    // Usata sopra in iscriviSingola ed iscriviConAbbonamento
    public boolean hasValidCertificato(Utente utente) {
        LocalDate today = LocalDate.now();
        return certService.findByUtenteCf(utente.getCf()).stream()
                .anyMatch(c -> c.getDataScadenza() != null && !c.getDataScadenza().isBefore(today));
    }

    // Verifica la compatibilità d'età dell'atleta col tipo di attività
    // (Junior/Senior)
    // Usata sopra in iscriviSingola ed iscriviConAbbonamento
    public boolean isDestinatarioCompatibile(Utente utente, Attivita attivita) {
        String destinatario = attivita.getDestinatario();
        if (destinatario == null || destinatario.isBlank() || destinatario.equalsIgnoreCase("Tutti")) {
            return true;
        }
        int eta = Period.between(utente.getDataNascita(), LocalDate.now()).getYears();
        if (destinatario.equalsIgnoreCase("Junior")) {
            return eta <= 18;
        }
        if (destinatario.equalsIgnoreCase("Senior")) {
            return eta >= 18;
        }
        return true;
    }

    // Calcola i posti residui per l'attività, verificando iscrizioni singole e con
    // abbonamento.
    // Usata sopra in iscriviSingola ed iscriviConAbbonamento
    public boolean isPostoDisponibile(Attivita attivita) {
        if (attivita.getMaxPartecipanti() == null) {
            return true;
        }
        long postiOccupati = iscRepo.countByAttivitaCodiceAtt(attivita.getCodiceAtt())
                + usaAbbRepo.countByAttivitaCodiceAtt(attivita.getCodiceAtt());
        return postiOccupati < attivita.getMaxPartecipanti();
    }

    public List<Iscrizione> getStoricoUtente(String cf) {
        return iscRepo.findByUtenteCf(cf);
    }
}
