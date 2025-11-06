package it.unicam.cs.ids2425.FilieraAgricola.service;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.AccreditaDistributoreRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.request.AccreditaProduttoreRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.request.AccreditaRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.request.AccreditaTrasformatoreRequest;
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
    @Autowired
    UserProfileRepository userProfileRepository;
    @Autowired
    FilieraPointRepository filieraPointRepository;
    @Autowired
    ContentSubmissionRepository submissionRepository;

    // Repository per i profili attore
    @Autowired
    ActorProfileProduttoreRepository actorProfileProduttoreRepository;
    @Autowired
    ActorProfileTrasformatoreRepository actorProfileTrasformatoreRepository;
    @Autowired
    ActorProfileDistributoreRepository actorProfileDistributoreRepository;


    /**
     * Accredita un utente con un ruolo "base" (es. CURATORE, ANIMATORE, GESTORE)
     * che non richiede un profilo attore complesso o un FilieraPoint.
     */
    @Transactional
    public void accreditaRuoloBase(Long userId, ERole ruoloNome) {
        // Impedisce l'assegnazione di ruoli complessi tramite questo endpoint
        if (ruoloNome == ERole.ROLE_PRODUTTORE ||
                ruoloNome == ERole.ROLE_TRASFORMATORE ||
                ruoloNome == ERole.ROLE_DISTRIBUTORE) {
            throw new IllegalArgumentException("Usare l'endpoint di accredito specifico per questo ruolo.");
        }

        Utente utente = utenteRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Errore: Utente non trovato con id " + userId));
        Role ruolo = roleRepository.findByName(ruoloNome)
                .orElseThrow(() -> new RuntimeException("Errore: Ruolo " + ruoloNome + " non trovato."));

        utente.getRoles().add(ruolo);
        utenteRepository.save(utente);
    }

    /**
     * Accredito completo di un PRODUTTORE.
     * Crea il profilo e approva automaticamente il FilieraPoint.
     */
    @Transactional
    public void accreditaProduttore(Long userId, AccreditaProduttoreRequest request) {
        // 1. Trova Utente e UserProfile
        Utente utente = utenteRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato con id: " + userId));

        UserProfile userProfile = userProfileRepository.findByUtenteId(userId)
                .orElseThrow(() -> new RuntimeException("Profilo utente non trovato per id: " + userId));

        // 2. Crea il FilieraPoint (Azienda Agricola)
        FilieraPoint point = creaEApprovaFilieraPoint(
                request.getNomePunto(),
                request.getDescrizionePunto(),
                request.getIndirizzoPunto(),
                request.getLatitudine(),
                request.getLongitudine(),
                TipoFilieraPoint.AZIENDA_AGRICOLA, // Tipo specifico
                utente
        );

        // 3. Crea l'ActorProfile_Produttore
        ActorProfile_Produttore profiloProduttore = new ActorProfile_Produttore();
        profiloProduttore.setUserProfile(userProfile);
        profiloProduttore.setFilieraPoint(point);
        profiloProduttore.setRagioneSociale(request.getRagioneSociale());
        profiloProduttore.setPartitaIva(request.getPartitaIva());
        profiloProduttore.setDescrizioneAzienda(request.getDescrizioneAzienda());
        actorProfileProduttoreRepository.save(profiloProduttore);

        // 4. Assegna il Ruolo
        assegnaRuolo(utente, ERole.ROLE_PRODUTTORE);
    }

    /**
     * Accredito completo di un TRASFORMATORE.
     * Crea il profilo e approva automaticamente il FilieraPoint.
     */
    @Transactional
    public void accreditaTrasformatore(Long userId, AccreditaTrasformatoreRequest request) {
        // 1. Trova Utente e UserProfile
        Utente utente = utenteRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato con id: " + userId));

        UserProfile userProfile = userProfileRepository.findByUtenteId(userId)
                .orElseThrow(() -> new RuntimeException("Profilo utente non trovato per id: " + userId));

        // 2. Crea il FilieraPoint (Laboratorio)
        FilieraPoint point = creaEApprovaFilieraPoint(
                request.getNomePunto(),
                request.getDescrizionePunto(),
                request.getIndirizzoPunto(),
                request.getLatitudine(),
                request.getLongitudine(),
                TipoFilieraPoint.LABORATORIO_TRASFORMAZIONE, // Tipo specifico
                utente
        );

        // 3. Crea l'ActorProfile_Trasformatore
        ActorProfile_Trasformatore profiloTrasformatore = new ActorProfile_Trasformatore();
        profiloTrasformatore.setUserProfile(userProfile);
        profiloTrasformatore.setFilieraPoint(point);
        profiloTrasformatore.setRagioneSociale(request.getRagioneSociale());
        profiloTrasformatore.setPartitaIva(request.getPartitaIva());
        profiloTrasformatore.setDescrizioneLaboratorio(request.getDescrizioneLaboratorio());
        actorProfileTrasformatoreRepository.save(profiloTrasformatore);

        // 4. Assegna il Ruolo
        assegnaRuolo(utente, ERole.ROLE_TRASFORMATORE);
    }

    /**
     * Accredito completo di un DISTRIBUTORE.
     * Crea il profilo e approva automaticamente il FilieraPoint.
     */
    @Transactional
    public void accreditaDistributore(Long userId, AccreditaDistributoreRequest request) {
        // 1. Trova Utente e UserProfile
        Utente utente = utenteRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato con id: " + userId));

        UserProfile userProfile = userProfileRepository.findByUtenteId(userId)
                .orElseThrow(() -> new RuntimeException("Profilo utente non trovato per id: " + userId));

        // 2. Crea il FilieraPoint (Punto Vendita/Magazzino)
        FilieraPoint point = creaEApprovaFilieraPoint(
                request.getNomePunto(),
                request.getDescrizionePunto(),
                request.getIndirizzoPunto(),
                request.getLatitudine(),
                request.getLongitudine(),
                TipoFilieraPoint.PUNTO_VENDITA, // Tipo specifico (o MERCATO)
                utente
        );

        // 3. Crea l'ActorProfile_Distributore
        ActorProfile_Distributore profiloDistributore = new ActorProfile_Distributore();
        profiloDistributore.setUserProfile(userProfile);
        profiloDistributore.setFilieraPoint(point);
        profiloDistributore.setRagioneSociale(request.getRagioneSociale());
        profiloDistributore.setPartitaIva(request.getPartitaIva());
        profiloDistributore.setInfoLogistica(request.getInfoLogistica());
        actorProfileDistributoreRepository.save(profiloDistributore);

        // 4. Assegna il Ruolo
        assegnaRuolo(utente, ERole.ROLE_DISTRIBUTORE);
    }


    // --- Metodi Helper ---

    /**
     * Metodo helper per creare, salvare e approvare automaticamente un FilieraPoint.
     * L'approvazione è automatica perché l'accredito è fatto da un GESTORE.
     */
    private FilieraPoint creaEApprovaFilieraPoint(String nome, String desc, String indirizzo,
                                                  java.math.BigDecimal lat, java.math.BigDecimal lon,
                                                  TipoFilieraPoint tipo, Utente utente) {

        FilieraPoint point = new FilieraPoint();
        point.setNomePunto(nome);
        point.setDescrizione(desc);
        point.setIndirizzo(indirizzo);
        point.setLatitudine(lat);
        point.setLongitudine(lon);
        point.setTipo(tipo);
        point.setUtente(utente);
        FilieraPoint savedPoint = filieraPointRepository.save(point);

        // Crea la sottomissione e la imposta come APPROVATA
        ContentSubmission submission = new ContentSubmission(savedPoint.getId(), "FILIERAPOINT");
        submission.setStatus(StatoContenuto.APPROVATO);
        submission.setFeedbackCuratore("Approvato automaticamente dal Gestore al momento dell'accredito.");
        submission.updateState();
        ContentSubmission savedSubmission = submissionRepository.save(submission);

        savedPoint.setSubmission(savedSubmission);
        return filieraPointRepository.save(savedPoint);
    }

    /**
     * Metodo helper per assegnare un ruolo a un utente.
     */
    private void assegnaRuolo(Utente utente, ERole eRole) {
        Role ruolo = roleRepository.findByName(eRole)
                .orElseThrow(() -> new RuntimeException("Errore: Ruolo " + eRole.name() + " non trovato."));
        utente.getRoles().add(ruolo);
        utenteRepository.save(utente);
    }
}