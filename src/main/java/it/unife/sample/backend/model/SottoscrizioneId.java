package it.unife.sample.backend.model;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SottoscrizioneId implements Serializable {
    private Long abbonamento;
    private Long pagamento;
    private String atleta;
}

//EntitàId.java, richiesta da JPA per le chiavi composte
