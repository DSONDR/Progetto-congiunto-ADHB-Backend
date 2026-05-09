package it.unife.sample.backend.service;
import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.unife.sample.backend.model.Attivita;
import it.unife.sample.backend.repository.AttivitaRepository;
import it.unife.sample.backend.repository.IscrizioneRepository;
import it.unife.sample.backend.repository.UsaAbbRepository;

@Service
public class AttivitaService {
    @Autowired private AttivitaRepository repo;
    @Autowired private IscrizioneRepository iscRepo;
    @Autowired private UsaAbbRepository usaRepo;
    
    //CRUD base
    public List<Attivita> findAll() {
        return repo.findAll();
    }

    public Optional<Attivita> findById(Long id) {
        return repo.findById(id);
    }

    @Transactional
    public Attivita save(Attivita a) {
        return repo.save(a);
    }

    @Transactional
    public void deleteById(Long id) {
        repo.deleteById(id);
    }
    
    //Altre
    public List<Attivita> getCalendario(LocalDateTime inizio, LocalDateTime fine) {
        return repo.findByDataOraBetween(inizio, fine);
    }

    // --- FILTRI ---
    public List<Attivita> filtra(Long idImpianto, Double prezzo, String target, String tipoEvento) {
        return repo.findFiltered(idImpianto, prezzo, target, tipoEvento);
    }

    // --- LOGICA POSTI ---
    public int getNumeroIscritti(Long idAttivita) {
        // Sommiamo chi ha pagato il singolo ingresso e chi usa l'abbonamento
        return (int) (iscRepo.countByAttivitaCodiceAtt(idAttivita) + usaRepo.countByAttivitaCodiceAtt(idAttivita));
    }

    public boolean isPostoDisponibile(Long idAttivita) {
        Attivita a = repo.findById(idAttivita).orElseThrow();
        return getNumeroIscritti(idAttivita) < a.getMaxPartecipanti();
    }
}
