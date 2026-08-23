package com.aktech.overseas.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // Skip login/register
        if (request.getServletPath().startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("==================================");
        System.out.println("REQUEST : " + request.getMethod() + " " + request.getRequestURI());

        String authHeader = request.getHeader("Authorization");
        System.out.println("HEADER : " + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            try {

                String token = authHeader.substring(7);

                System.out.println("TOKEN : " + token);

                String username = jwtUtil.extractUsername(token);
                String role = jwtUtil.extractRole(token);

                System.out.println("USERNAME : " + username);
                System.out.println("ROLE : " + role);

                if (jwtUtil.isTokenValid(token, username)) {

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    username,
                                    null,
                                    List.of(new SimpleGrantedAuthority(role))
                            );

                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    System.out.println("AUTHENTICATION SET");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // VERY IMPORTANT
        filterChain.doFilter(request, response);
    }
}