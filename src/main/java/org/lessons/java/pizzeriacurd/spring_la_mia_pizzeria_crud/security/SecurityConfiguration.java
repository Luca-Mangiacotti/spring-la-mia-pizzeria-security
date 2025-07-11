package org.lessons.java.pizzeriacurd.spring_la_mia_pizzeria_crud.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

//Questo file conterrà tutta la configurazione per la sicurezza
//EnableWebSecurity si assicura che la configurazione venga applicata per tutte le rotte 
//Vengono aggiunti tutti i Beans che vogliamo sfruttare sulla base della nostra configurazione

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    @SuppressWarnings("removal")

    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/pizzas/create", "/pizzas/edit/** ", "/offers/create", "/offers/edit/**",
                        "/ingredients/create", "/ingredients/edit/**")
                .hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.POST, "/pizzas/** ", "/offers/** ", "/ingredients/** ")
                .hasAnyAuthority("ADMIN")
                .requestMatchers("/pizzas", "/pizzas/**").hasAnyAuthority("ADMIN", "USER")
                .requestMatchers("/**").permitAll()
                .and().formLogin()
                .and().logout()
                .and().exceptionHandling()
                .and().csrf().disable();

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:5173"); // Cambia con l'URL del tuo frontend
        configuration.addAllowedMethod("GET");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    @SuppressWarnings("deprecation")
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        // questo provider utilizzerà un servizio per il recupero degli user tramite
        // username
        authProvider.setUserDetailsService(userDetailService());

        // questo provider utilizzera passwordEncoder
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    DatabaseUserDetailService userDetailService() {
        return new DatabaseUserDetailService();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
