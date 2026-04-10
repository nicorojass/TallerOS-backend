package com.talleros.modules.orden.repository;

import com.talleros.modules.orden.model.OrdenDeTrabajo;
import com.talleros.modules.orden.model.OrdenDeTrabajo.EstadoOT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrdenRepository extends JpaRepository<OrdenDeTrabajo, Long> {

    // Todas las OT de un taller
    List<OrdenDeTrabajo> findAllByTenantIdOrderByFechaIngresoDesc(Long tenantId);

    // OT por estado (para el Kanban)
    List<OrdenDeTrabajo> findAllByTenantIdAndEstado(Long tenantId, EstadoOT estado);

    // OT de un cliente específico
    List<OrdenDeTrabajo> findAllByClienteIdAndTenantId(Long clienteId, Long tenantId);

    // OT de un vehículo específico (historial)
    List<OrdenDeTrabajo> findAllByVehiculoIdAndTenantIdOrderByFechaIngresoDesc(Long vehiculoId, Long tenantId);

    // Buscar por número de OT (ej: OT-001)
    Optional<OrdenDeTrabajo> findByNumeroAndTenantId(String numero, Long tenantId);

    // OT del día (para el dashboard)
    List<OrdenDeTrabajo> findAllByTenantIdAndFechaIngresoBetween(
            Long tenantId, LocalDateTime inicio, LocalDateTime fin);

    // Último número de OT para generar el siguiente (ej: OT-042)
    @Query("SELECT COUNT(o) FROM OrdenDeTrabajo o WHERE o.tenant.id = :tenantId")
    Long contarPorTenant(Long tenantId);
}