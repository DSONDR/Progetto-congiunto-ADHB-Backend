package it.unife.sample.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "allenatori")
@Data
@EqualsAndHashCode(callSuper = true) // Importante per l'ereditarietà con Lombok
@NoArgsConstructor
@AllArgsConstructor
public class Allenatore extends Utente {
    
    private Integer grado;

}
