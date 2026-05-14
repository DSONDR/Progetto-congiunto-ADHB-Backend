package it.unife.sample.backend.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "ASSISTENZA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Assistenza {
    @Id
    @Column(name = "Id_Ticket")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTicket;
    
    private String oggetto;
    @Column(name = "Tipo_Ass")
    private String tipoAss;
    private String stato;
    private Integer soddisfazione;

    // 1:N - Molte assistenze possono essere richieste da un solo utente
    @ManyToOne
    @JoinColumn(name = "Cf_Utente")
    private Utente utente;

     // 1:N - Molte assistenze possono essere risolte da un solo utente
    @ManyToOne
    @JoinColumn(name = "Cf_Assistente")
    private Utente assistente;
    
    //getter e setter di lombok...
}
