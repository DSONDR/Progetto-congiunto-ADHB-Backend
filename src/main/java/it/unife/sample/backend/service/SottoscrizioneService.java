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
import it.unife.sample.backend.repository.AtletaRepository;
import it.unife.sample.backend.repository.UsaAbbRepository;
import it.unife.sample.backend.model.UsaAbb;
import it.unife.sample.backend.dto.response.TipoAbbonamentoDTO;
import it.unife.sample.backend.dto.response.SottoscrizioneResponseDTO;
import it.unife.sample.backend.service.CertificatoMedicoService;

@Service
public class SottoscrizioneService {
    @Autowired
    private SottoscrizioneRepository sottRepo;
    @Autowired
    private PagamentoRepository pagRepo;
    @Autowired
    private AbbonamentoRepository abbRepo;
    @Autowired
    private AtletaRepository atletaRepo;
    @Autowired
    private AbbonamentoService abbonamentoService;
    @Autowired
    private CertificatoMedicoService certService;
    @Autowired
    private UsaAbbRepository usaAbbRepo;

    // CRUD base
    public List<Sottoscrizione> findAll() {
        return sottRepo.findAll();
    }

    // CRUD base
    public Optional<Sottoscrizione> findById(SottoscrizioneId id) {
        return sottRepo.findById(id);
    }

    // CRUD base
    public Sottoscrizione save(Sottoscrizione sottoscrizione) {
        return sottRepo.save(sottoscrizione);
    }

    // CRUD base, usato anche per la disdetta --> Decurta i punti se necessario
    @Transactional
    public Integer deleteByIdAndReturnPoints(SottoscrizioneId id) {
        Integer nuoviPunti = null;
        // Recupera la sottoscrizione per ottenere l'abbonamento
        Sottoscrizione s = sottRepo.findById(id).orElse(null);
        if (s != null) {
            Abbonamento a = s.getAbbonamento();
            if (a != null) {
                // Decurtazione punti Gamification
                TipoAbbonamentoDTO tipo = abbonamentoService.getDettagliTipo(a.getTipoAbb()).orElse(null);
                if (tipo != null && !"PUNTI".equalsIgnoreCase(s.getPagamento().getMetodoPag())) {
                    Atleta atleta = a.getAtleta();
                    int puntiDaTogliere = (int) Math.floor(tipo.getPrezzo());
                    int puntiAttuali = (atleta.getPuntiGamification() != null) ? atleta.getPuntiGamification() : 0;
                    nuoviPunti = Math.max(0, puntiAttuali - puntiDaTogliere);
                    atleta.setPuntiGamification(nuoviPunti);
                    atletaRepo.save(atleta);
                } else {
                    nuoviPunti = a.getAtleta().getPuntiGamification();
                }

                // 1. Sposta l'abbonamento nello storico impostandolo a CANCELLATO
                a.setStatoAbb("CANCELLATO");
                abbRepo.save(a);

                // 2. Cancella tutte le iscrizioni agli eventi effettuate con questo abbonamento
                List<UsaAbb> usi = usaAbbRepo.findByAbbonamentoNumeroAbb(a.getNumeroAbb());
                if (!usi.isEmpty()) {
                    usaAbbRepo.deleteAll(usi);
                }
            }
            // 3. Cancella la sottoscrizione (facoltativo, ma manteniamo la coerenza col
            // passato)
            sottRepo.deleteById(id);
        }
        return nuoviPunti;
    }

    // Manteniamo deleteById per retrocompatibilità (se usato da altri)
    @Transactional
    public void deleteById(SottoscrizioneId id) {
        deleteByIdAndReturnPoints(id);
    }

    // --- Altre funzionalità ---

