package it.unife.sample.backend.repository;

import it.unife.sample.backend.model.Iscrizione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IscrizioneRepository extends JpaRepository<Iscrizione, Long> {
    List<Iscrizione> findByUtenteCf(String cf);
    List<Iscrizione> findByAttivitaCodiceAtt(Long idAttivita);
    long countByAttivitaCodiceAtt(Long idAttivita);
}
