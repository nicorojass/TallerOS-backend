package com.talleros.modules.orden.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TareaOTRequest {

    @NotBlank(message = "La descripción de la tarea es obligatoria")
    private String descripcion;

    @NotNull(message = "El precio de la tarea es obligatorio")
    private BigDecimal precio;
}
