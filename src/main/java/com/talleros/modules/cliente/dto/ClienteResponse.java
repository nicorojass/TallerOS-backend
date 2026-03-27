package com.talleros.modules.cliente.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteResponse {

    private Long id;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private String dni;
    private int cantidadVehiculos;
    private LocalDateTime createdAt;
}