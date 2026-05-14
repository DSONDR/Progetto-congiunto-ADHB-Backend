package it.unife.sample.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "COMPOSIZIONE_SQUADRA")
@IdClass(ComposizioneSquadraId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComposizioneSquadra {

    @Id
    @ManyToOne
    @JoinColumn(name = "Id_Squadra")
    private Squadra squadra;

    @Id
    @ManyToOne
    @JoinColumn(name = "Cf_Atleta")
    private Atleta atleta;
}
