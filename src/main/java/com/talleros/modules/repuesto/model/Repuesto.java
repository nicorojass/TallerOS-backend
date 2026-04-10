package com.talleros.modules.repuesto.model;

import com.talleros.modules.tenant.model.Tenant;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "repuestos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Repuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(unique = true)
    private String codigo;

    @Column
    private String categoria;

    @Column
    private String descripcion;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private Integer stockMinimo;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioCosto;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioVenta;

    // ── Multi-tenancy ───────────────────────────────────────────

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    // ── Fechas ──────────────────────────────────────────────────

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (stockMinimo == null) stockMinimo = 0;
        if (stock == null) stock = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ── Métodos de negocio ──────────────────────────────────────

    public boolean tieneStockBajo() {
        return stock <= stockMinimo;
    }

    public boolean tieneStock(int cantidad) {
        return stock >= cantidad;
    }

    public void descontarStock(int cantidad) {
        if (!tieneStock(cantidad)) {
            throw new RuntimeException("Stock insuficiente para: " + nombre);
        }
        this.stock -= cantidad;
    }

    public void agregarStock(int cantidad) {
        this.stock += cantidad;
    }
}