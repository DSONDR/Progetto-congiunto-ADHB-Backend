package it.unife.sample.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "impianto")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Impianto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome_i;
    private String tipo_impianto;
    private String omologazione;
    private String stato_i;

    // N:1 - un impianto può essere utilizzato da molte attività
    @OneToMany(mappedBy = "impianto")
    private List<Attivita> attivita = new ArrayList<>();


    // N:M - Relazione Sponsorizza_Im
    @ManyToMany
    @JoinTable(
        name = "sponsorizza_im",
        joinColumns = @JoinColumn(name = "id_impianto"),
        inverseJoinColumns = @JoinColumn(name = "p_iva_sponsor")
    )
    private List<Sponsor> sponsor = new ArrayList<>();

	//getter e setter di lombok...
}
