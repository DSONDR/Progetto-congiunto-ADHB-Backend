package it.unife.sample.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "USA_ABB")
@IdClass(UsaAbbId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsaAbb {
    @Id
    @ManyToOne
    @JoinColumn(name = "Numero_Abb")
    @NotNull
    private Abbonamento abbonamento;

    @Id
    @ManyToOne
    @JoinColumn(name = "Codice_Att")
    @NotNull
    private Attivita attivita;

    @Id
    @ManyToOne
    @JoinColumn(name = "Cf")
    @NotNull
    @JsonIgnore		//Per evitare ricorsione nei rapporti bidirezionali
    private Utente utente;

    @Column(name = "Data_Uso")
    @NotNull
    private LocalDate dataUso;
}
