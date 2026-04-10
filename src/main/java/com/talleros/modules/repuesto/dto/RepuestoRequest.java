package com.talleros.modules.repuesto.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepuestoRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String codigo;
    private String categoria;
    private String descripcion;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @NotNull(message = "El stock mínimo es obligatorio")
    @Min(value = 0, message = "El stock mínimo no puede ser negativo")
    private Integer stockMinimo;

    @NotNull(message = "El precio de costo es obligatorio")
    private BigDecimal precioCosto;

    @NotNull(message = "El precio de venta es obligatorio")
    private BigDecimal precioVenta;
}
