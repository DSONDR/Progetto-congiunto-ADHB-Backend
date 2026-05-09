package it.unife.sample.backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "abbonamento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Abbonamento {
    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long numeroAbb;
    
    @NotNull(message = "Inizio obbligatorio")
    private LocalDate dataInizio;
    @NotBlank(message = "Tipo obbligatorio")
    private String tipoAbb;
    
    private Integer ingressi;
    private String stato;
    private LocalDate dataScadenza;
}
