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
    List<Attivita> findByImpiantoId(Long id);
    List<Attivita> findByQuotaBaseLessThanEqual(Double prezzoMax);
    List<Attivita> findByDestinatario(String destinatario);
    List<Attivita> findByIstruttoreCf(String cfIstruttore);

    @Query("SELECT DISTINCT a FROM Attivita a JOIN a.dateAtts d WHERE d.date BETWEEN :start AND :end")
    List<Attivita> findByDateAttsDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // Filtro flessibile: filtra per i campi forniti (null = ignora)
    @Query("SELECT DISTINCT a FROM Attivita a LEFT JOIN a.dateAtts d WHERE " +
           "(:impiantoId IS NULL OR a.impianto.id = :impiantoId) AND " +
           "(:prezzo IS NULL OR a.quotaBase <= :prezzo) AND " +
           "(:target IS NULL OR a.destinatario = :target) AND " +
           "(:tipoEvento IS NULL OR a.tipoEvento = :tipoEvento) AND " +
           "(:inizio IS NULL OR d.date >= :inizio) AND " +
           "(:fine IS NULL OR d.date <= :fine)")
    List<Attivita> findFiltered(@Param("impiantoId") Long impiantoId, 
                                @Param("prezzo") Double prezzo, 
                                @Param("target") String target, 
                                @Param("tipoEvento") String tipoEvento,
                                @Param("inizio") LocalDateTime inizio,
                                @Param("fine") LocalDateTime fine);
}
