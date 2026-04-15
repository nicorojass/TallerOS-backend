package com.talleros.modules.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    // Datos del usuario
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Email
    @NotBlank(message = "El email es obligatorio")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    // Datos del taller (tenant)
    @NotBlank(message = "El nombre del taller es obligatorio")
    private String nombreTaller;

    @Email
    @NotBlank(message = "El email del taller es obligatorio")
    private String emailTaller;

    @NotBlank(message = "El teléfono del taller es obligatorio")
    private String telefonoTaller;
}