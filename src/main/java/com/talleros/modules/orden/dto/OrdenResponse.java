package com.talleros.modules.orden.dto;

import com.talleros.modules.orden.model.OrdenDeTrabajo.EstadoOT;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenResponse {

    private Long id;
    private String numero;
    private EstadoOT estado;
    private String descripcionProblema;
    private String observaciones;
    private Integer kilometrajeIngreso;

    // Cliente
    private Long clienteId;
    private String clienteNombre;
    private String clienteApellido;

    // Vehículo
    private Long vehiculoId;
    private String vehiculoPatente;
    private String vehiculoMarca;
    private String vehiculoModelo;

    // Tareas y repuestos
    private List<TareaOTResponse> tareas;
    private List<RepuestoOTResponse> repuestos;

    // Totales
    private BigDecimal totalManoObra;
    private BigDecimal totalRepuestos;
    private BigDecimal total;

    // Fechas
    private LocalDateTime fechaIngreso;
    private LocalDateTime fechaEntrega;
}