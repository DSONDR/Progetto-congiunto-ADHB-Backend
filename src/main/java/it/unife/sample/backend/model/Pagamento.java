package it.unife.sample.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "PAGAMENTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pagamento {
    @Id
    @Column(name = "Id_Pagamento", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPagamento;
    
    @Column(name = "Metodo_Pag")
    private String metodoPag; // Carta, Contanti, Bonifico
    
    @Column(name = "Data_Pag")
    @NotNull(message = "Data obbligatoria")
    private LocalDate dataPag;
    
    @Column(name = "Stato_Pag")
    @NotBlank(message = "Stato obbligatorio")
    private String statoPag;  // Confermato, In attesa
    
    @Column(name = "Importo")
    @NotNull(message = "Importo obbligatorio")
    private Double importo;
    
    @Column(name = "Fattura", unique = true)
    private String fattura;
}
