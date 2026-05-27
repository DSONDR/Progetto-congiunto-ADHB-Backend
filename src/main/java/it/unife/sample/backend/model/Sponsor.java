package it.unife.sample.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "SPONSOR")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sponsor {
    @Id
    @Column(name = "P_IVA", length = 11)
    private String partitaIva;
    
    @Column(name = "Azienda")
    private String azienda;
    @Column(name = "Contatto")
    private String contatto;

    // Relazioni
    @OneToMany(mappedBy = "sponsor")
    @JsonIgnore		//Per evitare ricorsione nei rapporti bidirezionali
    private List<Sponsorizzazione> sponsorizzazioni = new ArrayList<>();

}
