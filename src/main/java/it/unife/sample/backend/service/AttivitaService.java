package it.unife.sample.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.unife.sample.backend.dto.request.AttivitaRequestDTO;
import it.unife.sample.backend.dto.response.AttivitaResponseDTO;
import it.unife.sample.backend.model.Attivita;
import it.unife.sample.backend.model.DateAtt;
import it.unife.sample.backend.model.DateAttId;
import it.unife.sample.backend.model.Impianto;
import it.unife.sample.backend.model.Istruttore;
import it.unife.sample.backend.repository.AttivitaRepository;
import it.unife.sample.backend.repository.DateAttRepository;
import it.unife.sample.backend.repository.IscrizioneRepository;
import it.unife.sample.backend.repository.UsaAbbRepository;
import it.unife.sample.backend.repository.SquadraRepository;
import it.unife.sample.backend.model.Squadra;
import it.unife.sample.backend.dto.response.SquadraResponseDTO;

@Service
public class AttivitaService {

    @Autowired
    private AttivitaRepository repo;

    @Autowired
    private DateAttRepository dateAttRepo;

    @Autowired
    private IscrizioneRepository iscRepo;

    @Autowired
    private UsaAbbRepository usaRepo;

    @Autowired
    private SquadraRepository squadraRepo;

    @Autowired
    private IstruttoreService istruttoreService;

    @Autowired
    private ImpiantoService impiantoService;

    // Metodi per la visualizzazione, pubblici per tutti i tipi di utente
    // CRUD base
    public List<Attivita> findAll() {
        return repo.findAll();
    }

    // CRUD base
    public Optional<Attivita> findById(Long id) {
        return repo.findById(id);
    }