    // Funzionalità Sottoscrizione: Sottoscrive un nuovo abbonamento per l'Atleta.
    // Crea contestualmente il Pagamento, l'Abbonamento con le relative scadenze,
    // e la Sottoscrizione vera e propria che fa da legame.
    // Il certificato medico viene verificato ma NON è bloccante per l'acquisto:
    // se mancante/scaduto, l'acquisto va a buon fine ma la risposta contiene un
    // avviso.
    // Il certificato resta BLOCCANTE nell'uso dell'abbonamento (IscrizioneService).
    @Transactional
    // Usata in: SottoscrizioneController.sottoscrivi
    public SottoscrizioneResponseDTO sottoscrivi(Atleta atleta, String tipoAbbonamento, String metodo) {

        // Verifica certificato medico (non bloccante)
        String avviso = null;
        boolean hasCertValido = certService.findByUtenteCf(atleta.getCf()).stream()
                .anyMatch(c -> c.getDataScadenza() != null && !c.getDataScadenza().isBefore(LocalDate.now()));
        if (!hasCertValido) {
            avviso = "Acquisto effettuato, ma non hai un certificato medico valido. "
                    + "Dovrai rinnovarlo prima di poter usare l'abbonamento per iscriverti alle attività.";
        }

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

        // 4. Gamification: +1 punto per 1 euro speso
        // Evitiamo di assegnare nuovi punti se l'utente sta acquistando l'abbonamento
        // spendendo i suoi punti pregressi (metodo = "PUNTI")
        if (!"PUNTI".equalsIgnoreCase(metodo)) {
            int puntiDaAggiungere = (int) Math.floor(tipo.getPrezzo());
            int puntiAttuali = (atleta.getPuntiGamification() != null) ? atleta.getPuntiGamification() : 0;
            atleta.setPuntiGamification(puntiAttuali + puntiDaAggiungere);
            atletaRepo.save(atleta);
        }

        return new SottoscrizioneResponseDTO(sottRepo.save(s), avviso);
    }

    // Recupera lo storico delle sottoscrizioni (acquisti) di un Atleta
    // Usato da SottoscrizioneController (GET /storicoUtente/{cf})
    // NB: ritorna Sottoscrizioni (acquisti), non Abbonamenti (stato corrente)
    // Usata in: SottoscrizioneController.getStoricoUtente
    // Frontend: Utenti
    public List<Sottoscrizione> getStoricoUtente(String cf) {
        return sottRepo.findByAtletaCf(cf);
    }

    // Funzionalità Rinnovo: rinnova un abbonamento SCADUTO o in scadenza per lo
    // stesso Atleta (SottoscrizioneController.rinnova)
    // Crea un nuovo Pagamento e aggiorna le date e lo stato dell'Abbonamento
    // esistente
    @Transactional
    public Abbonamento rinnova(Long numeroAbb, String metodo) {
        Abbonamento a = abbRepo.findById(numeroAbb)
                .orElseThrow(() -> new IllegalArgumentException("Abbonamento non trovato: " + numeroAbb));

        // Un abbonamento cancellato non può essere rinnovato
        if ("CANCELLATO".equalsIgnoreCase(a.getStatoAbb())) {
            throw new IllegalStateException("Impossibile rinnovare un abbonamento cancellato");
        }

        // Recupera il tipo originale dal listino per sapere prezzo e durata
        TipoAbbonamentoDTO tipo = abbonamentoService.getDettagliTipo(a.getTipoAbb())
                .orElseThrow(
                        () -> new IllegalArgumentException("Tipo abbonamento non più disponibile: " + a.getTipoAbb()));

        // Crea un nuovo pagamento per il rinnovo
        Pagamento p = new Pagamento();
        p.setImporto(tipo.getPrezzo());
        p.setMetodoPag(metodo);
        p.setStatoPag("CONFERMATO");
        p.setDataPag(LocalDate.now());
        p = pagRepo.save(p);

        // Aggiorna l'abbonamento: nuova data inizio, nuova scadenza, stato ATTIVO
        a.setPagamento(p);
        a.setDataInizio(LocalDate.now());
        a.setStatoAbb("ATTIVO");

        if ("TEMPO".equalsIgnoreCase(tipo.getTipo())) {
            a.setDataScadenza(LocalDate.now().plusMonths(tipo.getDurataMesi()));
        } else {
            // Abbonamento a ingressi: azzera gli ingressi effettuati e aggiunge i nuovi
            a.setIngressiEffettuati(0);
        }

        // Gamification: +1 punto per 1 euro speso
        int puntiDaAggiungere = (int) Math.floor(tipo.getPrezzo());
        Atleta atleta = a.getAtleta();
        int puntiAttuali = (atleta.getPuntiGamification() != null) ? atleta.getPuntiGamification() : 0;
        atleta.setPuntiGamification(puntiAttuali + puntiDaAggiungere);
        atletaRepo.save(atleta);

        return abbRepo.save(a);
    }
}
