package com.SupportHub.demo.filter;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.SupportHub.demo.services.AuthenticationService;
import com.SupportHub.demo.Utils.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private final AuthenticationService userDetailsService;
    private final JwtUtil jwtUtil;

    public JwtRequestFilter(AuthenticationService userDetailsService, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, 
                                    @NonNull HttpServletResponse response, 
                                    @NonNull FilterChain filterChain) 
                                    throws ServletException, IOException {

        String requestPath = request.getServletPath();
        String method = request.getMethod();

        // Skip JWT authentication for these endpoints
        if (isExcludedEndpoint(requestPath, method)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        // Extract JWT from the Authorization header
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
        }

        // Validate token and set authentication if it is valid
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Check if the token is valid and if so, set up the security context
            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                
                // Set authentication in the SecurityContext
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }

    // Helper method to determine if the endpoint should be excluded from filtering
    private boolean isExcludedEndpoint(String requestPath, String method) {
        return (requestPath.equals("/authenticate") 
                || (requestPath.equals("/api/users") && method.equalsIgnoreCase("POST")));
    }
}
