package it.unife.sample.backend.repository; // Cartella repository

// Devi importare il Model perché si trova in un'altra cartella (package)
import it.unife.sample.backend.model.Pagamento; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    // Filtro per data
    List<Pagamento> findByDataPagBetween(LocalDateTime start, LocalDateTime end);

    // Filtro per prezzo (Range)
    List<Pagamento> findByImportoBetween(Double min, Double max);
}
