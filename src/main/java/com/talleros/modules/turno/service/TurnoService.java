package com.talleros.modules.turno.service;

import com.talleros.modules.cliente.model.Cliente;
import com.talleros.modules.cliente.repository.ClienteRepository;
import com.talleros.modules.tenant.model.Tenant;
import com.talleros.modules.tenant.repository.TenantRepository;
import com.talleros.modules.turno.dto.TurnoRequest;
import com.talleros.modules.turno.dto.TurnoResponse;
import com.talleros.modules.turno.model.Turno;
import com.talleros.modules.turno.model.Turno.EstadoTurno;
import com.talleros.modules.turno.repository.TurnoRepository;
import com.talleros.modules.vehiculo.model.Vehiculo;
import com.talleros.modules.vehiculo.repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TurnoService {

    private final TurnoRepository turnoRepository;
    private final ClienteRepository clienteRepository;
    private final VehiculoRepository vehiculoRepository;
    private final TenantRepository tenantRepository;

    // ── Obtener todos ───────────────────────────────────────────

    public List<TurnoResponse> obtenerTodos(Long tenantId) {
        return turnoRepository.findAllByTenantIdOrderByFechaHoraAsc(tenantId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Obtener por ID ──────────────────────────────────────────

    public TurnoResponse obtenerPorId(Long id, Long tenantId) {
        Turno turno = turnoRepository.findById(id)
                .filter(t -> t.getTenant().getId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));
        return toResponse(turno);
    }

    // ── Obtener por estado ──────────────────────────────────────

    public List<TurnoResponse> obtenerPorEstado(EstadoTurno estado, Long tenantId) {
        return turnoRepository.findAllByTenantIdAndEstado(tenantId, estado)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Obtener por cliente ─────────────────────────────────────

    public List<TurnoResponse> obtenerPorCliente(Long clienteId, Long tenantId) {
        return turnoRepository.findAllByClienteIdAndTenantId(clienteId, tenantId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Turnos del día (dashboard y agenda) ─────────────────────

    public List<TurnoResponse> obtenerDelDia(Long tenantId) {
        LocalDateTime inicioDia = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime finDia = inicioDia.plusDays(1);
        return turnoRepository.findAllByTenantIdAndFechaHoraBetweenOrderByFechaHoraAsc(
                        tenantId, inicioDia, finDia)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Turnos por rango de fechas (agenda semanal) ─────────────

    public List<TurnoResponse> obtenerPorRango(LocalDateTime inicio, LocalDateTime fin, Long tenantId) {
        return turnoRepository.findAllByTenantIdAndFechaHoraBetweenOrderByFechaHoraAsc(
                        tenantId, inicio, fin)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Crear ───────────────────────────────────────────────────

    @Transactional
    public TurnoResponse crear(TurnoRequest request, Long tenantId) {
        // Verificar solapamiento (ventana de 30 minutos)
        LocalDateTime inicio = request.getFechaHora().minusMinutes(29);
        LocalDateTime fin = request.getFechaHora().plusMinutes(29);
        if (turnoRepository.existsByTenantIdAndFechaHoraBetween(tenantId, inicio, fin)) {
            throw new RuntimeException("Ya existe un turno en ese horario");
        }

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Taller no encontrado"));

        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .filter(c -> c.getTenant().getId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Vehiculo vehiculo = vehiculoRepository.findById(request.getVehiculoId())
                .filter(v -> v.getTenant().getId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));

        Turno turno = Turno.builder()
                .fechaHora(request.getFechaHora())
                .motivo(request.getMotivo())
                .observaciones(request.getObservaciones())
                .cliente(cliente)
                .vehiculo(vehiculo)
                .tenant(tenant)
                .build();

        return toResponse(turnoRepository.save(turno));
    }

    // ── Cambiar estado ──────────────────────────────────────────

    @Transactional
    public TurnoResponse cambiarEstado(Long id, EstadoTurno nuevoEstado, Long tenantId) {
        Turno turno = turnoRepository.findById(id)
                .filter(t -> t.getTenant().getId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));
        turno.setEstado(nuevoEstado);
        return toResponse(turnoRepository.save(turno));
    }

    // ── Actualizar ──────────────────────────────────────────────

    @Transactional
    public TurnoResponse actualizar(Long id, TurnoRequest request, Long tenantId) {
        Turno turno = turnoRepository.findById(id)
                .filter(t -> t.getTenant().getId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        turno.setFechaHora(request.getFechaHora());
        turno.setMotivo(request.getMotivo());
        turno.setObservaciones(request.getObservaciones());

        return toResponse(turnoRepository.save(turno));
    }

    // ── Eliminar ────────────────────────────────────────────────

    @Transactional
    public void eliminar(Long id, Long tenantId) {
        Turno turno = turnoRepository.findById(id)
                .filter(t -> t.getTenant().getId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));
        turnoRepository.delete(turno);
    }

    // ── Mapper ──────────────────────────────────────────────────

    private TurnoResponse toResponse(Turno t) {
        return TurnoResponse.builder()
                .id(t.getId())
                .fechaHora(t.getFechaHora())
                .motivo(t.getMotivo())
                .observaciones(t.getObservaciones())
                .estado(t.getEstado())
                .clienteId(t.getCliente().getId())
                .clienteNombre(t.getCliente().getNombre())
                .clienteApellido(t.getCliente().getApellido())
                .clienteTelefono(t.getCliente().getTelefono())
                .vehiculoId(t.getVehiculo().getId())
                .vehiculoPatente(t.getVehiculo().getPatente())
                .vehiculoMarca(t.getVehiculo().getMarca())
                .vehiculoModelo(t.getVehiculo().getModelo())
                .createdAt(t.getCreatedAt())
                .build();
    }
}


