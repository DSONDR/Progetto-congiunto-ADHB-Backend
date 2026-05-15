package it.unife.sample.backend.repository;

import it.unife.sample.backend.model.Assistenza;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssistenzaRepository extends JpaRepository<Assistenza, Long> {

    // Ricerca ticket per stato
    List<Assistenza> findByStato(String stato);

    // Ricerca ticket per tipo assistenza
    List<Assistenza> findByTipoAss(String tipoAss);

    // Ricerca ticket per soddisfazione
    List<Assistenza> findBySoddisfazione(String soddisfazione);

}