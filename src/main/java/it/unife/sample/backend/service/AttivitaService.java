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
import it.unife.sample.backend.model.Impianto;
import it.unife.sample.backend.model.Istruttore;
import it.unife.sample.backend.repository.AttivitaRepository;
import it.unife.sample.backend.repository.IscrizioneRepository;
import it.unife.sample.backend.repository.UsaAbbRepository;

@Service
public class AttivitaService {

    @Autowired
    private AttivitaRepository repo;

    @Autowired
    private IscrizioneRepository iscRepo;

    @Autowired
    private UsaAbbRepository usaRepo;

    @Autowired
    private IstruttoreService istruttoreService;

    @Autowired
    private ImpiantoService impiantoService;

    // Metodi per la visualizzazione, pubblici per tutti i tipi di utente
    public List<Attivita> findAll() {
        return repo.findAll();
    }

    public Optional<Attivita> findById(Long id) {
        return repo.findById(id);
    }

    public List<AttivitaResponseDTO> findAllDTO() {
        return repo.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public Optional<AttivitaResponseDTO> findDtoById(Long id) {
        return repo.findById(id).map(this::mapToResponse);
    }

    // Metodi per la creazione e modifica delle attività, solo per istruttori
    @Transactional
    public Attivita save(Attivita a) {
        return repo.save(a);
    }

    @Transactional
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    // Metodi usati per la modifica e la creazione, accessibili solo all'istruttore
    // Metodo che crea un'attività e la salva nel database
    @Transactional
    public AttivitaResponseDTO create(AttivitaRequestDTO dto) {
        // Verifica che istruttore e impianto esistano
        Istruttore istruttore = istruttoreService.findById(dto.getIstruttoreCf())
                .orElseThrow(() -> new IllegalArgumentException("Istruttore non trovato: " + dto.getIstruttoreCf()));

        Impianto impianto = impiantoService.findById(dto.getImpiantoId())
                .orElseThrow(() -> new IllegalArgumentException("Impianto non trovato: " + dto.getImpiantoId()));

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
    @Transactional
    public AttivitaResponseDTO update(Long id, AttivitaRequestDTO dto) {
        // Recupera attività esistente e aggiorna dati
        Attivita existing = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Attività non trovata: " + id));

        Istruttore istruttore = istruttoreService.findById(dto.getIstruttoreCf())
                .orElseThrow(() -> new IllegalArgumentException("Istruttore non trovato: " + dto.getIstruttoreCf()));

        Impianto impianto = impiantoService.findById(dto.getImpiantoId())
                .orElseThrow(() -> new IllegalArgumentException("Impianto non trovato: " + dto.getImpiantoId()));

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

    // Metodi usati per la ricerca e il filtraggio, accessibili a tutti gli utenti
    // Metodo che restituisce un elenco di attività ordinate per data
    public List<AttivitaResponseDTO> getCalendario(LocalDateTime inizio, LocalDateTime fine) {
        return repo.findByDateAttsDateBetween(inizio, fine).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Ricerca avanzata con più parametri
    public List<AttivitaResponseDTO> filtra(Long idImpianto, Double prezzo, String target, String tipoEvento,
            LocalDateTime inizio, LocalDateTime fine) {
        return repo.findFiltered(idImpianto, prezzo, target, tipoEvento, inizio, fine).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<AttivitaResponseDTO> findByIstruttoreCf(String cfIstruttore) {
        return repo.findByIstruttoreCf(cfIstruttore).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Metodi per la gestione dei posti, usati da IscrizioneService per la
    // validazione
    // Metodo che conta tutti gli iscritti a un'attività
    // (da sommare agli iscritti con abbonamento)
    public int getNumeroIscritti(Long idAttivita) {
        return (int) (iscRepo.countByAttivitaCodiceAtt(idAttivita) + usaRepo.countByAttivitaCodiceAtt(idAttivita));
    }

    // Verifica se ci sono ancora posti disponibili
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
        attivita.setIstruttore(istruttore);
        attivita.setImpianto(impianto);

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

        if (attivita.getIstruttore() != null) {
            dto.setIstruttoreCf(attivita.getIstruttore().getCf());
        }

        if (attivita.getImpianto() != null) {
            dto.setImpiantoId(attivita.getImpianto().getId());
            dto.setImpiantoNome(attivita.getImpianto().getNome_i());
        }

        dto.setDateOrari(attivita.getDateAtts().stream()
                .map(DateAtt::getDate)
                .sorted()
                .collect(Collectors.toList()));

        return dto;
    }
}
