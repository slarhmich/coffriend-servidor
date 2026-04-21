package com.brewingcode.coffriend_servidor.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/login", "/api/auth/logout", "/error", "/docs.html").permitAll()
                // Let GET /api/productes be public
                .requestMatchers(HttpMethod.GET, "/api/productes/**").permitAll()
                // Let GET /api/botigues be public
                .requestMatchers(HttpMethod.GET, "/api/botigues/**").permitAll()
                // Let GET /api/insignies be public
                .requestMatchers(HttpMethod.GET, "/api/insignies/**").permitAll()
                // Let GET /api/gamificacion/levels be public
                .requestMatchers(HttpMethod.GET, "/api/gamificacion/levels").permitAll()
                // Let register be public
                .requestMatchers(HttpMethod.POST, "/api/usuaris").permitAll()
                // Require auth for everything else
                .anyRequest().authenticated()
            )
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) ->
                    response.sendError(401, "Unauthorized"))
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
