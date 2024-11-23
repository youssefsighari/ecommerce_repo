package Ecommerce.filters;



import Ecommerce.Utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
 
        try {
            // Vérifiez si l'en-tête Authorization est présent et commence par "Bearer "
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String jwt = authorizationHeader.substring(7);
                System.out.println("JWT extracted: " + jwt);

                // Extraire l'username du JWT
                String username = jwtUtils.extractUsername(jwt);
                System.out.println("Username extracted from JWT: " + username);

                // Vérifier que l'utilisateur n'est pas déjà authentifié dans le SecurityContext
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                    System.out.println("User loaded from database: " + userDetails.getUsername());
                    System.out.println("Roles assigned to user: " + userDetails.getAuthorities());
                    if (jwtUtils.validateToken(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        System.out.println("Authentication successful for user: " + username);
                    } else {
                        System.out.println("Invalid JWT for user: " + username);
                    }
                }

            } else {
                System.out.println("No JWT found in request header");
            }
        } catch (Exception e) {
            // Log et gestion des erreurs
            System.out.println("Error processing JWT: " + e.getMessage());
        }

        // Continuer la chaîne de filtres
        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Spécifiez ici les endpoints qui n'ont pas besoin de vérification JWT (ex: /login, /signup)
        String path = request.getRequestURI();
        return path.startsWith("/Login") || path.startsWith("/signup");
    }
}

