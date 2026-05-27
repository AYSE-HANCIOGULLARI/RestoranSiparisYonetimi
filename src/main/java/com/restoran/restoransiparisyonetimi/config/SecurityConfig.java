package com.restoran.restoransiparisyonetimi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/h2-console/**").permitAll()
                        .requestMatchers("/", "/menu", "/orders").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/menu-items/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/orders").hasAnyRole("CUSTOMER", "ADMIN","WAITER")
                        .requestMatchers(HttpMethod.PATCH, "/api/orders/**").hasAnyRole("WAITER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/menu-items").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/menu-items/**").hasAnyRole("ADMIN","WAITER")
                        .requestMatchers(HttpMethod.DELETE, "/api/menu-items/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )

                .httpBasic(httpBasic -> {})

                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )

                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("1214"))
                .roles("ADMIN")
                .build();

        UserDetails waiter = User.builder()
                .username("garson")
                .password(passwordEncoder.encode("1214"))
                .roles("WAITER")
                .build();

        UserDetails customer = User.builder()
                .username("musteri")
                .password(passwordEncoder.encode("1214"))
                .roles("CUSTOMER")
                .build();

        return new InMemoryUserDetailsManager(admin, waiter, customer);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}