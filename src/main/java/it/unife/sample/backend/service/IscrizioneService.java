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

@Service
public class IscrizioneService {
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

    @Transactional
    public Iscrizione iscriviSingola(Utente utente, Attivita attivita, Double importo, String metodo) {
        if (!(utente instanceof Atleta)) {
            throw new IllegalArgumentException("Solo un atleta può iscriversi a un'attività");
        }
        if (!hasValidCertificato(utente)) {
            throw new IllegalStateException("Certificato medico non valido o scaduto");
        }
        if (!isDestinatarioCompatibile(utente, attivita)) {
            throw new IllegalStateException("Atleta non compatibile con il destinatario dell'attività");
        }
        if (!isPostoDisponibile(attivita)) {
            throw new IllegalStateException("Non ci sono posti disponibili nell'attività");
        }

        Pagamento p = new Pagamento();
        p.setImporto(importo);
        p.setMetodoPag(metodo);
        p.setStatoPag("CONFERMATO");
        p.setDataPag(LocalDate.now());
        p = pagRepo.save(p);

        Iscrizione i = new Iscrizione();
        i.setUtente(utente);
        i.setAttivita(attivita);
        i.setPagamento(p);
        i.setDataIscr(LocalDate.now());
        i.setQrCode(UUID.randomUUID().toString());

        return iscRepo.save(i);
    }

    @Transactional
    public UsaAbb iscriviConAbbonamento(Utente utente, Attivita attivita, Abbonamento abbonamento) {
        if (!(utente instanceof Atleta)) {
            throw new IllegalArgumentException("Solo un atleta può usare un abbonamento");
        }
        if (abbonamento == null) {
            throw new IllegalArgumentException("Abbonamento obbligatorio");
        }
        if (abbonamento.getAtleta() == null || !abbonamento.getAtleta().getCf().equals(utente.getCf())) {
            throw new IllegalArgumentException("L'abbonamento non appartiene all'atleta");
        }
        if (abbonamento.getStatoAbb() == null || !abbonamento.getStatoAbb().equalsIgnoreCase("ATTIVO")) {
            throw new IllegalStateException("Abbonamento non attivo");
        }
        if (!hasValidCertificato(utente)) {
            throw new IllegalStateException("Certificato medico non valido o scaduto");
        }
        if (!isDestinatarioCompatibile(utente, attivita)) {
            throw new IllegalStateException("Atleta non compatibile con il destinatario dell'attività");
        }
        if (!isPostoDisponibile(attivita)) {
            throw new IllegalStateException("Non ci sono posti disponibili nell'attività");
        }

        if (abbonamento.getTipoAbb() != null && abbonamento.getTipoAbb().equalsIgnoreCase("INGRESSI")) {
            if (abbonamento.getIngressiEffettuati() == null) {
                abbonamento.setIngressiEffettuati(0);
            }
            abbonamento.setIngressiEffettuati(abbonamento.getIngressiEffettuati() + 1);
            abbonamentoService.save(abbonamento);
        }

        UsaAbb uso = new UsaAbb();
        uso.setAbbonamento(abbonamento);
        uso.setAttivita(attivita);
        uso.setUtente(utente);
        uso.setDataUso(LocalDate.now());
        uso.setQrCode(UUID.randomUUID().toString());

        return usaAbbRepo.save(uso);
    }

    @Transactional
    public void cancellaIscrizione(IscrSingolaId idIscrizione) {
        Iscrizione isc = iscRepo.findById(idIscrizione).orElseThrow(() -> new RuntimeException("Iscrizione non trovata"));
        LocalDateTime now = LocalDateTime.now();
        Optional<LocalDateTime> nextDate = isc.getAttivita().getDateAtts().stream()
                .map(DateAtt::getDate)
                .filter(d -> d.isAfter(now))
                .min(LocalDateTime::compareTo);

        if (nextDate.isPresent() && nextDate.get().isBefore(now.plusHours(24))) {
            throw new RuntimeException("Troppo tardi per cancellarsi!");
        }

        iscRepo.delete(isc);
    }

    public boolean hasValidCertificato(Utente utente) {
        LocalDate today = LocalDate.now();
        return certService.findByUtenteCf(utente.getCf()).stream()
                .anyMatch(c -> !c.getDataScadenza().isBefore(today));
    }

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
