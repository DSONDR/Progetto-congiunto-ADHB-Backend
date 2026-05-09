package it.unife.sample.backend.repository; // Cartella repository

// Devi importare il Model perché si trova in un'altra cartella (package)
import it.unife.sample.backend.model.Attivita; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface AttivitaRepository extends JpaRepository<Attivita, Long> {
    List<Attivita> findByImpiantoId(Long id);
    List<Attivita> findByQuotaBaseLessThanEqual(Double prezzoMax);
    List<Attivita> findByDestinatario(String destinatario);
    
    int getNumeroIscritti(Long idAttivita);
    boolean isPostoDisponibile(Long idAttivita);
    // Utile per il calendario: trova attività tra due date
    List<Attivita> findByDataOraBetween(LocalDateTime start, LocalDateTime end);

    // Filtro flessibile: filtra per i campi forniti (null = ignora)
    @Query("SELECT a FROM Attivita a WHERE " +
           "(:impiantoId IS NULL OR a.impianto.id = :impiantoId) AND " +
           "(:prezzo IS NULL OR a.quotaBase <= :prezzo) AND " +
           "(:target IS NULL OR a.destinatario = :target) AND " +
           "(:tipoEvento IS NULL OR a.tipoEvento = :tipoEvento)")
    List<Attivita> findFiltered(@Param("impiantoId") Long impiantoId, 
                                @Param("prezzo") Double prezzo, 
                                @Param("target") String target, 
                                @Param("tipoEvento") String tipoEvento);
}
