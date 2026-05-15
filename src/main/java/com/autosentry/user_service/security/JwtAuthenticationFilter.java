package com.autosentry.user_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

//  This will be changed once the service classes are written
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // Extract the Authorization header
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Check if the header is missing
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Pass the request along without authenticating (e.g., for /api/auth/login)
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the token string (remove "Bearer " which is 7 characters)
        jwt = authHeader.substring(7);

        // Ask JwtUtil to read the email from the token
        userEmail = jwtUtil.extractUsername(jwt);

        // If we found an email, and the user isn't already authenticated in this request cycle
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load the user from the database
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 6. Validate the token's cryptographic signature and expiration
            if (jwtUtil.isTokenValid(jwt, userDetails.getUsername())) {

                // Create Authentication token for Spring Security
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // Put the token into the Security Context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 9. Continue to the Controller endpoint
        filterChain.doFilter(request, response);
    }
}