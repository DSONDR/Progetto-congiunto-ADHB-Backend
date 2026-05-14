package it.unife.sample.backend.repository; // Cartella repository

// Devi importare il Model perché si trova in un'altra cartella (package)
import it.unife.sample.backend.model.UsaAbb; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.unife.sample.backend.model.UsaAbbId;

@Repository
public interface UsaAbbRepository extends JpaRepository<UsaAbb, UsaAbbId> {
    long countByAttivitaCodiceAtt(Long idAttivita); // Per il contatore iscritti con abbonamento
    long countBySottoscrizioneId(Long idSottoscrizione);
}
