package it.unife.sample.backend.repository; // Cartella repository

// Devi importare il Model perché si trova in un'altra cartella (package)
import it.unife.sample.backend.model.Assistenza;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssistenzaRepository extends JpaRepository<Assistenza, Long> {
    
    // Recupera i record filtrando per Stato
    // Usato da AssistenzaService.findByStato() e da ImpiantoService.findByStato()
    List<Assistenza> findByStato(String stato);
    
    // Recupera i record filtrando per UtenteCf
    // Usato da SottoscrizioneService.sottoscrivi() e da PagamentoService.getStoricoTransazioni() 
    // e da AssistenzaService.findByUtente() e da CertificatoMedicoService.findByUtenteCf() 
    // e da IscrizioneService.hasValidCertificato() e da IscrizioneService.checkSovrapposizioni() 
    // e da IscrizioneService.getStoricoUtente() e da IscrizioneService.getStoricoUsiAbbonamentoUtente()
    List<Assistenza> findByUtenteCf(String cf);
    
    // Recupera i record filtrando per AssistenteCf
    // Usato da AssistenzaService.findByAssistente()
    List<Assistenza> findByAssistenteCf(String cf);

}
