package it.unife.sample.backend.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO utilizzato per l'invio dei dati di un'Attività al frontend
 * Contiene i dati completi dell'attività, compreso il nome dell'impianto
 * associato e le date
 * Creato in AttivitaService (metodo mapToResponse)
 * e restituito da AttivitaController (metodi getAll, getById, create, update,
 * getCalendario, filtra)
 */
@Data
public class AttivitaResponseDTO {

    private Long codiceAtt;
    private String nomeAtt;
    private String tipoEvento;
    private String destinatario;
    private Double quotaBase;
    private Integer maxPartecipanti;
    private String istruttoreCf;
    private String impiantoNome;
    private Long impiantoId;
    private List<LocalDateTime> dateOrari;
    private List<SquadraResponseDTO> squadreAderenti;
}
