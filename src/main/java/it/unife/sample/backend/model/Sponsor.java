package it.unife.sample.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Sponsor")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Sponsor {
    @Id
    private String pIva;
    
    private String azienda;
    private String contatto;
    private String inizioContratto;
    private String fineContratto

    // N:M - Relazione Sponsorizza_Sq
    @ManyToMany(mappedBy = "sponsors")
    private List<Squadra> squadra = new ArrayList<>();

    // N:M - Relazione Sponsorizza_Im
    @ManyToMany(mappedBy = "sponsors")
    private List<Impianto> impianto = new ArrayList<>();

	//getter e setter di lombok...
}
