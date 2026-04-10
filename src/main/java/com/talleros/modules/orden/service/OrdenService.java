package com.talleros.modules.orden.service;

import com.talleros.modules.cliente.model.Cliente;
import com.talleros.modules.cliente.repository.ClienteRepository;
import com.talleros.modules.orden.dto.*;
import com.talleros.modules.orden.model.OrdenDeTrabajo;
import com.talleros.modules.orden.model.OrdenDeTrabajo.EstadoOT;
import com.talleros.modules.orden.model.RepuestoOT;
import com.talleros.modules.orden.model.TareaOT;
import com.talleros.modules.orden.repository.OrdenRepository;
import com.talleros.modules.tenant.model.Tenant;
import com.talleros.modules.tenant.repository.TenantRepository;
import com.talleros.modules.vehiculo.model.Vehiculo;
import com.talleros.modules.vehiculo.repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdenService {

    private final OrdenRepository ordenRepository;
    private final ClienteRepository clienteRepository;
    private final VehiculoRepository vehiculoRepository;
    private final TenantRepository tenantRepository;

    // ── Obtener todas ───────────────────────────────────────────

    public List<OrdenResponse> obtenerTodas(Long tenantId) {
        return ordenRepository.findAllByTenantIdOrderByFechaIngresoDesc(tenantId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Obtener por ID ──────────────────────────────────────────

    public OrdenResponse obtenerPorId(Long id, Long tenantId) {
        OrdenDeTrabajo orden = ordenRepository.findById(id)
                .filter(o -> o.getTenant().getId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        return toResponse(orden);
    }

    // ── Obtener por estado (Kanban) ─────────────────────────────

    public List<OrdenResponse> obtenerPorEstado(EstadoOT estado, Long tenantId) {
        return ordenRepository.findAllByTenantIdAndEstado(tenantId, estado)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Obtener por vehículo (historial) ────────────────────────

    public List<OrdenResponse> obtenerPorVehiculo(Long vehiculoId, Long tenantId) {
        return ordenRepository.findAllByVehiculoIdAndTenantIdOrderByFechaIngresoDesc(vehiculoId, tenantId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Obtener por cliente ─────────────────────────────────────

    public List<OrdenResponse> obtenerPorCliente(Long clienteId, Long tenantId) {
        return ordenRepository.findAllByClienteIdAndTenantId(clienteId, tenantId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── OT del día (dashboard) ──────────────────────────────────

    public List<OrdenResponse> obtenerDelDia(Long tenantId) {
        LocalDateTime inicioDia = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime finDia = inicioDia.plusDays(1);
        return ordenRepository.findAllByTenantIdAndFechaIngresoBetween(tenantId, inicioDia, finDia)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Crear ───────────────────────────────────────────────────

    @Transactional
    public OrdenResponse crear(OrdenRequest request, Long tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Taller no encontrado"));

        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .filter(c -> c.getTenant().getId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Vehiculo vehiculo = vehiculoRepository.findById(request.getVehiculoId())
                .filter(v -> v.getTenant().getId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));

        // Generar número correlativo OT-001, OT-002...
        Long count = ordenRepository.contarPorTenant(tenantId);
        String numero = String.format("OT-%03d", count + 1);

        OrdenDeTrabajo orden = OrdenDeTrabajo.builder()
                .numero(numero)
                .descripcionProblema(request.getDescripcionProblema())
                .observaciones(request.getObservaciones())
                .kilometrajeIngreso(request.getKilometrajeIngreso())
                .cliente(cliente)
                .vehiculo(vehiculo)
                .tenant(tenant)
                .build();

        // Agregar tareas
        if (request.getTareas() != null) {
            for (TareaOTRequest t : request.getTareas()) {
                TareaOT tarea = TareaOT.builder()
                        .descripcion(t.getDescripcion())
                        .precio(t.getPrecio())
                        .orden(orden)
                        .build();
                orden.getTareas().add(tarea);
            }
        }

        // Agregar repuestos
        if (request.getRepuestos() != null) {
            for (RepuestoOTRequest r : request.getRepuestos()) {
                RepuestoOT repuesto = RepuestoOT.builder()
                        .nombre(r.getNombre())
                        .cantidad(r.getCantidad())
                        .precioUnitario(r.getPrecioUnitario())
                        .orden(orden)
                        .build();
                orden.getRepuestos().add(repuesto);
            }
        }

        orden.recalcularTotal();
        return toResponse(ordenRepository.save(orden));
    }

    // ── Cambiar estado ──────────────────────────────────────────

    @Transactional
    public OrdenResponse cambiarEstado(Long id, EstadoOT nuevoEstado, Long tenantId) {
        OrdenDeTrabajo orden = ordenRepository.findById(id)
                .filter(o -> o.getTenant().getId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        orden.setEstado(nuevoEstado);

        // Si se marca como entregada, registrar fecha de entrega
        if (nuevoEstado == EstadoOT.ENTREGADO) {
            orden.setFechaEntrega(LocalDateTime.now());
        }

        return toResponse(ordenRepository.save(orden));
    }

    // ── Actualizar ──────────────────────────────────────────────

    @Transactional
    public OrdenResponse actualizar(Long id, OrdenRequest request, Long tenantId) {
        OrdenDeTrabajo orden = ordenRepository.findById(id)
                .filter(o -> o.getTenant().getId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        orden.setDescripcionProblema(request.getDescripcionProblema());
        orden.setObservaciones(request.getObservaciones());
        orden.setKilometrajeIngreso(request.getKilometrajeIngreso());

        // Reemplazar tareas
        orden.getTareas().clear();
        if (request.getTareas() != null) {
            for (TareaOTRequest t : request.getTareas()) {
                TareaOT tarea = TareaOT.builder()
                        .descripcion(t.getDescripcion())
                        .precio(t.getPrecio())
                        .orden(orden)
                        .build();
                orden.getTareas().add(tarea);
            }
        }

        // Reemplazar repuestos
        orden.getRepuestos().clear();
        if (request.getRepuestos() != null) {
            for (RepuestoOTRequest r : request.getRepuestos()) {
                RepuestoOT repuesto = RepuestoOT.builder()
                        .nombre(r.getNombre())
                        .cantidad(r.getCantidad())
                        .precioUnitario(r.getPrecioUnitario())
                        .orden(orden)
                        .build();
                orden.getRepuestos().add(repuesto);
            }
        }

        orden.recalcularTotal();
        return toResponse(ordenRepository.save(orden));
    }

    // ── Eliminar ────────────────────────────────────────────────

    @Transactional
    public void eliminar(Long id, Long tenantId) {
        OrdenDeTrabajo orden = ordenRepository.findById(id)
                .filter(o -> o.getTenant().getId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        ordenRepository.delete(orden);
    }

    // ── Mapper ──────────────────────────────────────────────────

    private OrdenResponse toResponse(OrdenDeTrabajo o) {
        List<TareaOTResponse> tareas = o.getTareas().stream()
                .map(t -> TareaOTResponse.builder()
                        .id(t.getId())
                        .descripcion(t.getDescripcion())
                        .precio(t.getPrecio())
                        .build())
                .collect(Collectors.toList());

        List<RepuestoOTResponse> repuestos = o.getRepuestos().stream()
                .map(r -> RepuestoOTResponse.builder()
                        .id(r.getId())
                        .nombre(r.getNombre())
                        .cantidad(r.getCantidad())
                        .precioUnitario(r.getPrecioUnitario())
                        .subtotal(r.getPrecioUnitario().multiply(BigDecimal.valueOf(r.getCantidad())))
                        .build())
                .collect(Collectors.toList());

        return OrdenResponse.builder()
                .id(o.getId())
                .numero(o.getNumero())
                .estado(o.getEstado())
                .descripcionProblema(o.getDescripcionProblema())
                .observaciones(o.getObservaciones())
                .kilometrajeIngreso(o.getKilometrajeIngreso())
                .clienteId(o.getCliente().getId())
                .clienteNombre(o.getCliente().getNombre())
                .clienteApellido(o.getCliente().getApellido())
                .vehiculoId(o.getVehiculo().getId())
                .vehiculoPatente(o.getVehiculo().getPatente())
                .vehiculoMarca(o.getVehiculo().getMarca())
                .vehiculoModelo(o.getVehiculo().getModelo())
                .tareas(tareas)
                .repuestos(repuestos)
                .totalManoObra(o.getTotalManoObra())
                .totalRepuestos(o.getTotalRepuestos())
                .total(o.getTotal())
                .fechaIngreso(o.getFechaIngreso())
                .fechaEntrega(o.getFechaEntrega())
                .build();
    }
}