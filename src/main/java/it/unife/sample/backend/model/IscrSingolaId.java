package it.unife.sample.backend.model;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IscrSingolaId implements Serializable {
    private Long attivita;
    private Long pagamento;
    private String utente;
}

//EntitàId.java, richiesta da JPA per le chiavi composte
