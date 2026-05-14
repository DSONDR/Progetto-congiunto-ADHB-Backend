package it.unife.sample.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SPONSOR")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sponsor {
    @Id
    @Column(name = "P_IVA", length = 11)
    private String pIva;
    
    @Column(name = "Azienda")
    private String azienda;
    @Column(name = "Contatto")
    private String contatto;

    // Relazioni
    @OneToMany(mappedBy = "sponsor")
    private List<Sponsorizzazione> sponsorizzazioni = new ArrayList<>();

    // N:M - Sponsor sponsorizza molte squadre
    @ManyToMany
    @JoinTable(
            name = "SPONSORIZZAZIONE",
            joinColumns = @JoinColumn(name = "P_IVA"),
            inverseJoinColumns = @JoinColumn(name = "Id_Squadra"),
            foreignKey = @ForeignKey(name = "FK_SPONSOR_SQUADRA")
    )
    private List<Squadra> squadre = new ArrayList<>();

    // N:M - Sponsor sponsorizza molti impianti
    @ManyToMany
    @JoinTable(
            name = "SPONSORIZZAZIONE",
            joinColumns = @JoinColumn(name = "P_IVA"),
            inverseJoinColumns = @JoinColumn(name = "Id_Impianto"),
            foreignKey = @ForeignKey(name = "FK_SPONSOR_IMPIANTO")
    )
    private List<Impianto> impianti = new ArrayList<>();
}
