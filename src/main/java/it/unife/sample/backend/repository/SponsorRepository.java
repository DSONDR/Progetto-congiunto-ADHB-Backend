package it.unife.sample.backend.repository;

import it.unife.sample.backend.model.Sponsor;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SponsorRepository extends JpaRepository<Sponsor, Long> {

    // ricerca sponsor per azienda
    List<Sponsor> findByAzienda(String azienda);

    // ricerca sponsor per partita iva
    List<Sponsor> findByPIva(String pIva);

}