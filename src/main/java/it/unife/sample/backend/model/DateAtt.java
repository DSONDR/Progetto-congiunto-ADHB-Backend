package it.unife.sample.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "DATE_ATT")
@IdClass(DateAttId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateAtt {
    @Id
    @Column(name = "Date", nullable = false)
    @NotNull
    private LocalDateTime date;

    @Id
    @ManyToOne
    @JoinColumn(name = "Codice_Att")
    @NotNull
    @JsonIgnore		//Per evitare ricorsione nei rapporti bidirezionali
    private Attivita attivita;
}
