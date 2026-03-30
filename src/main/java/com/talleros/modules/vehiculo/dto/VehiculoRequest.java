package com.talleros.modules.vehiculo.dto;

import com.talleros.modules.vehiculo.model.Vehiculo.TipoVehiculo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehiculoRequest {

    @NotBlank(message = "La patente es obligatoria")
    private String patente;

    @NotBlank(message = "La marca es obligatoria")
    private String marca;

    @NotBlank(message = "El modelo es obligatorio")
    private String modelo;

    @NotNull(message = "El año es obligatorio")
    private Integer anio;

    private Integer kilometraje;
    private String color;
    private TipoVehiculo tipo;

    @NotNull(message = "El cliente es obligatorio")
    private Long clienteId;
}