package com.talleros.multitenancy;

import com.talleros.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TenantContext {

    private final JwtService jwtService;

    public Long getCurrentTenantId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("No autenticado");
        }
        // El principal es el email — buscamos el tenantId en el token
        String token = (String) auth.getCredentials();
        if (token != null) {
            return jwtService.extractTenantId(token);
        }
        throw new RuntimeException("Token inválido");
    }
}