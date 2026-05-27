package it.unife.sample.backend.repository; // Cartella repository

// Devi importare il Model perché si trova in un'altra cartella (package)
import it.unife.sample.backend.model.Iscrizione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

import it.unife.sample.backend.model.IscrSingolaId;

@Repository
public interface IscrizioneRepository extends JpaRepository<Iscrizione, IscrSingolaId> {

    // Recupera i record filtrando per UtenteCf
    // Usato da SottoscrizioneService.sottoscrivi() e da PagamentoService.getStoricoTransazioni()
    // e da AssistenzaService.findByUtente() e da CertificatoMedicoService.findByUtenteCf()
    // e da IscrizioneService.hasValidCertificato() e da IscrizioneService.checkSovrapposizioni()
    // e da IscrizioneService.getStoricoUtente() e da IscrizioneService.getStoricoUsiAbbonamentoUtente()
    List<Iscrizione> findByUtenteCf(String cf);

    // Recupera i record filtrando per AttivitaCodiceAtt
    // Usato da IscrizioneService.getIscrittiByAttivita() e da PagamentoService.getPagamentiPerAttivita()
    List<Iscrizione> findByAttivitaCodiceAtt(Long idAttivita);

    // Conta il numero di record in base a AttivitaCodiceAtt
    // Usato da IscrizioneService.isPostoDisponibile() e da AttivitaService.getNumeroIscritti()
    long countByAttivitaCodiceAtt(Long idAttivita);
}
