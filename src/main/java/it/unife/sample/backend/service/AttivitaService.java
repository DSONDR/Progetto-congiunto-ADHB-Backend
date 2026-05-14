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

    //VISUALIZZAZIONE [Pubblici - lettura attività]
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

    //CREAZIONE E MODIFICA [Solo istruttori]
    @Transactional
    public Attivita save(Attivita a) {
        return repo.save(a);
    }

    @Transactional
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    @Transactional
    public AttivitaResponseDTO create(AttivitaRequestDTO dto) {
        //Verifica che istruttore e impianto esistano
        Istruttore istruttore = istruttoreService.findById(dto.getIstruttoreCf())
                .orElseThrow(() -> new IllegalArgumentException("Istruttore non trovato: " + dto.getIstruttoreCf()));

        Impianto impianto = impiantoService.findById(dto.getImpiantoId())
                .orElseThrow(() -> new IllegalArgumentException("Impianto non trovato: " + dto.getImpiantoId()));

        Attivita attivita = mapToEntity(dto, istruttore, impianto, new Attivita());
        Attivita saved = repo.save(attivita);
        return mapToResponse(saved);
    }

    @Transactional
    public AttivitaResponseDTO update(Long id, AttivitaRequestDTO dto) {
        //Recupera attività esistente e aggiorna dati
        Attivita existing = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Attività non trovata: " + id));

        Istruttore istruttore = istruttoreService.findById(dto.getIstruttoreCf())
                .orElseThrow(() -> new IllegalArgumentException("Istruttore non trovato: " + dto.getIstruttoreCf()));

        Impianto impianto = impiantoService.findById(dto.getImpiantoId())
                .orElseThrow(() -> new IllegalArgumentException("Impianto non trovato: " + dto.getImpiantoId()));

        Attivita attivita = mapToEntity(dto, istruttore, impianto, existing);
        Attivita saved = repo.save(attivita);
        return mapToResponse(saved);
    }

    //RICERCA E FILTRAGGIO [Pubblici - lettura attività]
    public List<AttivitaResponseDTO> getCalendario(LocalDateTime inizio, LocalDateTime fine) {
        return repo.findByDateAttsDateBetween(inizio, fine).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // --- FILTRI ---
    //Ricerca avanzata con più parametri
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

    //GESTIONE POSTI [Usato da IscrizioneService per validazione]
    //Conta tutti gli iscritti (singoli + abbonamento)
    public int getNumeroIscritti(Long idAttivita) {
        return (int) (iscRepo.countByAttivitaCodiceAtt(idAttivita) + usaRepo.countByAttivitaCodiceAtt(idAttivita));
    }

    //Verifica se ci sono ancora posti disponibili
    public boolean isPostoDisponibile(Long idAttivita) {
        Attivita a = repo.findById(idAttivita).orElseThrow(() -> new IllegalArgumentException("Attività non trovata: " + idAttivita));
        return getNumeroIscritti(idAttivita) < a.getMaxPartecipanti();
    }

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
