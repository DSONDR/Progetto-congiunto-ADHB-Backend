package it.unife.sample.backend.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.unife.sample.backend.model.Abbonamento;
import it.unife.sample.backend.repository.AbbonamentoRepository;
import it.unife.sample.backend.dto.response.TipoAbbonamentoDTO;

@Service
public class AbbonamentoService {
    // Richiami alle funzioni dei repository di:
    @Autowired
    private AbbonamentoRepository repo;

    // CRUD base
    public List<Abbonamento> findAll() {
        return repo.findAll();
    }

    public Optional<Abbonamento> findById(Long id) {
        return repo.findById(id);
    }

    public Abbonamento save(Abbonamento abbonamento) {
        return repo.save(abbonamento);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    // Metodo di Gestione tipi abbonamento - Crea una lista (finta)
    // di abbonamenti tra cui scegliere al momento dell'acquisto
    // Usato nella funzionalità di sottoscrizione degli abbonamenti
    public List<TipoAbbonamentoDTO> getTipiAbbonamento() {
        return List.of(
                new TipoAbbonamentoDTO("Mensile", "TEMPO", 50.0, 1, null),
                new TipoAbbonamentoDTO("Trimestrale", "TEMPO", 135.0, 3, null),
                new TipoAbbonamentoDTO("Annuale", "TEMPO", 450.0, 12, null),
                new TipoAbbonamentoDTO("10 Ingressi", "INGRESSI", 70.0, null, 10),
                new TipoAbbonamentoDTO("20 Ingressi", "INGRESSI", 130.0, null, 20),
                new TipoAbbonamentoDTO("30 Ingressi", "INGRESSI", 180.0, null, 30));
    }

    // Metodo per ritrovare i dettagli di un tipo di abbonamento tramite il suo
    // nomeDto
    // Usato nella funzionalità di sottoscrizione degli abbonamenti o l'uso
    // dell'abbonamento nell'iscrizione
    public Optional<TipoAbbonamentoDTO> getDettagliTipo(String nomeTipoDto) {
        return getTipiAbbonamento().stream()
                .filter(t -> t.getNome().equalsIgnoreCase(nomeTipoDto))
                .findFirst();
    }
}
