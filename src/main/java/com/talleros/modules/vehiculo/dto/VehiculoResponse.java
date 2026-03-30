package com.talleros.modules.vehiculo.dto;

import com.talleros.modules.vehiculo.model.Vehiculo.TipoVehiculo;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehiculoResponse {

    private Long id;
    private String patente;
    private String marca;
    private String modelo;
    private Integer anio;
    private Integer kilometraje;
    private String color;
    private TipoVehiculo tipo;
    private Long clienteId;
    private String clienteNombre;
    private String clienteApellido;
    private LocalDateTime createdAt;
}
