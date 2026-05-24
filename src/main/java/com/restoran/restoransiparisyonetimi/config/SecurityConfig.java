package com.restoran.restoransiparisyonetimi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)

                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin())
                )

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/h2-console/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/menu-items/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/orders").hasAnyRole("CUSTOMER", "ADMIN")

                        .requestMatchers(HttpMethod.PATCH, "/api/orders/**").hasAnyRole("WAITER", "ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/menu-items").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/menu-items/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/menu-items/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                .httpBasic(Customizer.withDefaults())

                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles("ADMIN")
                .build();

        UserDetails waiter = User.builder()
                .username("garson")
                .password(passwordEncoder.encode("garson123"))
                .roles("WAITER")
                .build();

        UserDetails customer = User.builder()
                .username("musteri")
                .password(passwordEncoder.encode("musteri123"))
                .roles("CUSTOMER")
                .build();

        return new InMemoryUserDetailsManager(admin, waiter, customer);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}