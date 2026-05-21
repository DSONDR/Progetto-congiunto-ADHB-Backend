package it.unife.sample.backend.dto.response;

import lombok.Data;

@Data
public class AssistenzaResponseDTO {
    private Long idTicket;
    private String oggetto;
    private String tipoAss;
    private String stato;
    private Integer soddisfazione;
    private String utenteCf;
    private String assistenteCf;
}
