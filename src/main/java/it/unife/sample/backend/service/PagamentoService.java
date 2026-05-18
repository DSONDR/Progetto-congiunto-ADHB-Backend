package it.unife.sample.backend.service;

import java.util.*;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.unife.sample.backend.model.Pagamento;
import it.unife.sample.backend.model.Iscrizione;
import it.unife.sample.backend.model.Sottoscrizione;
import it.unife.sample.backend.repository.PagamentoRepository;
import it.unife.sample.backend.repository.IscrizioneRepository;
import it.unife.sample.backend.repository.SottoscrizioneRepository;
import it.unife.sample.backend.dto.response.PagamentoResponseDTO;

@Service
public class PagamentoService {
    @Autowired
    private PagamentoRepository repo;
    
    @Autowired
    private IscrizioneRepository iscRepo;
    
    @Autowired
    private SottoscrizioneRepository sottRepo;

    /**
     * Mapper interno per convertire l'entità Pagamento nel DTO di risposta.
     * La causale è opzionale e può essere impostata dopo il mapping di base.
     */
    private PagamentoResponseDTO mapToDTO(Pagamento p) {
        PagamentoResponseDTO dto = new PagamentoResponseDTO();
        dto.setIdPagamento(p.getIdPagamento());
        dto.setMetodoPag(p.getMetodoPag());
        dto.setDataPag(p.getDataPag());
        dto.setStatoPag(p.getStatoPag());
        dto.setImporto(p.getImporto());
        dto.setFattura(p.getFattura());
        return dto;
    }

    // CRUD base (solo lettura per i Controller)
    public List<PagamentoResponseDTO> findAll() {
        return repo.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<PagamentoResponseDTO> findById(Long id) {
        return repo.findById(id).map(this::mapToDTO);
    }

    // Metodi di utilità interna (non esposti come DTO nel controller)
    public Pagamento save(Pagamento pagamento) {
        return repo.save(pagamento);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    // Ricerca avanzata per data e/o importo
    public List<PagamentoResponseDTO> ricercaAvanzata(LocalDateTime da, LocalDateTime a, Double min, Double max) {
        List<Pagamento> risultati;
        if (da != null && a != null) {
            risultati = repo.findByDataPagBetween(da, a);
        } else if (min != null && max != null) {
            risultati = repo.findByImportoBetween(min, max);
        } else {
            risultati = repo.findAll();
        }
        return risultati.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // Filtro per Attività (recupera i pagamenti associati alle iscrizioni di un'attività)
    public List<PagamentoResponseDTO> getPagamentiPerAttivita(Long idAttivita) {
        return iscRepo.findByAttivitaCodiceAtt(idAttivita).stream()
                .map(Iscrizione::getPagamento)
                .map(p -> {
                    PagamentoResponseDTO dto = mapToDTO(p);
                    dto.setCausale("Iscrizione Singola Attività ID " + idAttivita);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // Storico transazioni di un utente (unisce Iscrizioni e Sottoscrizioni)
    public List<PagamentoResponseDTO> getStoricoTransazioni(String cf) {
        List<PagamentoResponseDTO> storico = new ArrayList<>();

        // Pagamenti derivanti da iscrizioni singole
        iscRepo.findByUtenteCf(cf).forEach(iscrizione -> {
            PagamentoResponseDTO dto = mapToDTO(iscrizione.getPagamento());
            dto.setCausale("Iscrizione Attività: " + iscrizione.getAttivita().getNomeAttivita());
            storico.add(dto);
        });

        // Pagamenti derivanti da abbonamenti
        sottRepo.findByAtletaCf(cf).forEach(sottoscrizione -> {
            PagamentoResponseDTO dto = mapToDTO(sottoscrizione.getPagamento());
            dto.setCausale("Sottoscrizione Abbonamento: " + sottoscrizione.getAbbonamento().getTipoAbb());
            storico.add(dto);
        });

        // Ordina lo storico per data decrescente (più recenti prima)
        storico.sort((p1, p2) -> p2.getDataPag().compareTo(p1.getDataPag()));
        return storico;
    }

    // Genera una ricevuta formattata testuale per un determinato pagamento
    public String generaRicevuta(Long idPagamento) {
        Pagamento p = repo.findById(idPagamento)
                .orElseThrow(() -> new RuntimeException("Pagamento non trovato"));

        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("             RICEVUTA DI PAGAMENTO      \n");
        sb.append("========================================\n");
        sb.append(String.format("ID Transazione: %d\n", p.getIdPagamento()));
        sb.append(String.format("Data:           %s\n", p.getDataPag()));
        sb.append(String.format("Metodo:         %s\n", p.getMetodoPag()));
        sb.append(String.format("Stato:          %s\n", p.getStatoPag()));
        sb.append("----------------------------------------\n");
        sb.append(String.format("IMPORTO TOTALE: € %.2f\n", p.getImporto()));
        sb.append("========================================\n");
        sb.append("Grazie per aver utilizzato i nostri servizi!\n");
        
        return sb.toString();
    }
}
