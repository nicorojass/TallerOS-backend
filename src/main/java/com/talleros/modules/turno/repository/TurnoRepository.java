package com.talleros.modules.turno.repository;

import com.talleros.modules.turno.model.Turno;
import com.talleros.modules.turno.model.Turno.EstadoTurno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long> {

    // Todos los turnos de un taller ordenados por fecha
    List<Turno> findAllByTenantIdOrderByFechaHoraAsc(Long tenantId);

    // Turnos por estado
    List<Turno> findAllByTenantIdAndEstado(Long tenantId, EstadoTurno estado);

    // Turnos de un cliente
    List<Turno> findAllByClienteIdAndTenantId(Long clienteId, Long tenantId);

    // Turnos de un vehículo
    List<Turno> findAllByVehiculoIdAndTenantId(Long vehiculoId, Long tenantId);

    // Turnos del día (para el dashboard y la agenda)
    List<Turno> findAllByTenantIdAndFechaHoraBetweenOrderByFechaHoraAsc(
            Long tenantId, LocalDateTime inicio, LocalDateTime fin);

    // Verificar solapamiento de turnos (mismo horario)
    boolean existsByTenantIdAndFechaHoraBetween(
            Long tenantId, LocalDateTime inicio, LocalDateTime fin);
}