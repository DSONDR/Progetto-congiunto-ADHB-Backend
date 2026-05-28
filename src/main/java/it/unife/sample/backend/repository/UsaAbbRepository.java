package it.unife.sample.backend.repository; // Cartella repository

// Devi importare il Model perché si trova in un'altra cartella (package)
import it.unife.sample.backend.model.UsaAbb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

import it.unife.sample.backend.model.UsaAbbId;

@Repository
public interface UsaAbbRepository extends JpaRepository<UsaAbb, UsaAbbId> {
    // Conta il numero di record in base a AttivitaCodiceAtt
    // Usato da IscrizioneService.isPostoDisponibile() e da
    // AttivitaService.getNumeroIscritti()
    long countByAttivitaCodiceAtt(Long idAttivita);

    // Conta il numero di record in base a AbbonamentoNumeroAbb
    // Usato dal sistema per logiche interne di gestione
    long countByAbbonamentoNumeroAbb(Long numeroAbb);

    // Recupera i record filtrando per AbbonamentoNumeroAbb
    // Usato da SottoscrizioneService per cancellare le iscrizioni quando si disdice l'abbonamento
    List<UsaAbb> findByAbbonamentoNumeroAbb(Long numeroAbb);

    // Recupera i record filtrando per UtenteCf
    // Usato da SottoscrizioneService.sottoscrivi() e da
    // PagamentoService.getStoricoTransazioni()
    // e da AssistenzaService.findByUtente() e da
    // CertificatoMedicoService.findByUtenteCf()
    // e da IscrizioneService.hasValidCertificato() e da
    // IscrizioneService.checkSovrapposizioni()
    // e da IscrizioneService.getStoricoUtente() e da
    // IscrizioneService.getStoricoUsiAbbonamentoUtente()
    List<UsaAbb> findByUtenteCf(String cf);

    // Recupera i record filtrando per AttivitaCodiceAtt
    // Usato da IscrizioneService.getIscrittiByAttivita() e da
    // PagamentoService.getPagamentiPerAttivita()
    List<UsaAbb> findByAttivitaCodiceAtt(Long idAttivita);
}
