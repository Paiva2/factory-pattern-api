package com.root.pattern.application.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private final RequestFilterChain requestFilterChain;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(req -> {
                req.antMatchers(HttpMethod.POST, "/api/v1/user/register").permitAll();
                req.antMatchers(HttpMethod.POST, "/api/v1/musician/register").permitAll();
                req.antMatchers(HttpMethod.POST, "/api/v1/user/login").permitAll();
                req.antMatchers(HttpMethod.POST, "/api/v1/musician/login").permitAll();
                req.antMatchers(HttpMethod.POST, "/api/v1/musician/album").hasAnyRole("MUSICIAN", "ADMIN");
                req.antMatchers(HttpMethod.POST, "/api/v1/music/new/**").hasAnyRole("MUSICIAN", "ADMIN");
                req.antMatchers(HttpMethod.POST, "/api/v1/playlist/new").hasAnyRole("USER", "ADMIN");

                req.antMatchers(HttpMethod.GET, "/api/v1/music/all/{musicianId}").permitAll();
                req.antMatchers(HttpMethod.GET, "/api/v1/musician/album/all").permitAll();
                req.antMatchers(HttpMethod.GET, "/api/v1/musician/profile").permitAll();
                req.antMatchers(HttpMethod.GET, "/api/v1/musician/album/{albumId}").permitAll();
                req.antMatchers(HttpMethod.GET, "/api/v1/musician/album/list/{musicianId}").permitAll();
                req.antMatchers(HttpMethod.GET, "/api/v1/music").permitAll();
                req.antMatchers(HttpMethod.GET, "/api/v1/music/{musicId}").permitAll();
                req.antMatchers(HttpMethod.GET, "/api/v1/musician").permitAll();
                req.antMatchers(HttpMethod.GET, "/api/v1/user/profile").hasAnyRole("USER", "ADMIN");
                req.antMatchers(HttpMethod.GET, "/api/v1/musician/profile").hasAnyRole("MUSICIAN", "ADMIN");
                req.antMatchers(HttpMethod.GET, "/api/v1/music/own/list").hasAnyRole("USER", "MUSICIAN", "ADMIN");
                req.antMatchers(HttpMethod.GET, "/api/v1/playlist/own/list").hasAnyRole("USER", "ADMIN");

                req.antMatchers(HttpMethod.PATCH, "/api/v1/user/profile").hasAnyRole("USER", "ADMIN");
                req.antMatchers(HttpMethod.PATCH, "/api/v1/musician/profile").hasAnyRole("MUSICIAN", "ADMIN");
                req.antMatchers(HttpMethod.PATCH, "/api/v1/musician/album/{albumId}").hasAnyRole("MUSICIAN", "ADMIN");

                req.antMatchers(HttpMethod.DELETE, "/api/v1/musician/album/{albumId}").hasAnyRole("MUSICIAN", "ADMIN");
                req.antMatchers(HttpMethod.DELETE, "/api/v1/music/{musicId}").hasAnyRole("MUSICIAN", "ADMIN");

                req.anyRequest().authenticated();
            }).addFilterBefore(requestFilterChain, UsernamePasswordAuthenticationFilter.class)
            .csrf(csrf -> csrf.disable()).cors(cors -> cors.disable());

        return http.build();
    }

    @Bean
    PasswordEncoder securityEncoder() {
        return new BCryptPasswordEncoder();
    }
}
