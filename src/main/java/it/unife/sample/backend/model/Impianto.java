package it.unife.sample.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "IMPIANTO")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Impianto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Impianto")
    private Long id;
    
    @Column(name = "Nome_I")
    private String nome;
    
    @Column(name = "Tipo_Impianto")
    private String tipoImpianto;
    
    @Column(name = "Stato_I")
    private String stato;
    
    @Column(name = "Omologazione")
    private String omologazione;

    // N:1 - un impianto può essere utilizzato da molte attività
    @OneToMany(mappedBy = "impianto")
    @JsonIgnore		//Per evitare ricorsione nei rapporti bidirezionali
    private List<Attivita> attivita = new ArrayList<>();

    // Relazione con sponsorizzazioni
    @OneToMany(mappedBy = "impianto")
    @JsonIgnore		//Per evitare ricorsione nei rapporti bidirezionali
    private List<Sponsorizzazione> sponsorizzazioni = new ArrayList<>();
}
