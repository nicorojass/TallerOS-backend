package com.talleros.modules.repuesto.repository;

import com.talleros.modules.repuesto.model.Repuesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepuestoRepository extends JpaRepository<Repuesto, Long> {

    // Todos los repuestos de un taller
    List<Repuesto> findAllByTenantId(Long tenantId);

    // Buscar por nombre o categoría
    List<Repuesto> findByTenantIdAndNombreContainingIgnoreCase(Long tenantId, String nombre);

    // Buscar por categoría
    List<Repuesto> findAllByTenantIdAndCategoria(Long tenantId, String categoria);

    // Buscar por código
    Optional<Repuesto> findByCodigoAndTenantId(String codigo, Long tenantId);

    // Verificar si existe el código en el taller
    boolean existsByCodigoAndTenantId(String codigo, Long tenantId);

    // Repuestos con stock bajo (para el dashboard)
    @Query("SELECT r FROM Repuesto r WHERE r.tenant.id = :tenantId AND r.stock <= r.stockMinimo")
    List<Repuesto> findStockBajo(Long tenantId);
}