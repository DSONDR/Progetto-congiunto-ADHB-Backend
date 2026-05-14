package it.unife.sample.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "SOTTOSCRIVE")
@IdClass(SottoscrizioneId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sottoscrizione {
    @Id
    @ManyToOne
    @JoinColumn(name = "Numero_Abb")
    @NotNull
    private Abbonamento abbonamento;

    @Id
    @OneToOne
    @JoinColumn(name = "Id_Pagamento")
    @NotNull
    private Pagamento pagamento;

    @Id
    @ManyToOne
    @JoinColumn(name = "Cf")
    @NotNull
    private Atleta atleta;
}


