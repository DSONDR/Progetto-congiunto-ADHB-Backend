package it.unife.sample.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO utilizzato per la creazione e gestione di un Pagamento
 * Raccoglie i dati inviati dal frontend relativi al pagamento
 * di attività o abbonamenti
 * 
 * Utilizzato nel file PagamentoController
 * e processato da PagamentoService
 */
@Data
public class PagamentoRequestDTO {

    private Long idPagamento;

    @NotNull(message = "Importo obbligatorio")
    @Positive(message = "L'importo deve essere positivo")
    private BigDecimal importo;

    @NotBlank(message = "Metodo di pagamento obbligatorio")
    private String metodoPagamento;

    private String statoPagamento;

    private String fattura;

    private LocalDateTime dataPagamento;

     // Utente che effettua il pagamento
     
    @NotBlank(message = "Codice fiscale utente obbligatorio")
    private String utenteCf;

    // Attività associata al pagamento (opzionale)

    private Long attivitaId;

    // Abbonamento associato al pagamento (opzionale)

    private Long abbonamentoId;
}
