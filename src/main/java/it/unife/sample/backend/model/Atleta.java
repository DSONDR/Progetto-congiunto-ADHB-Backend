package it.unife.sample.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ATLETA")
@PrimaryKeyJoinColumn(name = "Cf")
@Data
@EqualsAndHashCode(callSuper = true)
public class Atleta extends Utente {

}
