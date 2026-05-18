package it.unife.sample.backend.dto.response;

import lombok.Data;

/**
 * DTO di risposta per le squadre.
 * Incapsula l'entità evitando di esporre direttamente la lista 
 * completa degli atleti (recuperabile tramite apposito endpoint)
 * e le credenziali dell'allenatore.
 */
@Data
public class SquadraResponseDTO {
    
    private Long id;
    private String nome;
    private String sport;
    private String campionato;
    
    private String allenatoreCf;
    private String nomeAllenatore; // Unione di nome e cognome per praticità lato client
}
