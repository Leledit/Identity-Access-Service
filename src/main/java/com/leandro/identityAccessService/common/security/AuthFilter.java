package com.leandro.identityAccessService.common.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
@Profile("!local")
public class AuthFilter implements Filter {

    private static final String API_KEY_HEADER_NAME = "apiKey";

    @Value("${service.apiKey}")
    private String serviceApiKey;

    private static final String[] AUTH_WHITELIST = {
            "swagger",
            "api-docs",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars/**",
            "/actuator",
            "/actuator/**",
            "/actuator/health",
            "/actuator/health/**",
            "/actuator/health/liveness",
            "/actuator/health/readiness"
    };

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest  req  = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        log.debug("Starting a transaction for req : {} {}", req.getMethod(), req.getRequestURI());

        final String headerApiKey  = req.getHeader(API_KEY_HEADER_NAME);
        final String requestApiKey = req.getParameter(API_KEY_HEADER_NAME);

        if (Arrays.stream(AUTH_WHITELIST).parallel().anyMatch(req.getRequestURI()::contains)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        if (StringUtils.isBlank(headerApiKey) && StringUtils.isBlank(requestApiKey)) {
            doUnauthorizedRequest(resp);
            return;
        }

        if (!StringUtils.equals(serviceApiKey, headerApiKey)
                && !StringUtils.equals(serviceApiKey, requestApiKey)) {
            doUnauthorizedRequest(resp);
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void doUnauthorizedRequest(HttpServletResponse response) throws IOException {
        if (response != null) {
            response.reset();
            response.setStatus(401);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("""
                    {
                      "code": "UNAUTHORIZED",
                      "message": "Missing or invalid API key"
                    }
                    """);
        }
    }
}