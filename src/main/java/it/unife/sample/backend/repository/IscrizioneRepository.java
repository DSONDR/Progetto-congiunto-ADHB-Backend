package it.unife.sample.backend.repository;

import it.unife.sample.backend.model.Iscrizione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import it.unife.sample.backend.model.IscrSingolaId;

@Repository
public interface IscrizioneRepository extends JpaRepository<Iscrizione, IscrSingolaId> {

    // Cerca tutte le iscrizioni di un utente dato il suo cf
    List<Iscrizione> findByUtenteCf(String cf);

    // Cerca tutte le iscrizioni a un'attività dato il suo codice
    List<Iscrizione> findByAttivitaCodiceAtt(Long idAttivita);

    // Conta il numero di iscritti a un'attività
    long countByAttivitaCodiceAtt(Long idAttivita);
}
