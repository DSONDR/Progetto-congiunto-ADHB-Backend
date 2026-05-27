package it.unife.sample.backend.repository; // Cartella repository

// Devi importare il Model perché si trova in un'altra cartella (package)
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.unife.sample.backend.model.Attivita;

@Repository
public interface AttivitaRepository extends JpaRepository<Attivita, Long> {

       // Recupera tutte le attività che si svolgono un impianto
       List<Attivita> findByImpiantoId(Long id);

       // Recupera le attività assegnate a un istruttore
       List<Attivita> findByIstruttoreCf(String cfIstruttore);

       // Trova attività programmate in un intervallo di date
       @Query("SELECT DISTINCT a FROM Attivita a JOIN a.dateAtts d WHERE d.date BETWEEN :start AND :end")
       List<Attivita> findByDateAttsDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

       // Filtro di ricerca dinamico, ignora i campi nulli e filtra gli altri in base ai parametri passati
       // Usato da AttivitaService.filtra() nella funzionalità di ricerca pre iscrizione
       @Query("SELECT DISTINCT a FROM Attivita a LEFT JOIN a.dateAtts d LEFT JOIN a.squadreAderenti sq WHERE " +
                     "(:impiantoId IS NULL OR a.impianto.id = :impiantoId) AND " +
                     "(:prezzo IS NULL OR a.quotaBase <= :prezzo) AND " +
                     "(:target IS NULL OR a.destinatario = :target) AND " +
                     "(:tipoEvento IS NULL OR a.tipoEvento = :tipoEvento) AND " +
                     "(:istruttoreCf IS NULL OR a.istruttore.cf = :istruttoreCf OR sq.allenatore.cf = :istruttoreCf) AND " +
                     "(:squadraId IS NULL OR sq.id = :squadraId) AND " +
                     "(:inizio IS NULL OR d.date >= :inizio) AND " +
                     "(:fine IS NULL OR d.date <= :fine)")
       List<Attivita> findFiltered(@Param("impiantoId") Long impiantoId,
                     @Param("prezzo") Double prezzo,
                     @Param("target") String target,
                     @Param("tipoEvento") String tipoEvento,
                     @Param("istruttoreCf") String istruttoreCf,
                     @Param("squadraId") Long squadraId,
                     @Param("inizio") LocalDateTime inizio,
                     @Param("fine") LocalDateTime fine);

       // Verifica se esiste già un'attività in quell'impianto in date specifiche
       // Usato da AttivitaService.create() nella funzionalità di creazione attività
       @Query("SELECT COUNT(a) > 0 FROM Attivita a JOIN a.dateAtts d WHERE a.impianto.id = :impiantoId AND d.date IN :date")
       boolean existsByImpiantoAndDateOverlap(@Param("impiantoId") Long impiantoId,
                     @Param("date") List<LocalDateTime> date);

       // Verifica sovrapposizioni escludendo l'attività corrente (usato in update)
       // Usato da AttivitaService.update() nella funzionalità di modifica attività
       @Query("SELECT COUNT(a) > 0 FROM Attivita a JOIN a.dateAtts d WHERE a.impianto.id = :impiantoId AND a.codiceAtt != :codiceAtt AND d.date IN :date")
       boolean existsByImpiantoAndDateOverlapExcluding(@Param("impiantoId") Long impiantoId,
                     @Param("codiceAtt") Long codiceAtt, @Param("date") List<LocalDateTime> date);

       // --- FILTRI DINAMICI FRONTEND ---
       @Query("SELECT DISTINCT a.tipoEvento FROM Attivita a WHERE a.tipoEvento IS NOT NULL")
       List<String> findDistinctTipiEvento();

       @Query("SELECT DISTINCT a.destinatario FROM Attivita a WHERE a.destinatario IS NOT NULL")
       List<String> findDistinctDestinatari();
}
