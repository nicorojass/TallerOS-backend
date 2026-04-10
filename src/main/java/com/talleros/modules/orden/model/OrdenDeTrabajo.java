package com.talleros.modules.orden.model;

import com.talleros.modules.cliente.model.Cliente;
import com.talleros.modules.tenant.model.Tenant;
import com.talleros.modules.vehiculo.model.Vehiculo;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ordenes_de_trabajo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenDeTrabajo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero; // Ej: OT-001, OT-002

    @Column(nullable = false)
    private String descripcionProblema;

    @Column
    private String observaciones;

    @Column
    private Integer kilometrajeIngreso;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoOT estado;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalManoObra;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalRepuestos;

    @Column(precision = 10, scale = 2)
    private BigDecimal total;

    // ── Relaciones ──────────────────────────────────────────────

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TareaOT> tareas = new ArrayList<>();

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RepuestoOT> repuestos = new ArrayList<>();

    // ── Multi-tenancy ───────────────────────────────────────────

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    // ── Fechas ──────────────────────────────────────────────────

    @Column(name = "fecha_ingreso", updatable = false)
    private LocalDateTime fechaIngreso;

    @Column(name = "fecha_entrega")
    private LocalDateTime fechaEntrega;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        fechaIngreso = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (estado == null) estado = EstadoOT.RECIBIDO;
        if (totalManoObra == null) totalManoObra = BigDecimal.ZERO;
        if (totalRepuestos == null) totalRepuestos = BigDecimal.ZERO;
        if (total == null) total = BigDecimal.ZERO;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ── Método para recalcular el total ─────────────────────────

    public void recalcularTotal() {
        totalManoObra = tareas.stream()
                .map(TareaOT::getPrecio)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalRepuestos = repuestos.stream()
                .map(r -> r.getPrecioUnitario().multiply(BigDecimal.valueOf(r.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        total = totalManoObra.add(totalRepuestos);
    }

    // ── Enum ────────────────────────────────────────────────────

    public enum EstadoOT {
        RECIBIDO, EN_PROCESO, LISTO, ENTREGADO
    }
}