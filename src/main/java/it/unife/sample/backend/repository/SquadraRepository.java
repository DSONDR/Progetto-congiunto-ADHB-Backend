package it.unife.sample.backend.repository;

import it.unife.sample.backend.model.Squadra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SquadraRepository extends JpaRepository<Squadra, Long> {

    // Trova le squadre allenate da un allenatore
    List<Squadra> findByAllenatoreCf(String cf);

    // Trova le squadre di cui un atleta fa parte
    List<Squadra> findByAtletaCf(String cf);
}