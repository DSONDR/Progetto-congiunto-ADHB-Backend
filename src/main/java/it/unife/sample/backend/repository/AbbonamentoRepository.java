package it.unife.sample.backend.repository; // Cartella repository

// Devi importare il Model perché si trova in un'altra cartella (package)
import it.unife.sample.backend.model.Abbonamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AbbonamentoRepository extends JpaRepository<Abbonamento, Long> {

    // Recupera tutti gli abbonamenti di un atleta specifico
    // Usato da AbbonamentoService.findByAtletaCf() nella visualizzazione storico atleta
    List<Abbonamento> findByAtletaCf(String cf);

    // Recupera gli abbonamenti TEMPO con data di scadenza passata
    // Usato da AbbonamentoService.aggiornaScaduti() nel task schedulato giornaliero
    List<Abbonamento> findByStatoAbbAndDataScadenzaBefore(String statoAbb, LocalDate data);

    // Recupera gli abbonamenti ATTIVO senza dataScadenza (proxy per tipo INGRESSI)
    // Usato da AbbonamentoService.aggiornaScaduti() per verificare ingressi esauriti
    List<Abbonamento> findByStatoAbbAndDataScadenzaIsNull(String statoAbb);
}
