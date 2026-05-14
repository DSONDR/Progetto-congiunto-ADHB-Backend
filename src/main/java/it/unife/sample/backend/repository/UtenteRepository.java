package it.unife.sample.backend.repository; // Cartella repository

// Devi importare il Model perché si trova in un'altra cartella (package)
import it.unife.sample.backend.model.Utente; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, String> {

    Optional<Utente> findByEmail(String email);

    Optional<Utente> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsById(String cf);
}
