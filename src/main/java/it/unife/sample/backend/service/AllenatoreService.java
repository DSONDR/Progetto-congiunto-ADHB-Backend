package it.unife.sample.backend.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unife.sample.backend.model.Allenatore;
import it.unife.sample.backend.repository.AllenatoreRepository;

@Service
public class AllenatoreService {

    @Autowired
    private AllenatoreRepository repository;

    //Crud per l'id univoco
    public List<Allenatore> findAll() {
        return repository.findAll();
    }

    public Optional<Allenatore> findById(String id) {
        return repository.findById(id);
    }

    public Allenatore save(Allenatore allenatore) {
        //qui da aggiungere controlli sui gradi, ma se servono chi li chiede?
        return repository.save(allenatore);
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }
    
    //Trova allenatori con certo grado
    public List<Allenatore> findByGrado(String grado){
    	return repository.findByGrado(grado);    
    }
    
    //Trova gli allenatori tra due gradi
    public List<Allenatore> findByGradoBetween(Integer min, Integer max){
        if(min>max){
             return repository.findByGradoBetween(max, min);
        }   
        return repository.findByGradoBetween(min, max);
    }  
}
