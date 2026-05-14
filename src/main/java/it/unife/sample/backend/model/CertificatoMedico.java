package it.unife.sample.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "CERTIFICATO_MEDICO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificatoMedico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Certificato")
    private Long idCertificato;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "Cf")
    private Utente utente;

    @Column(name = "Tipo")
    @NotBlank
    private String tipo;

    @Column(name = "Data_Emissione")
    @NotNull
    private LocalDate dataEmissione;

    @Column(name = "Data_Scadenza")
    @NotNull
    private LocalDate dataScadenza;

    @Column(name = "Medico_Riferimento")
    @NotBlank
    private String medicoRiferimento;
}
