package it.unife.sample.backend.dto.response;

import it.unife.sample.backend.model.Sottoscrizione;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * DTO di risposta per l'acquisto di un Abbonamento (Sottoscrizione).
 * Contiene la Sottoscrizione creata e un eventuale avviso non bloccante,
 * ad esempio la mancanza di un certificato medico valido.
 * Utilizzato da SottoscrizioneController (metodo sottoscrivi)
 * e prodotto da SottoscrizioneService (metodo sottoscrivi).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SottoscrizioneResponseDTO {

    private Sottoscrizione sottoscrizione;

    // null se tutto è in regola, stringa di avviso se c'è qualcosa da segnalare
    // (es. certificato medico mancante o scaduto)
    private String avviso;
}
