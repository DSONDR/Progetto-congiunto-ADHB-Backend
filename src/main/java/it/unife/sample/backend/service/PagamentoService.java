package it.unife.sample.backend.service;

import java.util.*;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.unife.sample.backend.model.Pagamento;
import it.unife.sample.backend.model.Iscrizione;
import it.unife.sample.backend.repository.PagamentoRepository;
import it.unife.sample.backend.repository.IscrizioneRepository;

@Service
public class PagamentoService {
    @Autowired
    private PagamentoRepository repo;
    @Autowired
    private IscrizioneRepository iscRepo;

    // CRUD base
    public List<Pagamento> findAll() {
        return repo.findAll();
    }

    public Optional<Pagamento> findById(Long id) {
        return repo.findById(id);
    }

    public Pagamento save(Pagamento pagamento) {
        return repo.save(pagamento);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    // Altre funzioni, questa per i pagamenti in base alla data o all'importo
    // Usata in ???
    public List<Pagamento> ricercaAvanzata(LocalDateTime da, LocalDateTime a, Double min, Double max) {
        if (da != null && a != null)
            return repo.findByDataPagBetween(da, a);
        if (min != null && max != null)
            return repo.findByImportoBetween(min, max);
        return repo.findAll();
    }

    // Filtro per Attività
    // Usato in ???
    public List<Pagamento> getPagamentiPerAttivita(Long idAttivita) {
        return iscRepo.findByAttivitaCodiceAtt(idAttivita).stream()
                .map(Iscrizione::getPagamento)
                .collect(Collectors.toList());
    }
}
