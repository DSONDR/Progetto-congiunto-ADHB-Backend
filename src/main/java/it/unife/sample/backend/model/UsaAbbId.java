package it.unife.sample.backend.model;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsaAbbId implements Serializable {
    private Long abbonamento;
    private Long attivita;
    private String utente;
}

//EntitàId.java, richiesta da JPA per le chiavi composte
