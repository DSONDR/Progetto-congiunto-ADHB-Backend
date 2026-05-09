package it.unife.sample.backend.service;

import java.util.*;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.unife.sample.backend.model.Iscrizione;
import it.unife.sample.backend.model.Utente;
import it.unife.sample.backend.model.Attivita;
import it.unife.sample.backend.model.Pagamento;
import it.unife.sample.backend.repository.IscrizioneRepository;
import it.unife.sample.backend.repository.PagamentoRepository;

@Service
public class IscrizioneService {
    @Autowired
    private IscrizioneRepository iscRepo;
    @Autowired
    private PagamentoRepository pagRepo;

    // CRUD base
    public List<Iscrizione> findAll() {
        return iscRepo.findAll();
    }

    public Optional<Iscrizione> findById(Long id) {
        return iscRepo.findById(id);
    }

    public Iscrizione save(Iscrizione iscrizione) {
        return iscRepo.save(iscrizione);
    }

    public void deleteById(Long id) {
        iscRepo.deleteById(id);
    }
    
    //Iscrizione
    @Transactional
    public Iscrizione iscriviUtente(Utente utente, Attivita attivita, Double importo, String metodo) {
        // 1. Creo e salvo il pagamento
        Pagamento p = new Pagamento();
        p.setImporto(importo);
        p.setMetodoPag(metodo);
        p.setStatoPag("CONFERMATO");
        p = pagRepo.save(p);

        // 2. Creo l'iscrizione legando i tre pezzi
        Iscrizione i = new Iscrizione();
        i.setUtente(utente);
        i.setAttivita(attivita);
        i.setPagamento(p);
        i.setDataRegistrazione(LocalDateTime.now());
        
        return iscRepo.save(i);
    }

    //Cancella iscrizione
    @Transactional
    public void cancellaIscrizione(Long idIscrizione) {
        Iscrizione isc = iscRepo.findById(idIscrizione).orElseThrow(() -> new RuntimeException("Iscrizione non trovata"));
        // Qui aggiungere una logica: "Puoi cancellarti solo 24h prima"
        if (isc.getAttivita().getDataOra().isBefore(LocalDateTime.now().plusHours(24))) {
            throw new RuntimeException("Troppo tardi per cancellarsi!");	//Qui non .orElseThrow, perchè req valida, ma op vietata
        }
        
        iscRepo.delete(isc);
    }
    
    
    // Visualizza tutte le attività a cui si è iscritto un utente
    public List<Iscrizione> getStoricoUtente(String cf) {
        return iscRepo.findByUtenteCf(cf);
    }

    
}
