package it.unife.sample.backend.dto.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO di risposta alla validazione del QR code.
 * Restituisce i metadati dell'uso del codice, sia in caso di iscrizione singola
 * sia in caso di uso di abbonamento.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QrCodeValidationResponseDTO {
    private String tipo;
    private String qrCode;
    private String atletaCf;
    private Long codiceAttivita;
    private Long idPagamento;
    private Long numeroAbbonamento;
    private LocalDate dataRiferimento;
}
