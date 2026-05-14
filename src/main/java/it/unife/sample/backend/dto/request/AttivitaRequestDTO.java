package it.unife.sample.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AttivitaRequestDTO {

    private Long codiceAtt;

    @NotBlank(message = "Nome attività obbligatorio")
    private String nomeAtt;

    @NotBlank(message = "Tipo evento obbligatorio")
    private String tipoEvento;

    private String destinatario;

    @PositiveOrZero(message = "Quota base deve essere zero o positiva")
    private Double quotaBase;

    @NotNull(message = "Numero massimo di partecipanti obbligatorio")
    @Positive(message = "Numero massimo di partecipanti deve essere positivo")
    private Integer maxPartecipanti;

    @NotBlank(message = "Codice fiscale istruttore obbligatorio")
    private String istruttoreCf;

    @NotNull(message = "Id impianto obbligatorio")
    private Long impiantoId;

    private List<LocalDateTime> dateOrari;
}
