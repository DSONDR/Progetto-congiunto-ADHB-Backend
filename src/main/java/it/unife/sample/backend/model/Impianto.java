package it.unife.sample.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "IMPIANTO")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Impianto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Impianto")
    private Long id;
    
    @Column(name = "Nome_I")
    private String nome_i;
    @Column(name = "Tipo_Impianto")
    private String tipo_impianto;
    @Column(name = "Stato_I")
    private String stato_i;
    @Column(name = "Omologazione")
    private String omologazione;

    // N:1 - un impianto può essere utilizzato da molte attività
    @OneToMany(mappedBy = "impianto")
    private List<Attivita> attivita = new ArrayList<>();

    // Relazione con sponsorizzazioni
    @OneToMany(mappedBy = "impianto")
    private List<Sponsorizzazione> sponsorizzazioni = new ArrayList<>();
}
