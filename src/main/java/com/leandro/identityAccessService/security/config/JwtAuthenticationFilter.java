package com.leandro.identityAccessService.security.config;

import com.leandro.identityAccessService.security.service.JwtService;
import com.leandro.identityAccessService.user.dto.UserAuthDTO;
import com.leandro.identityAccessService.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        if (!jwtService.isValid(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        String userId = jwtService.extractUserId(token);

        UserAuthDTO user = userService.getUserAuthInfo(userId);

        if (user == null || !Boolean.TRUE.equals(user.getActive())) {
            filterChain.doFilter(request, response);
            return;
        }


        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getPermissions()
                                .stream()
                                .map(SimpleGrantedAuthority::new)
                                .toList()
                );

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

}