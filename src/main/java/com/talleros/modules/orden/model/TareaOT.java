// ── TareaOT.java ────────────────────────────────────────────────
package com.talleros.modules.orden.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tareas_ot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TareaOT {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_id", nullable = false)
    private OrdenDeTrabajo orden;
}

