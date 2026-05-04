package it.unife.sample.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "squadra")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Squadra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    private String sport;
    private String campionato;

    // 1:N - Molte squadre hanno un solo allenatore
    @ManyToOne
    @JoinColumn(name = "allenatore_cf", referencedColumnName = "Cf")
    private Allenatore allenatore;

    // N:M - Relazione Compone
    @ManyToMany
    @JoinTable(
        name = "compone",
        joinColumns = @JoinColumn(name = "id_squadra"),
        inverseJoinColumns = @JoinColumn(name = "cf_atleta")
    )
    private List<Atleta> atleti = new ArrayList<>();

	//getter e setter di lombok...
}
