package it.unife.sample.backend.service;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unife.sample.backend.model.Utente;
import it.unife.sample.backend.repository.UtenteRepository;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
public class SquadraService {
     @Autowired
    private SquadraRepository sqrepo;

    @Autowired
    private AtletaRepository atlrepo;

    @Autowired
    private AllenatoreRepository allrepo;    

    public List<Squadra> findAll() {
        return sqrepo.findAll();
    }

    public Optional<Squadra> findById(Long id) {
        return sqrepo.findById(id);
    }

    public Utente save(Squadra squadra) {
        return sqrepo.save(squadra);
    }

    public void deleteById(Long id) {
        sqrepo.deleteById(id);
    }
    
    //FK di squadra è il cf allenatore
    public List<Squadra> findByAllenatoreCf(String cf){
        return sqrepo.findByAllenatoreCf(cf);
    } 
	
    //In compone ho cf atleta, e in model lo riporto su squadra, qui lista squadre di un atleta
    publiv List<Squadra> findByAtletaCf(String cf){
        return sqrepo.findByAtletaCf(cf);
    }
    
    //Aggiungo atleta a una squadra
    public void aggiungiAtletaASquadra(Long squadraId, String atletaCf) {
        Squadra squadra = sqrepo.findById(squadraId).orElseThrow();
        Atleta atleta = atlRepo.findById(atletaCf).orElseThrow();
        
        squadra.getAtleti().add(atleta); // Uniamo i due oggetti
        sqRepo.save(squadra);       // Salviamo la relazione
    }
    
    //Listo atleti di una squadra
    public List<Atleta> getAtletiBySquadra(Long squadraId) {
        return sqrepo.findById(squadraId)
            .map(Squadra::getAtleti)
            .orElse(new ArrayList<>());
    }

	public List<Atleta> getAtletiScadutiBySquadra(Long squadraId) {
		Squadra squadra = sqrepo.findById(squadraId)
		        .orElseThrow(() -> new RuntimeException("Squadra non trovata"));
		
		return squadra.getAtleti().stream()
		        .filter(atleta -> atleta.getScadenzaVisita() != null && 
		                          atleta.getScadenzaVisita().isBefore(LocalDate.now()))
		        .collect(Collectors.toList());
	}
}
