package it.unife.sample.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "PARTECIPAZIONE_SQ")
@IdClass(PartecipazioneSqId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartecipazioneSq {

    @Id
    @ManyToOne
    @JoinColumn(name = "Id_Squadra")
    private Squadra squadra;

    @Id
    @ManyToOne
    @JoinColumn(name = "Codice_Att")
    private Attivita attivita;
}
