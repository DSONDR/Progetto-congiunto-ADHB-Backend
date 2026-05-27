package it.unife.sample.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "SPONSORIZZAZIONE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sponsorizzazione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sponsorizzazione")
    private Long idSponsorizzazione;

    @NotNull
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "P_IVA")
    private Sponsor sponsor;

    @ManyToOne
    @JoinColumn(name = "Id_Squadra")
    private Squadra squadra;

    @ManyToOne
    @JoinColumn(name = "Id_Impianto")
    private Impianto impianto;

    @Column(name = "Data_Inizio")
    @NotNull
    private LocalDate dataInizio;

    @Column(name = "Data_Fine")
    @NotNull
    private LocalDate dataFine;

    @Column(name = "Importo")
    @NotNull
    private Double importo;
}
