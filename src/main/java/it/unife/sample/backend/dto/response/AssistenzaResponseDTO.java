package it.unife.sample.backend.dto.response;

import lombok.Data;

/**
 * DTO di risposta per i ticket di assistenza.
 * Creato da AssistenzaService (tramite conversione dell'entità Assistenza) e restituito da AssistenzaController.
 */
@Data
public class AssistenzaResponseDTO {
    private Long idTicket;
    private String oggetto;
    private String tipoAss;
    private String stato;
    private Integer soddisfazione;
    private String utenteCf;
    private String assistenteCf;
    private String contenuto;
    private String risposta;
}
