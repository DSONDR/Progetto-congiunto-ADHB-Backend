package it.unife.sample.backend.repository;

import it.unife.sample.backend.model.PartecipazioneSq;
import it.unife.sample.backend.model.PartecipazioneSqId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartecipazioneSqRepository extends JpaRepository<PartecipazioneSq, PartecipazioneSqId> {

    List<PartecipazioneSq> findBySquadraId(Long squadraId);

    List<PartecipazioneSq> findByAttivitaCodiceAtt(Long attivitaId);
}
