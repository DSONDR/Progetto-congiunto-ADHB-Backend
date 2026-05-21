package it.unife.sample.backend.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.stream.Collectors;

import it.unife.sample.backend.model.Squadra;
import it.unife.sample.backend.model.Atleta;
import it.unife.sample.backend.model.Allenatore;
import it.unife.sample.backend.repository.SquadraRepository;
import it.unife.sample.backend.repository.AtletaRepository;
import it.unife.sample.backend.repository.UtenteRepository;
import it.unife.sample.backend.repository.AllenatoreRepository;
import it.unife.sample.backend.dto.request.SquadraRequestDTO;
import it.unife.sample.backend.dto.response.SquadraResponseDTO;
import it.unife.sample.backend.dto.response.UserResponseDTO;

@Service
public class SquadraService {
    
    @Autowired
    private SquadraRepository sqRepo;
    
    @Autowired
    private AtletaRepository atlRepo;

    @Autowired
    private AllenatoreRepository allenatoreRepo;

    // Mapper interno per SquadraResponseDTO
    private SquadraResponseDTO mapToDTO(Squadra sq) {
        SquadraResponseDTO dto = new SquadraResponseDTO();
        dto.setId(sq.getId());
        dto.setNome(sq.getNome());
        dto.setSport(sq.getSport());
        dto.setCampionato(sq.getCampionato());
        if (sq.getAllenatore() != null) {
            dto.setAllenatoreCf(sq.getAllenatore().getCf());
            dto.setNomeAllenatore(sq.getAllenatore().getNome() + " " + sq.getAllenatore().getCognome());
        }
        return dto;
    }

    // Mapper interno per UserResponseDTO (utilizzato per elencare gli atleti nel roster)
    private UserResponseDTO mapAtletaToUserDTO(Atleta atleta) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setCf(atleta.getCf());
        dto.setNome(atleta.getNome());
        dto.setCognome(atleta.getCognome());
        dto.setGenere(atleta.getGenere());
        dto.setDataNascita(atleta.getDataNascita());
        dto.setCittaResidenza(atleta.getCittaResidenza());
        dto.setUsername(atleta.getUsername());
        dto.setEmail(atleta.getEmail());
        dto.setTipoIscritto(atleta.getTipoIscritto());
        return dto;
    }

    // --- LETTURA SQUADRE ---
    
    public List<SquadraResponseDTO> findAll() {
        return sqRepo.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Optional<SquadraResponseDTO> findById(Long id) {
        return sqRepo.findById(id).map(this::mapToDTO);
    }

    public List<SquadraResponseDTO> findByAllenatoreCf(String cf) {
        return sqRepo.findByAllenatoreCf(cf).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<SquadraResponseDTO> findByAtletaCf(String cf) {
        return sqRepo.findByAtletaCf(cf).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // --- GESTIONE SQUADRA ---

    @Transactional
    public SquadraResponseDTO creaSquadra(SquadraRequestDTO dto) {
        Allenatore allenatore = allenatoreRepo.findById(dto.getAllenatoreCf())
                .orElseThrow(() -> new IllegalArgumentException("Allenatore non trovato"));
                
        Squadra sq = new Squadra();
        sq.setNome(dto.getNome());
        sq.setSport(dto.getSport());
        sq.setCampionato(dto.getCampionato());
        sq.setAllenatore(allenatore);
        
        return mapToDTO(sqRepo.save(sq));
    }

    @Transactional
    public SquadraResponseDTO modificaSquadra(Long id, SquadraRequestDTO dto) {
        Squadra sq = sqRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Squadra non trovata"));
        
        Allenatore allenatore = allenatoreRepo.findById(dto.getAllenatoreCf())
                .orElseThrow(() -> new IllegalArgumentException("Allenatore non trovato"));

        sq.setNome(dto.getNome());
        sq.setSport(dto.getSport());
        sq.setCampionato(dto.getCampionato());
        sq.setAllenatore(allenatore);

        return mapToDTO(sqRepo.save(sq));
    }

    @Transactional
    public void deleteById(Long id) {
        sqRepo.deleteById(id);
    }

    // --- GESTIONE ROSTER ATLETI ---

    @Transactional
    public void aggiungiAtletaASquadra(Long squadraId, String atletaCf) {
        Squadra squadra = sqRepo.findById(squadraId).orElseThrow(() -> new RuntimeException("Squadra non trovata"));
        Atleta atleta = atlRepo.findById(atletaCf).orElseThrow(() -> new RuntimeException("Atleta non trovato"));

        if (!squadra.getAtleti().contains(atleta)) {
            squadra.getAtleti().add(atleta);
            sqRepo.save(squadra);
        }
    }

    @Transactional
    public void rimuoviAtletaDaSquadra(Long squadraId, String atletaCf) {
        Squadra squadra = sqRepo.findById(squadraId).orElseThrow(() -> new RuntimeException("Squadra non trovata"));
        Atleta atleta = atlRepo.findById(atletaCf).orElseThrow(() -> new RuntimeException("Atleta non trovato"));

        if (squadra.getAtleti().contains(atleta)) {
            squadra.getAtleti().remove(atleta);
            sqRepo.save(squadra);
        }
    }

    public List<UserResponseDTO> getAtletiBySquadra(Long squadraId) {
        return sqRepo.findById(squadraId)
                .map(Squadra::getAtleti)
                .orElse(new ArrayList<>())
                .stream().map(this::mapAtletaToUserDTO).collect(Collectors.toList());
    }

    public List<UserResponseDTO> getAtletiScadutiBySquadra(Long squadraId) {
        Squadra squadra = sqRepo.findById(squadraId)
                .orElseThrow(() -> new RuntimeException("Squadra non trovata"));

        return squadra.getAtleti().stream()
                .filter(atleta -> atleta.getCertificatiMedici() == null || atleta.getCertificatiMedici().isEmpty() ||
                        atleta.getCertificatiMedici().stream()
                                .noneMatch(cert -> cert.getDataScadenza() != null
                                        && !cert.getDataScadenza().isBefore(LocalDate.now())))
                .map(this::mapAtletaToUserDTO)
                .collect(Collectors.toList());
    }
}
