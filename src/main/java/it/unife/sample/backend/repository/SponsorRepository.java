package it.unife.sample.backend.repository;

import it.unife.sample.backend.model.Sponsor;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SponsorRepository extends JpaRepository<Sponsor, String> {

    // ricerca sponsor per azienda
    List<Sponsor> findByAzienda(String azienda);

    // ricerca sponsor per partita iva
    List<Sponsor> findByPIva(String pIva);

    // ricerca sponsor che sponsorizzano una determinata squadra
    List<Sponsor> findBySquadreId(Long idSquadra);

    // ricerca sponsor che sponsorizzano un determinato impianto
    List<Sponsor> findByImpiantiId(Long idImpianto);

}