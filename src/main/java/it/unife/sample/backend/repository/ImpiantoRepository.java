package it.unife.sample.backend.repository;

import it.unife.sample.backend.model.Impianto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImpiantoRepository extends JpaRepository<Impianto, Long> {

    // Trova impianti per tipo
    List<Impianto> findByTipoImpianto(String tipoImpianto);

    // Trova impianti per stato
    List<Impianto> findByStato(String stato);

    // Trova impianto per omologazione
    List<Impianto> findByOmologazione(String omologazione);

}