package it.unife.sample.backend.service;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.unife.sample.backend.model.Assistenza;
import it.unife.sample.backend.model.Utente;
import it.unife.sample.backend.repository.AssistenzaRepository;
import it.unife.sample.backend.repository.UtenteRepository;
import it.unife.sample.backend.dto.request.AssistenzaRequestDTO;
import it.unife.sample.backend.dto.response.AssistenzaResponseDTO;

@Service
public class AssistenzaService {

    @Autowired
    private AssistenzaRepository aRepo;

    @Autowired
    private UtenteRepository uRepo;

    // Mapper interno Entity -> ResponseDTO
    private AssistenzaResponseDTO mapToDTO(Assistenza a) {
        AssistenzaResponseDTO dto = new AssistenzaResponseDTO();
        dto.setIdTicket(a.getIdTicket());
        dto.setOggetto(a.getOggetto());
        dto.setTipoAss(a.getTipoAss());
        dto.setStato(a.getStato());
        dto.setSoddisfazione(a.getSoddisfazione());
        dto.setContenuto(a.getContenuto());
        dto.setRisposta(a.getRisposta());
        if (a.getUtente() != null) {
            dto.setUtenteCf(a.getUtente().getCf());
        }
        if (a.getAssistente() != null) {
            dto.setAssistenteCf(a.getAssistente().getCf());
        }
        return dto;
    }

    // CRUD base
    public List<AssistenzaResponseDTO> findAll() {
        return aRepo.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // CRUD base
    public Optional<AssistenzaResponseDTO> findById(Long id) {
        return aRepo.findById(id).map(this::mapToDTO);
    }

    // Funzionalità: Recupera i dati filtrando per Stato
    // Usata in: AssistenzaController.getByStato
    // Frontend: Area Personale (Assistenza)
    public List<AssistenzaResponseDTO> findByStato(String stato) {
        return aRepo.findByStato(stato).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // Funzionalità: Recupera i dati filtrando per Utente
    // Usata in: AssistenzaController.getByUtente
    // Frontend: Area Personale (Assistenza) / Login / Utenti
    public List<AssistenzaResponseDTO> findByUtente(String cf) {
        return aRepo.findByUtenteCf(cf).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // Funzionalità: Recupera i dati filtrando per Assistente
    // Usata in: AssistenzaController.getByAssistente
    // Frontend: Area Personale (Assistenza)
    public List<AssistenzaResponseDTO> findByAssistente(String cf) {
        return aRepo.findByAssistenteCf(cf).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // OPERAZIONI DI BUSINESS SUI TICKET

    /**
     * 1. Apertura: L'utente apre il ticket (APERTO)
     */
    // Usata in: AssistenzaController.apriTicket
    // Frontend: Area Personale (Assistenza)
    @Transactional
    public AssistenzaResponseDTO apriTicket(AssistenzaRequestDTO dto) {
        Utente utente = uRepo.findById(dto.getUtenteCf())
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        Assistenza a = new Assistenza();
        a.setUtente(utente);
        a.setOggetto(dto.getOggetto());
        a.setTipoAss(dto.getTipoAss());
        a.setContenuto(dto.getContenuto());
        a.setStato("APERTO");
        a.setSoddisfazione(null);
        a.setAssistente(null);

        return mapToDTO(aRepo.save(a));
    }

    /**
     * 2. Presa in carico: Un admin/staff si prende in carico il ticket (IN LAVORAZIONE)
     */
    // Usata in: AssistenzaController.prendiInCarico
    // Frontend: Area Personale (Assistenza)
    @Transactional
    public AssistenzaResponseDTO prendiInCarico(Long idTicket, String assistenteCf) {
        Assistenza a = aRepo.findById(idTicket)
                .orElseThrow(() -> new IllegalArgumentException("Ticket non trovato"));

        Utente assistente = uRepo.findById(assistenteCf)
                .orElseThrow(() -> new IllegalArgumentException("Assistente non trovato"));

        if (!"APERTO".equals(a.getStato())) {
            throw new IllegalStateException("Solo i ticket APERTI possono essere presi in carico");
        }

        a.setAssistente(assistente);
        a.setStato("IN LAVORAZIONE");
        return mapToDTO(aRepo.save(a));
    }

    /**
     * 3. Risoluzione: L'assistente chiude l'intervento (RISOLTO)
     */
    // Usata in: AssistenzaController.risolviTicket
    // Frontend: Area Personale (Assistenza)
    @Transactional
    public AssistenzaResponseDTO risolviTicket(Long idTicket, String risposta) {
        Assistenza a = aRepo.findById(idTicket)
                .orElseThrow(() -> new IllegalArgumentException("Ticket non trovato"));

        if (!"IN LAVORAZIONE".equals(a.getStato())) {
            throw new IllegalStateException("Solo i ticket IN LAVORAZIONE possono essere risolti");
        }

        a.setStato("RISOLTO");
        a.setRisposta(risposta);
        return mapToDTO(aRepo.save(a));
    }

    /**
     * 4. Valutazione: L'utente valuta l'assistenza (CHIUSO)
     */
    // Usata in: AssistenzaController.risolviTicket
    // Frontend: Area Personale (Assistenza)
    @Transactional
    public AssistenzaResponseDTO valutaTicket(Long idTicket, Integer voto) {
        Assistenza a = aRepo.findById(idTicket)
                .orElseThrow(() -> new IllegalArgumentException("Ticket non trovato"));

        if (!"RISOLTO".equals(a.getStato())) {
            throw new IllegalStateException("Solo i ticket RISOLTI possono essere valutati");
        }
        if (voto == null || voto < 1 || voto > 5) {
            throw new IllegalArgumentException("Il voto deve essere compreso tra 1 e 5");
        }

        a.setSoddisfazione(voto);
        a.setStato("CHIUSO");
        return mapToDTO(aRepo.save(a));
    }
}
