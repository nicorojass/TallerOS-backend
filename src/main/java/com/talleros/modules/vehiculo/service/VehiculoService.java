package com.talleros.modules.vehiculo.service;

import com.talleros.modules.cliente.model.Cliente;
import com.talleros.modules.cliente.repository.ClienteRepository;
import com.talleros.modules.tenant.model.Tenant;
import com.talleros.modules.tenant.repository.TenantRepository;
import com.talleros.modules.vehiculo.dto.VehiculoRequest;
import com.talleros.modules.vehiculo.dto.VehiculoResponse;
import com.talleros.modules.vehiculo.model.Vehiculo;
import com.talleros.modules.vehiculo.repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final ClienteRepository clienteRepository;
    private final TenantRepository tenantRepository;

    // ── Obtener todos ───────────────────────────────────────────

    public List<VehiculoResponse> obtenerTodos(Long tenantId) {
        return vehiculoRepository.findAllByTenantId(tenantId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Obtener por ID ──────────────────────────────────────────

    public VehiculoResponse obtenerPorId(Long id, Long tenantId) {
        Vehiculo v = vehiculoRepository.findById(id)
                .filter(x -> x.getTenant().getId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));
        return toResponse(v);
    }

    // ── Obtener por cliente ─────────────────────────────────────

    public List<VehiculoResponse> obtenerPorCliente(Long clienteId, Long tenantId) {
        return vehiculoRepository.findAllByClienteIdAndTenantId(clienteId, tenantId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Buscar por patente ──────────────────────────────────────

    public VehiculoResponse obtenerPorPatente(String patente, Long tenantId) {
        Vehiculo v = vehiculoRepository.findByPatenteAndTenantId(patente, tenantId)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));
        return toResponse(v);
    }

    // ── Crear ───────────────────────────────────────────────────

    public VehiculoResponse crear(VehiculoRequest request, Long tenantId) {
        if (vehiculoRepository.existsByPatenteAndTenantId(request.getPatente(), tenantId)) {
            throw new RuntimeException("Ya existe un vehículo con esa patente");
        }

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Taller no encontrado"));

        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .filter(c -> c.getTenant().getId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Vehiculo v = Vehiculo.builder()
                .patente(request.getPatente().toUpperCase())
                .marca(request.getMarca())
                .modelo(request.getModelo())
                .anio(request.getAnio())
                .kilometraje(request.getKilometraje())
                .color(request.getColor())
                .tipo(request.getTipo() != null ? request.getTipo() : Vehiculo.TipoVehiculo.AUTO)
                .cliente(cliente)
                .tenant(tenant)
                .build();

        return toResponse(vehiculoRepository.save(v));
    }

    // ── Actualizar ──────────────────────────────────────────────

    public VehiculoResponse actualizar(Long id, VehiculoRequest request, Long tenantId) {
        Vehiculo v = vehiculoRepository.findById(id)
                .filter(x -> x.getTenant().getId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));

        v.setPatente(request.getPatente().toUpperCase());
        v.setMarca(request.getMarca());
        v.setModelo(request.getModelo());
        v.setAnio(request.getAnio());
        v.setKilometraje(request.getKilometraje());
        v.setColor(request.getColor());
        if (request.getTipo() != null) v.setTipo(request.getTipo());

        return toResponse(vehiculoRepository.save(v));
    }

    // ── Eliminar ────────────────────────────────────────────────

    public void eliminar(Long id, Long tenantId) {
        Vehiculo v = vehiculoRepository.findById(id)
                .filter(x -> x.getTenant().getId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));
        vehiculoRepository.delete(v);
    }

    // ── Mapper ──────────────────────────────────────────────────

    private VehiculoResponse toResponse(Vehiculo v) {
        return VehiculoResponse.builder()
                .id(v.getId())
                .patente(v.getPatente())
                .marca(v.getMarca())
                .modelo(v.getModelo())
                .anio(v.getAnio())
                .kilometraje(v.getKilometraje())
                .color(v.getColor())
                .tipo(v.getTipo())
                .clienteId(v.getCliente().getId())
                .clienteNombre(v.getCliente().getNombre())
                .clienteApellido(v.getCliente().getApellido())
                .createdAt(v.getCreatedAt())
                .build();
    }
}