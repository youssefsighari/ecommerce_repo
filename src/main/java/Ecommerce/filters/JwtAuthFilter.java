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
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        try {
            // Check if the Authorization header is present and starts with "Bearer "
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String jwt = authorizationHeader.substring(7);
                logger.info("JWT extracted: {}", jwt);

                // Extract the username from the JWT
                String username = jwtUtils.extractUsername(jwt);
                logger.info("Username extracted from JWT: {}", username);

                // Ensure the user is not already authenticated
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                    logger.info("User loaded from database: {}", userDetails.getUsername());
                    if (jwtUtils.validateToken(jwt, userDetails)) {
                        // Récupérez le rôle brut du JWT (par exemple : USER ou ADMIN)
                        String role = jwtUtils.extractClaim(jwt, claims -> claims.get("role", String.class)); 
                        
                        // Ajoutez le préfixe "ROLE_" si nécessaire
                        if (!role.startsWith("ROLE_")) {
                            role = "ROLE_" + role;
                        }

                        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, authorities);
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        logger.info("Authentication successful for user: {} with roles: {}", username, authorities);
                    }

else {
                        logger.warn("Invalid JWT for user: {}", username);
                    }
                }
            } else {
                logger.warn("No JWT found in request header");
            }
        } catch (Exception e) {
            logger.error("Error processing JWT: {}", e.getMessage(), e);
            if (!response.isCommitted()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Invalid JWT token\"}");
            }
            return; // Stop the filter chain
        }

        // Continue the filter chain
        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Exclude public endpoints from filtering
        String path = request.getRequestURI().toLowerCase();
        return path.startsWith("/login") || path.startsWith("/signup");
    }
}
