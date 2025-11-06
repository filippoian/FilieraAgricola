package it.unicam.cs.ids2425.FilieraAgricola.service;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.AccreditaProduttoreRequest;
import it.unicam.cs.ids2425.FilieraAgricola.model.*;
import it.unicam.cs.ids2425.FilieraAgricola.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GestoreService {

    @Autowired
    UtenteRepository utenteRepository;

    @Autowired
    RoleRepository roleRepository;

    // --- Repository Aggiunti ---
    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    FilieraPointRepository filieraPointRepository;

    @Autowired
    ActorProfileProduttoreRepository actorProfileProduttoreRepository;


    /**
     * Rimuove o mantiene il vecchio metodo di accredito (solo ruolo)
     * per compatibilità temporanea, se necessario.
     */
    @Transactional
    public void accreditaRuoloBase(Long userId, ERole ruoloNome) {
        Utente utente = utenteRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Errore: Utente non trovato con id " + userId));
        Role ruolo = roleRepository.findByName(ruoloNome)
                .orElseThrow(() -> new RuntimeException("Errore: Ruolo " + ruoloNome + " non trovato."));
        utente.getRoles().add(ruolo);
        utenteRepository.save(utente);
    }

    /**
     * Nuovo metodo per l'accredito completo di un PRODUTTORE,
     * come da specifiche.
     */
    @Transactional
    public void accreditaProduttore(Long userId, AccreditaProduttoreRequest request) {
        // 1. Trova Utente e UserProfile
        Utente utente = utenteRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato con id: " + userId));

        UserProfile userProfile = userProfileRepository.findByUtenteId(userId)
                .orElseThrow(() -> new RuntimeException("Profilo utente non trovato per id: " + userId));

        // 2. Crea il FilieraPoint (l'azienda agricola)
        FilieraPoint point = new FilieraPoint();
        point.setNomePunto(request.getNomePunto());
        point.setDescrizione(request.getDescrizionePunto());
        point.setIndirizzo(request.getIndirizzoPunto());
        point.setLatitudine(request.getLatitudine());
        point.setLongitudine(request.getLongitudine());
        point.setTipo(TipoFilieraPoint.AZIENDA_AGRICOLA); // Tipo hardcoded
        point.setUtente(utente); // Associa l'utente al punto
        FilieraPoint savedPoint = filieraPointRepository.save(point);

        // 3. Crea l'ActorProfile_Produttore
        ActorProfile_Produttore profiloProduttore = new ActorProfile_Produttore();
        profiloProduttore.setUserProfile(userProfile);
        profiloProduttore.setFilieraPoint(savedPoint);
        profiloProduttore.setRagioneSociale(request.getRagioneSociale());
        profiloProduttore.setPartitaIva(request.getPartitaIva());
        profiloProduttore.setDescrizioneAzienda(request.getDescrizioneAzienda());
        actorProfileProduttoreRepository.save(profiloProduttore);

        // 4. Assegna il Ruolo
        Role ruolo = roleRepository.findByName(ERole.ROLE_PRODUTTORE)
                .orElseThrow(() -> new RuntimeException("Errore: Ruolo PRODUTTORE non trovato."));
        utente.getRoles().add(ruolo);
        utenteRepository.save(utente);
    }
}