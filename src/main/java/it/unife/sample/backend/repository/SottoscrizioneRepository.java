package it.unife.sample.backend.repository; // Cartella repository

// Devi importare il Model perché si trova in un'altra cartella (package)
import it.unife.sample.backend.model.Sottoscrizione; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SottoscrizioneRepository extends JpaRepository<Sottoscrizione, Long> {
    List<Sottoscrizione> findByUtenteCf(String cf);
}
