package it.unife.sample.backend.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO utilizzato quando un Atleta intende iscriversi a un'attività scalando un
 * ingresso
 * dal proprio abbonamento (o sfruttando l'abbonamento a tempo)
 * Utilizzato in IscrizioneController (metodo usaAbbonamento)
 * e processato da IscrizioneService (metodo iscriviConAbbonamento)
 */
@Data
public class UsaAbbonamentoRequest {

    @NotBlank(message = "Il codice fiscale dell'atleta è obbligatorio")
    private String atletaCf;

    @NotNull(message = "L'ID dell'attività è obbligatorio")
    private Long attivitaId;

    @NotNull(message = "L'ID dell'abbonamento è obbligatorio")
    private Long abbonamentoId;
}
