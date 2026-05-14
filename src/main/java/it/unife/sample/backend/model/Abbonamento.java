package it.unife.sample.backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "ABBONAMENTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Abbonamento {
    @Id
    @Column(name = "Numero_Abb", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long numeroAbb;
    
    @Column(name = "Data_Inizio")
    @NotNull(message = "Inizio obbligatorio")
    private LocalDate dataInizio;
    @Column(name = "Data_Scadenza")
    private LocalDate dataScadenza;
    @Column(name = "Tipo_Abb")
    @NotBlank(message = "Tipo obbligatorio")
    private String tipoAbb;
    
    @Column(name = "Ingressi_Effettuati")
    private Integer ingressiEffettuati;
    @Column(name = "Stato_Abb")
    private String statoAbb;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "Cf_atleta")
    private Atleta atleta;

    @NotNull
    @OneToOne
    @JoinColumn(name = "Id_Pagamento")
    private Pagamento pagamento;
}
