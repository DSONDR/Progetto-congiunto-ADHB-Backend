package it.unife.sample.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * DTO utilizzato per la richiesta di Iscrizione Singola a un'attività
 * Serve a raccogliere i dati dal frontend quando un Atleta decide di pagare
 * singolarmente per un'attività
 * Utilizzato nel file IscrizioneController (metodo iscriviSingola)
 * e processato da IscrizioneService (metodo iscriviSingola)
 */
@Data
public class IscrizioneRequest {

    @NotBlank(message = "Il codice fiscale dell'atleta è obbligatorio")
    private String atletaCf;

    @NotNull(message = "L'ID dell'attività è obbligatorio")
    private Long attivitaId;

    @NotNull(message = "L'importo è obbligatorio")
    @Positive(message = "L'importo deve essere positivo")
    private Double importo;

    @NotBlank(message = "Il metodo di pagamento è obbligatorio")
    private String metodo;
}
