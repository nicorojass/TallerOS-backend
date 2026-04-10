package com.talleros.modules.orden.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenRequest {

    @NotNull(message = "El cliente es obligatorio")
    private Long clienteId;

    @NotNull(message = "El vehículo es obligatorio")
    private Long vehiculoId;

    @NotBlank(message = "La descripción del problema es obligatoria")
    private String descripcionProblema;

    private String observaciones;

    private Integer kilometrajeIngreso;

    @Builder.Default
    private List<TareaOTRequest> tareas = new ArrayList<>();

    @Builder.Default
    private List<RepuestoOTRequest> repuestos = new ArrayList<>();
}