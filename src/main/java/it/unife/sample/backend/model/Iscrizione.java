package it.unife.sample.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@Table(name = "iscr_singola")
@NoArgsConstructor
@AllArgsConstructor
public class Iscrizione {
    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Cf-utente obbligatorio")
    @ManyToOne
    @JoinColumn(name = "cf")
    private Utente utente;

    @NotNull(message = "codiceAtt obbligatorio")
    @ManyToOne
    @JoinColumn(name = "codiceAtt")
    private Attivita attivita;

    @NotNull(message = "Id-pagamento obbligatorio")
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "id_pagamento")
    private Pagamento pagamento;

    private LocalDateTime dataRegistrazione;
}
