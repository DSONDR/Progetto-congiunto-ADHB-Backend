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
@Table(name = "attivita")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attivita {
    @Id
    @Column(nullable = false, unique = true)
    private Long codiceAtt;
    
    @NotBlank(message = "Nome obbligatorio")
    private String nomeAtt;
    @NotBlank(message = "Tipo obbligatorio")
    private String tipoEvento; // Es: Allenamento, Partita, Corso
    private String destinatario; // Es: Junior, Senior, Tutti
    private Double quotaBase;
    @NotNull(message = "Orario obbligatorio")
    private LocalDateTime dataOra; // Include data e ora di inizio, un record per ogni ripetizione attività
    private Integer maxPartecipanti;

    @NotNull(message = "Impianto obbligatorio")
    @ManyToOne
    @JoinColumn(name = "id_impianto")
    private Impianto impianto;

    // Relazione con le squadre (N:M)
    @ManyToMany(mappedBy = "attivita")
    private List<Squadra> squadre = new ArrayList<>();
}
