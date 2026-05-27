package it.unife.sample.backend.repository; // Cartella repository

// Devi importare il Model perché si trova in un'altra cartella (package)
import it.unife.sample.backend.model.Sponsorizzazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SponsorizzazioneRepository extends JpaRepository<Sponsorizzazione, Long> {

    // Recupera i record filtrando per SponsorPartitaIva
    // Usato da SponsorizzazioneService.findBySponsorPartitaIva()
    List<Sponsorizzazione> findBySponsorPartitaIva(String partitaIva);
    
    // Recupera i record filtrando per SquadraId
    // Usato da SponsorizzazioneService.findBySquadraId()
    List<Sponsorizzazione> findBySquadraId(Long idSquadra);
    
    // Recupera i record filtrando per ImpiantoId
    // Usato da SponsorizzazioneService.findByImpiantoId()
    List<Sponsorizzazione> findByImpiantoId(Long idImpianto);
    
}
