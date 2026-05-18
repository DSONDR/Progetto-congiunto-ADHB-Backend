package it.unife.sample.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO utilizzato per la creazione o modifica di una Squadra.
 */
@Data
public class SquadraRequestDTO {
    
    @NotBlank(message = "Il nome della squadra è obbligatorio")
    private String nome;
    
    @NotBlank(message = "Lo sport è obbligatorio")
    private String sport;
    
    private String campionato;
    
    @NotBlank(message = "Il codice fiscale dell'allenatore è obbligatorio")
    private String allenatoreCf;
}
