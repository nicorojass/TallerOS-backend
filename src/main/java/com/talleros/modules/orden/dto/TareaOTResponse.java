package com.talleros.modules.orden.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TareaOTResponse {

    private Long id;
    private String descripcion;
    private BigDecimal precio;
}