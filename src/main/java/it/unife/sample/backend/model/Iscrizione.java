package it.unife.sample.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "ISCR_SINGOLA")
@IdClass(IscrSingolaId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Iscrizione {
    @Id
    @ManyToOne
    @JoinColumn(name = "Codice_Att")
    @NotNull
    private Attivita attivita;

    @Id
    @NotNull(message = "Id-pagamento obbligatorio")
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "Id_Pagamento")
    private Pagamento pagamento;

    @Id
    @ManyToOne
    @JoinColumn(name = "Cf")
    @NotNull
    @JsonIgnore		//Per evitare ricorsione nei rapporti bidirezionali
    private Utente utente;

    @Column(name = "Data_Iscr")
    @NotNull
    private LocalDate dataIscr;
}
