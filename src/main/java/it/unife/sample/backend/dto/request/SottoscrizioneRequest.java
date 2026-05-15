package it.unife.sample.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO utilizzato per l'acquisto (Sottoscrizione) di un Abbonamento
 * Raccoglie i dati inviati dal frontend quando l'Atleta sceglie il tipo di
 * abbonamento
 * Utilizzato nel file SottoscrizioneController (metodo sottoscrivi)
 * e processato da SottoscrizioneService (metodo sottoscrivi)
 */
@Data
public class SottoscrizioneRequest {

    @NotBlank(message = "Il codice fiscale dell'atleta è obbligatorio")
    private String atletaCf;

    @NotBlank(message = "Il tipo di abbonamento è obbligatorio")
    private String tipoAbbonamento;

    @NotBlank(message = "Il metodo di pagamento è obbligatorio")
    private String metodo;
}
