package it.unife.sample.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "ATTIVITA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attivita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Codice_Att", nullable = false, unique = true)
    private Long codiceAtt;
    
    @Column(name = "Nome_Att")
    @NotBlank(message = "Nome obbligatorio")
    private String nomeAtt;
    @Column(name = "Tipo_Evento")
    @NotBlank(message = "Tipo obbligatorio")
    private String tipoEvento; // Es: Allenamento, Partita, Corso
    @Column(name = "Destinatario")
    private String destinatario; // Es: Junior, Senior, Tutti
    @Column(name = "Quota_Base")
    private Double quotaBase;
    @Column(name = "Max_Partecipanti")
    private Integer maxPartecipanti;

    @NotNull(message = "Istruttore obbligatorio")
    @ManyToOne
    @JoinColumn(name = "Cf_Istruttore")
    private Istruttore istruttore;

    @NotNull(message = "Impianto obbligatorio")
    @ManyToOne
    @JoinColumn(name = "Id_Impianto")
    private Impianto impianto;

    @OneToMany(mappedBy = "attivita", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DateAtt> dateAtts = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "ATTIVITA_SQUADRA",
        joinColumns = @JoinColumn(name = "Codice_Att"),
        inverseJoinColumns = @JoinColumn(name = "Id_Squadra")
    )
    private List<Squadra> squadreAderenti = new ArrayList<>();
}
