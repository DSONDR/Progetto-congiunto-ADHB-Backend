package it.unife.sample.backend.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

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
}
