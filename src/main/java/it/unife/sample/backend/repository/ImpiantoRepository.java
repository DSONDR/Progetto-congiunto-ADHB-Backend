package it.unife.sample.backend.repository; // Cartella repository

// Devi importare il Model perché si trova in un'altra cartella (package)
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

}
