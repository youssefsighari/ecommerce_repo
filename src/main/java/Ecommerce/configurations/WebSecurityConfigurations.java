package Ecommerce.configurations;



import Ecommerce.filters.JwtAuthFilter; 
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) 
public class WebSecurityConfigurations { 

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .cors().configurationSource(request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
                config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                config.setAllowedHeaders(Arrays.asList("*"));
                config.setAllowCredentials(true);
                return config;
            })
            .and()
            .authorizeHttpRequests()
            // Autorisations publiques
            .requestMatchers("/signup", "/Login").permitAll()
            
            // Autorisations spécifiques aux rôles
            .requestMatchers("/admin/**").hasRole("ADMIN")  // Accessible uniquement aux admins
            .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN") // Accessible aux utilisateurs et admins
            
            // Autorisation pour tout utilisateur authentifié
            .requestMatchers("/admin/produits/deleteAll").hasRole("ADMIN")
            
            .requestMatchers("/cart/**").permitAll()
            




            
            // Tout le reste nécessite une authentification
            
            
            .and()
            .exceptionHandling()
            .accessDeniedHandler((request, response, accessDeniedException) -> {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("{\"error\": \"Access Denied\", \"message\": \"" + accessDeniedException.getMessage() + "\"}");
            })
            .authenticationEntryPoint((request, response, authException) -> {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"Please authenticate.\"}");
            })
            
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean 
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
