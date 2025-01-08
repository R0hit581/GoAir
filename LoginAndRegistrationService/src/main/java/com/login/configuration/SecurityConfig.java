package com.login.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.login.service.CustomUserDetailsService;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    
    @Autowired
    CustomUserDetailsService customUserDetailsService; // Injecting custom UserDetailsService for authentication.

    @Bean
    public static PasswordEncoder passwordEncoder() {
        // Bean to encode passwords using BCryptPasswordEncoder.
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        /*
         * Configures the security filter chain:
         * - Disables CSRF protection (suitable for stateless APIs).
         * - Allows all requests to paths matching "/api/**".
         * - Secures all other endpoints, requiring authentication.
         */
        http.csrf(c -> c.disable())
           .authorizeHttpRequests(
                request -> request.requestMatchers("/api/**").permitAll()
                                   .anyRequest().authenticated());
        return http.build();    
    }
     
    @Bean
    public AuthenticationProvider authenticationProvider() {
        /*
         * Configures the DaoAuthenticationProvider:
         * - Uses the custom UserDetailsService to load user details.
         * - Uses BCryptPasswordEncoder for password matching.
         */
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
    
    @Bean
    public AuthenticationManager authenticationManager() {
        /*
         * Provides an AuthenticationManager instance using the configured AuthenticationProvider.
         */
        return new ProviderManager(authenticationProvider());
    }
}