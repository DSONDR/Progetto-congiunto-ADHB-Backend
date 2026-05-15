package it.unife.sample.backend.repository; // Cartella repository

// Devi importare il Model perché si trova in un'altra cartella (package)
import it.unife.sample.backend.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, String> {

    // Cerca un utente per la sua email
    Optional<Utente> findByEmail(String email);

    // Cerca un utente per il suo username
    Optional<Utente> findByUsername(String username);

    // Controlla se esiste un utente con la mail indicata
    boolean existsByEmail(String email);

    // Controlla se esiste un utente con l'username indicato
    boolean existsByUsername(String username);
}
