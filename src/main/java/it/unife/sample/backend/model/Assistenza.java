package it.unife.sample.backend.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "assistenza")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Assistenza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTicket;
    
    private String oggetto;
    private String tipoAss;
    private String stato;
    private String soddisfazione;

    // 1:N - Molte assistenze possono essere richieste da un solo utente
    @ManyToOne
    @JoinColumn(name = "utente_cf", referencedColumnName = "Cf")
    private Utente utente;

     // 1:N - Molte assistenze possono essere risolte da un solo utente
    @ManyToOne
    @JoinColumn(name = "admin_cf", referencedColumnName = "Cf")
    private Utente admin;

	//getter e setter di lombok...
}
