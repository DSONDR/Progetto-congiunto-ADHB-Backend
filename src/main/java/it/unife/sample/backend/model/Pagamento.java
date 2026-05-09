package it.unife.sample.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "pagamento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pagamento {
    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPagamento;
    
    @NotNull(message = "Importo obbligatorio")
    private Double importo;
    @NotNull(message = "Orario obbligatorio")
    private LocalDateTime dataPag = LocalDateTime.now();
    private String metodoPag; // Carta, Contanti, Bonifico
    @NotBlank(message = "Stato obbligatorio")
    private String statoPag;  // Confermato, In attesa
}
