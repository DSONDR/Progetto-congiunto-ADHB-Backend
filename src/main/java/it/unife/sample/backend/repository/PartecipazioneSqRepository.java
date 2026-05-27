package it.unife.sample.backend.repository; // Cartella repository

// Devi importare il Model perché si trova in un'altra cartella (package)
import it.unife.sample.backend.model.PartecipazioneSq;
import it.unife.sample.backend.model.PartecipazioneSqId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartecipazioneSqRepository extends JpaRepository<PartecipazioneSq, PartecipazioneSqId> {
    
    // Recupera i record filtrando per SquadraId
    // Usato da PartecipazioneSqService per la logica di business e le chiamate API
    List<PartecipazioneSq> findBySquadraId(Long squadraId);
   
    // Recupera i record filtrando per AttivitaCodiceAtt
    // Usato da PartecipazioneSqService per la logica di business e le chiamate API
    List<PartecipazioneSq> findByAttivitaCodiceAtt(Long attivitaId);

}
