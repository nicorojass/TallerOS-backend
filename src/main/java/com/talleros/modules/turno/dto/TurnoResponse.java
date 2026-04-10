package com.talleros.modules.turno.dto;

import com.talleros.modules.turno.model.Turno.EstadoTurno;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TurnoResponse {

    private Long id;
    private LocalDateTime fechaHora;
    private String motivo;
    private String observaciones;
    private EstadoTurno estado;

    // Cliente
    private Long clienteId;
    private String clienteNombre;
    private String clienteApellido;
    private String clienteTelefono;

    // Vehículo
    private Long vehiculoId;
    private String vehiculoPatente;
    private String vehiculoMarca;
    private String vehiculoModelo;

    private LocalDateTime createdAt;
}