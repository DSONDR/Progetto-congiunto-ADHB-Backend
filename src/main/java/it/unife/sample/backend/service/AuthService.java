package it.unife.sample.backend.service;

import it.unife.sample.backend.dto.request.LoginRequestDTO;
import it.unife.sample.backend.dto.request.RegisterRequestDTO;
import it.unife.sample.backend.dto.response.LoginResponseDTO;
import it.unife.sample.backend.dto.response.UserResponseDTO;
import it.unife.sample.backend.model.Atleta;
import it.unife.sample.backend.model.Utente;
import it.unife.sample.backend.repository.UtenteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UtenteRepository utenteRepository;

    //REGISTRAZIONE [Solo degli atleti, gli altri non si registrano da soli]
    public UserResponseDTO register(RegisterRequestDTO dto) {

        //Controlli unicità
        if(utenteRepository.existsById(dto.getCf())) {
            throw new RuntimeException("Codice fiscale già registrato");
        }
        if(utenteRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email già registrata");
        }
        if(utenteRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username già esistente");
        }

        //Creo atleta

        Atleta atleta = new Atleta();

        atleta.setCf(dto.getCf());
        atleta.setNome(dto.getNome());
        atleta.setCognome(dto.getCognome());
        atleta.setGenere(dto.getGenere());
        atleta.setDataNascita(dto.getDataNascita());
        atleta.setCittaResidenza(dto.getCittaResidenza());
        atleta.setUsername(dto.getUsername());
        atleta.setEmail(dto.getEmail());
	//Ruolo applicativo
        atleta.setTipoIscritto("ATLETA");

        //Temporaneo
        atleta.setPassword(dto.getPassword());
        
        //Campi opzionali
        atleta.setStipendio(null);
        atleta.setEmissioneVisita(null);
        atleta.setScadenzaVisita(null);
        atleta.setMedicoRiferimento(null);

        //Salvo nel DB
        atleta = utenteRepository.save(atleta);
        //E infine rispondo
        return mapUserResponse(atleta);
    }

    //LOGIN [stavolta per tutti i tipi]
    public LoginResponseDTO login(LoginRequestDTO dto) {

        Utente utente = utenteRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        //Controllo password
        if(!utente.getPassword().equals(dto.getPassword())) {
            throw new RuntimeException("Password errata");
        }

        LoginResponseDTO response = new LoginResponseDTO();

        response.setCf(utente.getCf());
        response.setNome(utente.getNome());
        response.setCognome(utente.getCognome());
        response.setEmail(utente.getEmail());
        response.setUsername(utente.getUsername());
        response.setTipoIscritto(utente.getTipoIscritto());
        response.setMessaggio("Login effettuato");
        return response;
    }

    //LOGOUT, vuoto perchè solo logico, su frontend
    public String logout() {
        return "Logout effettuato";
    }

    //CANCELLA ACCOUNT
    public void deleteAccount(String cf) {

        Utente utente = utenteRepository.findById(cf)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        utenteRepository.delete(utente);
    }
    
    //Mapper interno, chiamato alla creazione dell'atleta
    private UserResponseDTO mapUserResponse(Utente utente) {

        UserResponseDTO dto = new UserResponseDTO();

        dto.setCf(utente.getCf());

        dto.setNome(utente.getNome());
        dto.setCognome(utente.getCognome());

        dto.setGenere(utente.getGenere());

        dto.setDataNascita(utente.getDataNascita());

        dto.setCittaResidenza(
                utente.getCittaResidenza());

        dto.setUsername(utente.getUsername());

        dto.setEmail(utente.getEmail());

        dto.setTipoIscritto(
                utente.getTipoIscritto());

        return dto;
    }
}
