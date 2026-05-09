package it.unife.sample.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Nome obbligatorio")
    private String nome;
    @NotBlank(message = "Sport obbligatorio")
    private String sport;
    private String campionato;

    // 1:N - Molte squadre hanno un solo allenatore
    @ManyToOne
    @JoinColumn(name = "allenatore_cf", referencedColumnName = "cf")
    private Allenatore allenatore;

    // N:M - Relazione Compone
    @ManyToMany
    @JoinTable(
        name = "compone",
        joinColumns = @JoinColumn(name = "id_squadra"),
        inverseJoinColumns = @JoinColumn(name = "cf_atleta")
    )
    private List<Atleta> atleti = new ArrayList<>();
    
    // N:M - Relazione Partecipa (Squadra partecipa ad Attività)
    @ManyToMany
    @JoinTable(
        name = "partecipa",
        joinColumns = @JoinColumn(name = "id_squadra"),
        inverseJoinColumns = @JoinColumn(name = "id_attivita")
    )
    private List<Attivita> attivita = new ArrayList<>();
}
