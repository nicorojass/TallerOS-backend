package com.talleros.modules.vehiculo.model;

import com.talleros.modules.cliente.model.Cliente;
import com.talleros.modules.tenant.model.Tenant;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "vehiculos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String patente;

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private String modelo;

    @Column(nullable = false)
    private Integer anio;

    @Column
    private Integer kilometraje;

    @Column
    private String color;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoVehiculo tipo;

    // ── Relación con cliente ────────────────────────────────────
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    // ── Multi-tenancy ───────────────────────────────────────────
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (tipo == null) tipo = TipoVehiculo.AUTO;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ── Enum ────────────────────────────────────────────────────

    public enum TipoVehiculo {
        AUTO, MOTO, CAMIONETA, CAMION, OTRO
    }
}