    // CRUD base
    public List<AttivitaResponseDTO> findAllDTO() {
        return repo.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // CRUD base
    public Optional<AttivitaResponseDTO> findDtoById(Long id) {
        return repo.findById(id).map(this::mapToResponse);
    }

    // -- Metodi per la creazione e modifica delle attività, SOLO PER ISTRUTTORI --
    
    // CRUD base
    @Transactional
    public Attivita save(Attivita a) {
        return repo.save(a);
    }

    // CRUD base
    @Transactional
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    // Metodi usati per la modifica e la creazione, accessibili solo all'istruttore
    // Metodo che crea un'attività e la salva nel database
    // Usata in: AttivitaGestioneController.create
    // Frontend: Calendario / Eventi
    @Transactional
    public AttivitaResponseDTO create(AttivitaRequestDTO dto) {
        // Verifica che istruttore e impianto esistano
        Istruttore istruttore = istruttoreService.findById(dto.getIstruttoreCf())
                .orElseThrow(() -> new IllegalArgumentException("Istruttore non trovato: " + dto.getIstruttoreCf()));

        Impianto impianto = impiantoService.findById(dto.getImpiantoId())
                .orElseThrow(() -> new IllegalArgumentException("Impianto non trovato: " + dto.getImpiantoId()));

        if (dto.getDateOrari() == null || dto.getDateOrari().isEmpty()) {
            throw new IllegalStateException("Un'attività deve avere almeno una data o un orario programmato");
        }

        LocalDateTime now = LocalDateTime.now();
        java.util.Set<LocalDateTime> uniqueDates = new java.util.HashSet<>();
        for (LocalDateTime date : dto.getDateOrari()) {
            if (date.isBefore(now)) {
                throw new IllegalStateException("Non è possibile inserire orari nel passato: " + date);
            }
            if (!uniqueDates.add(date)) {
                throw new IllegalStateException("Orari duplicati rilevati: " + date);
            }
        }

        // Prevenzione sovrapposizioni: controlla se l'impianto è libero per la date in
        // cui si vuole creare l'attività
        if (dto.getDateOrari() != null && !dto.getDateOrari().isEmpty()) {
            boolean isOccupato = repo.existsByImpiantoAndDateOverlap(impianto.getId(), dto.getDateOrari());
            if (isOccupato) {
                throw new IllegalStateException("L'impianto selezionato è già occupato in una o più date indicate");
            }
        }

        // Se esistono mappa l'attività sul dto e la salva
        Attivita attivita = mapToEntity(dto, istruttore, impianto, new Attivita());
        Attivita saved = repo.save(attivita);
        return mapToResponse(saved);
    }

    // Metodo che aggiorna un'attività e la salva nel database
    // Usata in: AttivitaGestioneController.update
    // Frontend: Calendario / Eventi
    @Transactional
    public AttivitaResponseDTO update(Long id, AttivitaRequestDTO dto) {
        // Recupera attività esistente e aggiorna dati
        Attivita existing = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Attività non trovata: " + id));

        Istruttore istruttore = istruttoreService.findById(dto.getIstruttoreCf())
                .orElseThrow(() -> new IllegalArgumentException("Istruttore non trovato: " + dto.getIstruttoreCf()));

        Impianto impianto = impiantoService.findById(dto.getImpiantoId())
                .orElseThrow(() -> new IllegalArgumentException("Impianto non trovato: " + dto.getImpiantoId()));

        if (dto.getDateOrari() == null || dto.getDateOrari().isEmpty()) {
            throw new IllegalStateException("Un'attività deve avere almeno una data o un orario programmato");
        }

        java.util.Set<LocalDateTime> uniqueDates = new java.util.HashSet<>();
        for (LocalDateTime date : dto.getDateOrari()) {
            if (!uniqueDates.add(date)) {
                throw new IllegalStateException("Orari duplicati rilevati: " + date);
            }
        }

        // Prevenzione sovrapposizioni: controlla se l'impianto è libero escludendo
        // questa stessa attività
        if (dto.getDateOrari() != null && !dto.getDateOrari().isEmpty()) {
            boolean isOccupato = repo.existsByImpiantoAndDateOverlapExcluding(impianto.getId(), existing.getCodiceAtt(),
                    dto.getDateOrari());
            if (isOccupato) {
                throw new IllegalStateException("L'impianto selezionato è già occupato in una o più date indicate");
            }
        }

        Attivita attivita = mapToEntity(dto, istruttore, impianto, existing);
        Attivita saved = repo.save(attivita);
        return mapToResponse(saved);
    }

    // -- Metodi usati per la ricerca e il filtraggio, accessibili a TUTTI GLI UTENTI --
    
    // Metodo che restituisce un elenco di attività ordinate per data
    // Usata in: AttivitaVisualizzazioneController.getById
    // Frontend: Calendario / Eventi
    public List<AttivitaResponseDTO> getCalendario(LocalDateTime inizio, LocalDateTime fine) {
        return repo.findByDateAttsDateBetween(inizio, fine).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Ricerca avanzata con più parametri (incluso istruttore e squadra [perchè di vedano nelle attività])
    // Usata in: AttivitaVisualizzazioneController.getById
    // Frontend: Calendario / Eventi
    public List<AttivitaResponseDTO> filtra(Long idImpianto, Double prezzo, String target, String tipoEvento,
            String istruttoreCf, Long squadraId, LocalDateTime inizio, LocalDateTime fine) {
        return repo.findFiltered(idImpianto, prezzo, target, tipoEvento, istruttoreCf, squadraId, inizio, fine).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Funzionalità: Ottiene i dati di TipiEvento
    // Usata in: AttivitaVisualizzazioneController.getTipiEvento
    // Frontend: Calendario / Eventi
    public List<String> getTipiEvento() {
        return repo.findDistinctTipiEvento();
    }

    // Funzionalità: Ottiene i dati di Destinatari
    // Usata in: AttivitaVisualizzazioneController.getDestinatari
    // Frontend: Calendario / Eventi
    public List<String> getDestinatari() {
        return repo.findDistinctDestinatari();
    }

    // Funzionalità: Recupera i dati filtrando per IstruttoreCf
    // Usata in: IstruttoreController.getAttivita
    // Frontend: Calendario / Eventi
    public List<AttivitaResponseDTO> findByIstruttoreCf(String cfIstruttore) {
        return repo.findByIstruttoreCf(cfIstruttore).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Metodi per la gestione dei posti, usati da IscrizioneService per la validazione
    // Metodo che conta tutti gli iscritti a un'attività (da sommare agli iscritti con abbonamento)
    // Funzionalità: Ottiene i dati di NumeroIscritti
    // Usata in: AttivitaVisualizzazioneController.getNumeroIscritti
    // Frontend: Calendario / Eventi
    public int getNumeroIscritti(Long idAttivita) {
        return (int) (iscRepo.countByAttivitaCodiceAtt(idAttivita) + usaRepo.countByAttivitaCodiceAtt(idAttivita));
    }

    // Verifica se ci sono ancora posti disponibili
    // Usata in: AttivitaVisualizzazioneController.isPostoDisponibile
    // Frontend: Calendario / Eventi
    public boolean isPostoDisponibile(Long idAttivita) {
        Attivita a = repo.findById(idAttivita)
                .orElseThrow(() -> new IllegalArgumentException("Attività non trovata: " + idAttivita));
        return getNumeroIscritti(idAttivita) < a.getMaxPartecipanti();
    }

    // Metodo helper per convertire un DTO in un'entità Attivita
    // Usato in create() e update()
    private Attivita mapToEntity(AttivitaRequestDTO dto, Istruttore istruttore, Impianto impianto, Attivita attivita) {
        if (dto.getCodiceAtt() != null) {
            attivita.setCodiceAtt(dto.getCodiceAtt());
        }
        attivita.setNomeAtt(dto.getNomeAtt());
        attivita.setTipoEvento(dto.getTipoEvento());
        attivita.setDestinatario(dto.getDestinatario());
        attivita.setQuotaBase(dto.getQuotaBase());
        attivita.setMaxPartecipanti(dto.getMaxPartecipanti());
        attivita.setDescrizione(dto.getDescrizione());
        attivita.setIstruttore(istruttore);
        attivita.setImpianto(impianto);

        // Gestione delle squadre aderenti
        attivita.getSquadreAderenti().clear();
        if (dto.getSquadreIds() != null && !dto.getSquadreIds().isEmpty()) {
            List<Squadra> squadre = squadraRepo.findAllById(dto.getSquadreIds());
            attivita.getSquadreAderenti().addAll(squadre);
        }

        attivita.getDateAtts().clear();
        if (dto.getDateOrari() != null) {
            for (LocalDateTime date : dto.getDateOrari()) {
                DateAtt dateAtt = new DateAtt(date, attivita);
                attivita.getDateAtts().add(dateAtt);
            }
        }

        return attivita;
    }

    // Metodo per convertire un'entità Attivita in un DTO
    // Usato in findAllDTO() e findDtoById()
    private AttivitaResponseDTO mapToResponse(Attivita attivita) {
        AttivitaResponseDTO dto = new AttivitaResponseDTO();
        dto.setCodiceAtt(attivita.getCodiceAtt());
        dto.setNomeAtt(attivita.getNomeAtt());
        dto.setTipoEvento(attivita.getTipoEvento());
        dto.setDestinatario(attivita.getDestinatario());
        dto.setQuotaBase(attivita.getQuotaBase());
        dto.setMaxPartecipanti(attivita.getMaxPartecipanti());
        dto.setDescrizione(attivita.getDescrizione());
        dto.setIscritti(getNumeroIscritti(attivita.getCodiceAtt()));

        if (attivita.getIstruttore() != null) {
            dto.setIstruttoreCf(attivita.getIstruttore().getCf());
            dto.setNomeIstruttore(attivita.getIstruttore().getNome() + " " + attivita.getIstruttore().getCognome());
        }

        if (attivita.getImpianto() != null) {
            dto.setImpiantoId(attivita.getImpianto().getId());
            dto.setImpiantoNome(attivita.getImpianto().getNome());
        }

        dto.setDateOrari(attivita.getDateAtts().stream()
                .map(DateAtt::getDate)
                .sorted()
                .collect(Collectors.toList()));

        if (attivita.getSquadreAderenti() != null) {
            dto.setSquadreAderenti(attivita.getSquadreAderenti().stream().map(s -> {
                SquadraResponseDTO sDto = new SquadraResponseDTO();
                sDto.setId(s.getId());
                sDto.setNome(s.getNome());
                sDto.setSport(s.getSport());
                sDto.setCampionato(s.getCampionato());
                if (s.getAllenatore() != null) {
                    sDto.setAllenatoreCf(s.getAllenatore().getCf());
                    sDto.setNomeAllenatore(s.getAllenatore().getNome() + " " + s.getAllenatore().getCognome());
                }
                return sDto;
            }).collect(Collectors.toList()));
        }

        return dto;
    }

    // Controlla che l'impianto non sia già occupato in quella data prima di
    // aggiungerla
    // Usata in: AttivitaGestioneController.delete
    // Frontend: Calendario / Eventi
    @Transactional
    public AttivitaResponseDTO addSessione(Long idAttivita, LocalDateTime data) {
        Attivita attivita = repo.findById(idAttivita)
                .orElseThrow(() -> new IllegalArgumentException("Attività non trovata: " + idAttivita));

        // Controlla sovrapposizione impianto per la singola data
        boolean isOccupato = repo.existsByImpiantoAndDateOverlapExcluding(
                attivita.getImpianto().getId(), idAttivita, List.of(data));
        if (isOccupato) {
            throw new IllegalStateException(
                    "L'impianto è già occupato in questa data: " + data);
        }

        // Aggiunge la nuova sessione alla lista (cascade salva automaticamente)
        DateAtt nuovaSessione = new DateAtt(data, attivita);
        attivita.getDateAtts().add(nuovaSessione);
        Attivita saved = repo.save(attivita);
        return mapToResponse(saved);
    }

    // Lancia eccezione se la data non è presente nell'attività
    // Usata in: AttivitaGestioneController.delete
    // Frontend: Calendario / Eventi
    @Transactional
    public AttivitaResponseDTO removeSessione(Long idAttivita, LocalDateTime data) {
        Attivita attivita = repo.findById(idAttivita)
                .orElseThrow(() -> new IllegalArgumentException("Attività non trovata: " + idAttivita));

        DateAttId dateAttId = new DateAttId(data, idAttivita);
        if (!dateAttRepo.existsById(dateAttId)) {
            throw new IllegalArgumentException("Sessione non trovata per la data: " + data);
        }

        // Rimuove dalla lista (orphanRemoval=true su Attivita cancella automaticamente
        // da DB)
        attivita.getDateAtts().removeIf(d -> d.getDate().equals(data));
        Attivita saved = repo.save(attivita);
        return mapToResponse(saved);
    }
}
