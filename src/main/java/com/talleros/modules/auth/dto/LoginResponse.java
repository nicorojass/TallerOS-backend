package com.talleros.modules.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private String token;
    private String email;
    private String nombre;
    private String role;
    private Long tenantId;
    private String nombreTaller;
}