package it.unicam.cs.ids2425.FilieraAgricola.config;

import it.unicam.cs.ids2425.FilieraAgricola.model.ERole;
import it.unicam.cs.ids2425.FilieraAgricola.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import it.unicam.cs.ids2425.FilieraAgricola.repository.RoleRepository;

/**
 * Questo componente viene eseguito automaticamente all'avvio dell'applicazione.
 * Controlla che tutti i ruoli definiti nell'enum ERole esistano nel database
 * e li crea se mancano.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Avvio inizializzazione ruoli...");

        // Iteriamo su tutti i ruoli definiti nell'enum
        for (ERole roleName : ERole.values()) {

            // Controlliamo se il ruolo esiste già nel database
            if (roleRepository.findByName(roleName).isEmpty()) {

                // Se non esiste, lo creiamo e salviamo
                Role role = new Role(roleName);
                roleRepository.save(role);

                System.out.println("Creato ruolo di default: " + roleName);
            }
        }
        System.out.println("Inizializzazione ruoli completata.");
    }
}

