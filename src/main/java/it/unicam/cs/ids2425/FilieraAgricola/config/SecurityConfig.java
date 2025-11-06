package it.unicam.cs.ids2425.FilieraAgricola.config;

import it.unicam.cs.ids2425.FilieraAgricola.security.AuthEntryPointJwt;
import it.unicam.cs.ids2425.FilieraAgricola.security.AuthTokenFilter;
import it.unicam.cs.ids2425.FilieraAgricola.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity // Abilita la sicurezza a livello di metodo (es. @PreAuthorize)
public class SecurityConfig {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        // Questo espone l'AuthenticationManager, risolvendo l'errore in AuthService
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disattiva CSRF per API stateless
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler)) // Gestore errori 401
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // No sessioni
                .authorizeHttpRequests(auth -> auth
                        // Permetti a tutti di accedere agli endpoint di autenticazione
                        .requestMatchers("/api/auth/**").permitAll()
                        // Permetti a tutti di VEDERE i prodotti (GET)
                        .requestMatchers(HttpMethod.GET, "/api/prodotti/**").permitAll()
                        // Permetti a tutti di VEDERE le aziende (GET)
                        .requestMatchers(HttpMethod.GET, "/api/aziende/**").permitAll()
                        // Permetti a tutti di VEDERE il catalogo (GET)
                        .requestMatchers(HttpMethod.GET, "/api/marketplace/catalogo").permitAll()
                        // Tutte le altre richieste devono essere autenticate
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider()) // Imposta il nostro provider di autenticazione
                // Aggiunge il nostro filtro JWT prima del filtro standard
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

