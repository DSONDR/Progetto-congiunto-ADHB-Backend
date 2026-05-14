package it.unife.sample.backend.repository;

import it.unife.sample.backend.model.Sponsorizzazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SponsorizzazioneRepository extends JpaRepository<Sponsorizzazione, Long> {
    List<Sponsorizzazione> findBySponsorPIva(String pIva);
    List<Sponsorizzazione> findBySquadraId(Long idSquadra);
    List<Sponsorizzazione> findByImpiantoId(Long idImpianto);
}