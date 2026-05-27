package it.unife.sample.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "ALLENATORE")
@PrimaryKeyJoinColumn(name = "Cf")
@Data
@EqualsAndHashCode(callSuper = true) // Importante per l'ereditarietà con Lombok
@NoArgsConstructor
@AllArgsConstructor
public class Allenatore extends Utente {
    @Column(name = "Grado")
    @NotNull(message = "Grado obbligatorio")
    private Integer grado;

}
