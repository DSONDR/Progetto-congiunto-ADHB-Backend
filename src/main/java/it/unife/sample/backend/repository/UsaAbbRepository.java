package it.unife.sample.backend.repository; // Cartella repository

// Devi importare il Model perché si trova in un'altra cartella (package)
import it.unife.sample.backend.model.UsaAbb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import it.unife.sample.backend.model.UsaAbbId;

@Repository
public interface UsaAbbRepository extends JpaRepository<UsaAbb, UsaAbbId> {

    // Conta quanti atleti hanno usato un abbonamento per un'attività
    // (utilizzato per limiti di capienza assieme al counter di Iscrizione)
    long countByAttivitaCodiceAtt(Long idAttivita);

    // Conta quante volte è stato utilizzato un abbonamento
    // (in totale, non per attività)
    long countByAbbonamentoNumeroAbb(Long numeroAbb);

    // Cerca un uso abbonamento tramite QR code
    Optional<UsaAbb> findByQrCode(String qrCode);
}
