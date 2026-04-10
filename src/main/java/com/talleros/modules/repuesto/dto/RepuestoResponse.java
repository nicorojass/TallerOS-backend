package com.talleros.modules.repuesto.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepuestoResponse {

    private Long id;
    private String nombre;
    private String codigo;
    private String categoria;
    private String descripcion;
    private Integer stock;
    private Integer stockMinimo;
    private BigDecimal precioCosto;
    private BigDecimal precioVenta;
    private boolean stockBajo;
    private LocalDateTime createdAt;
}