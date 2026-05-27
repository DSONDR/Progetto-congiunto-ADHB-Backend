package it.unife.sample.backend.model;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartecipazioneSqId implements Serializable {

    private Long squadra;
    private Long attivita;
}

//EntitàId.java, richiesta da JPA per le chiavi composte
