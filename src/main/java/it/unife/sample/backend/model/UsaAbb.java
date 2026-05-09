package it.unife.sample.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@Table(name = "usa_abbonamento")
@NoArgsConstructor
@AllArgsConstructor
public class UsaAbb {
    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Id-sottoscrizione obbligatorio")
    @ManyToOne
    @JoinColumn(name = "id_sottoscrizione")
    private Sottoscrizione sottoscrizione;

    @NotNull(message = "id-attività obbligatorio")
    @ManyToOne
    @JoinColumn(name = "id_attivita")
    private Attivita attivita;

    @NotNull(message = "Orario obbligatorio")
    private LocalDateTime dataUtilizzo = LocalDateTime.now();
}
