package com.talleros.modules.auth.service;

import com.talleros.config.JwtService;
import com.talleros.modules.auth.dto.LoginRequest;
import com.talleros.modules.auth.dto.LoginResponse;
import com.talleros.modules.auth.dto.RegisterRequest;
import com.talleros.modules.auth.model.User;
import com.talleros.modules.auth.repository.UserRepository;
import com.talleros.modules.tenant.model.Tenant;
import com.talleros.modules.tenant.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    // ── Register ────────────────────────────────────────────────

    @Transactional
    public LoginResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con ese email");
        }

        // Crear el tenant (taller) primero
        if (tenantRepository.existsByEmail(request.getEmailTaller())) {
            throw new RuntimeException("Ya existe un taller con ese email");
        }

        Tenant tenant = Tenant.builder()
                .nombre(request.getNombreTaller())
                .email(request.getEmailTaller())
                .telefono(request.getTelefonoTaller())
                .build();

        tenant = tenantRepository.save(tenant);

        // Crear el usuario ADMIN del taller
        User user = User.builder()
                .nombre(request.getNombre())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.Role.ADMIN)
                .tenant(tenant)
                .build();

        user = userRepository.save(user);

        String token = jwtService.generateToken(user);
        return LoginResponse.builder()
                .token(token)
                .email(user.getEmail())
                .nombre(user.getNombre())
                .role(user.getRole().name())
                .tenantId(tenant.getId())
                .nombreTaller(tenant.getNombre())
                .build();
    }

    // ── Login ───────────────────────────────────────────────────

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtService.generateToken(user);
        return LoginResponse.builder()
                .token(token)
                .email(user.getEmail())
                .nombre(user.getNombre())
                .role(user.getRole().name())
                .tenantId(user.getTenant().getId())
                .nombreTaller(user.getTenant().getNombre())
                .build();
    }
}