package it.unife.sample.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO utilizzato dall'utente per aprire un nuovo ticket di assistenza.
 */
@Data
public class AssistenzaRequestDTO {
    
    @NotBlank(message = "L'oggetto della richiesta è obbligatorio")
    private String oggetto;
    
    @NotBlank(message = "Il tipo di assistenza è obbligatorio")
    private String tipoAss;
    
    @NotBlank(message = "Il codice fiscale dell'utente è obbligatorio")
    private String utenteCf;
}
