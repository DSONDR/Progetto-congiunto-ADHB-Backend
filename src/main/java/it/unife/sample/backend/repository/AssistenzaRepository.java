package it.unife.sample.backend.repository;

import it.unife.sample.backend.model.Assistenza;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssistenzaRepository extends JpaRepository<Assistenza, Long> {

    // ricerca ticket per stato
    List<Assistenza> findByStato(String stato);

    // ricerca ticket per tipo assistenza
    List<Assistenza> findByTipoAss(String tipoAss);

    // ricerca ticket per soddisfazione
    List<Assistenza> findBySoddisfazione(String soddisfazione);

}