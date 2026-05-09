package it.unife.sample.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import it.unife.sample.backend.model.Utente;
import it.unife.sample.backend.model.Abbonamento;
import it.unife.sample.backend.model.Pagamento;

@Entity
@Data
@Table(name = "sottoscrive")
@NoArgsConstructor
@AllArgsConstructor
public class Sottoscrizione {
    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Cf-utente obbligatorio")
    @ManyToOne
    @JoinColumn(name = "cf")
    private Utente utente;

    @NotNull(message = "Id-abbonamento obbligatorio")
    @ManyToOne
    @JoinColumn(name = "id_abbonamento")
    private Abbonamento abbonamento;

    @NotNull(message = "Id-pagamento obbligatorio")
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "id_pagamento")
    private Pagamento pagamento;
}


