package it.unicam.cs.ids2425.FilieraAgricola.service;

import it.unicam.cs.ids2425.FilieraAgricola.model.ERole;
import it.unicam.cs.ids2425.FilieraAgricola.model.Role;
import it.unicam.cs.ids2425.FilieraAgricola.model.Utente;
import it.unicam.cs.ids2425.FilieraAgricola.repository.RoleRepository;
import it.unicam.cs.ids2425.FilieraAgricola.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GestoreService {

    @Autowired
    UtenteRepository utenteRepository;

    @Autowired
    RoleRepository roleRepository;

    @Transactional // Assicura che l'operazione sia atomica
    public void accreditaUtente(Long userId, ERole ruoloNome) {
        // 1. Trova l'utente
        Utente utente = utenteRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Errore: Utente non trovato con id " + userId));

        // 2. Trova il ruolo (DataInitializer si è assicurato che esista)
        Role ruolo = roleRepository.findByName(ruoloNome)
                .orElseThrow(() -> new RuntimeException("Errore: Ruolo " + ruoloNome + " non trovato."));

        // 3. Aggiungi il nuovo ruolo al Set di ruoli dell'utente
        utente.getRoles().add(ruolo);

        // 4. Salva l'utente aggiornato
        utenteRepository.save(utente);

        // N.B. Come da specifica, qui in futuro dovremmo anche creare
        // l'ActorProfile_... associato, ma per ora assegniamo solo il ruolo.
    }
}
