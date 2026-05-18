package it.unife.sample.backend.dto.response;

import lombok.Data;
import java.time.LocalDate;

/**
 * DTO di risposta per la visualizzazione dei dettagli di un pagamento.
 * Evita di esporre direttamente l'entità JPA e aggiunge un campo "causale"
 * per indicare da dove proviene il pagamento (es. Iscrizione o Abbonamento).
 */
@Data
public class PagamentoResponseDTO {
    
    private Long idPagamento;
    private String metodoPag;
    private LocalDate dataPag;
    private String statoPag;
    private Double importo;
    private String fattura;
    
    // Campo aggiunto per dare maggiore contesto al client
    private String causale;
}
