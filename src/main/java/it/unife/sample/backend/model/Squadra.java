package it.unife.sample.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SQUADRA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Squadra {
    @Id
    @Column(name = "Id_Squadra", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "Nome_Sq")
    @NotBlank(message = "Nome obbligatorio")
    private String nome;
    @Column(name = "Sport")
    @NotBlank(message = "Sport obbligatorio")
    private String sport;
    @Column(name = "Campionato")
    private String campionato;

    // 1:N - Molte squadre hanno un solo allenatore
    @ManyToOne
    @JoinColumn(name = "Allenatore_Cf", referencedColumnName = "Cf")
    private Allenatore allenatore;

    // N:M - Molti atleti in molte squadre via ComposizioneSquadra
    @ManyToMany
    @JoinTable(
            name = "COMPOSIZIONE_SQUADRA",
            joinColumns = @JoinColumn(name = "Id_Squadra"),
            inverseJoinColumns = @JoinColumn(name = "Cf_Atleta")
    )
    private List<Atleta> atleti = new ArrayList<>();
}
