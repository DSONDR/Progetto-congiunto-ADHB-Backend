package it.unife.sample.backend.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * DTO utilizzato per definire un "pacchetto" di abbonamento acquistabile
 * Contiene i dettagli di prezzo, tipo (TEMPO o INGRESSI)
 * Creato in AbbonamentoService (metodi getTipiAbbonamento, getDettagliTipo)
 * e restituito da AbbonamentoController (metodo getTipiAbbonamento)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoAbbonamentoDTO {
    private String nome;
    private String tipo; // "TEMPO" o "INGRESSI"
    private Double prezzo;
    private Integer durataMesi;
    private Integer maxIngressi;
}
