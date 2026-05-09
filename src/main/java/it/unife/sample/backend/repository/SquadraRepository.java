package it.unife.sample.backend.repository;

import it.unife.sample.backend.model.Squadra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SquadraRepository extends JpaRepository<Squadra, Long> {
	List<Squadra> findByAllenatoreCf(String cf);
	List<Squadra> findByAtletaCf(String cf);
}
