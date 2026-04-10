package com.talleros.modules.repuesto.service;

import com.talleros.modules.repuesto.dto.RepuestoRequest;
import com.talleros.modules.repuesto.dto.RepuestoResponse;
import com.talleros.modules.repuesto.model.Repuesto;
import com.talleros.modules.repuesto.repository.RepuestoRepository;
import com.talleros.modules.tenant.model.Tenant;
import com.talleros.modules.tenant.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RepuestoService {

    private final RepuestoRepository repuestoRepository;
    private final TenantRepository tenantRepository;

    // ── Obtener todos ───────────────────────────────────────────

    public List<RepuestoResponse> obtenerTodos(Long tenantId) {
        return repuestoRepository.findAllByTenantId(tenantId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Obtener por ID ──────────────────────────────────────────

    public RepuestoResponse obtenerPorId(Long id, Long tenantId) {
        Repuesto r = repuestoRepository.findById(id)
                .filter(x -> x.getTenant().getId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("Repuesto no encontrado"));
        return toResponse(r);
    }

    // ── Buscar por nombre ───────────────────────────────────────

    public List<RepuestoResponse> buscar(String nombre, Long tenantId) {
        return repuestoRepository.findByTenantIdAndNombreContainingIgnoreCase(tenantId, nombre)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Obtener por categoría ───────────────────────────────────

    public List<RepuestoResponse> obtenerPorCategoria(String categoria, Long tenantId) {
        return repuestoRepository.findAllByTenantIdAndCategoria(tenantId, categoria)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Stock bajo (dashboard) ──────────────────────────────────

    public List<RepuestoResponse> obtenerStockBajo(Long tenantId) {
        return repuestoRepository.findStockBajo(tenantId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Crear ───────────────────────────────────────────────────

    @Transactional
    public RepuestoResponse crear(RepuestoRequest request, Long tenantId) {
        if (request.getCodigo() != null &&
                repuestoRepository.existsByCodigoAndTenantId(request.getCodigo(), tenantId)) {
            throw new RuntimeException("Ya existe un repuesto con ese código");
        }

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Taller no encontrado"));

        Repuesto repuesto = Repuesto.builder()
                .nombre(request.getNombre())
                .codigo(request.getCodigo())
                .categoria(request.getCategoria())
                .descripcion(request.getDescripcion())
                .stock(request.getStock())
                .stockMinimo(request.getStockMinimo())
                .precioCosto(request.getPrecioCosto())
                .precioVenta(request.getPrecioVenta())
                .tenant(tenant)
                .build();

        return toResponse(repuestoRepository.save(repuesto));
    }

    // ── Actualizar ──────────────────────────────────────────────

    @Transactional
    public RepuestoResponse actualizar(Long id, RepuestoRequest request, Long tenantId) {
        Repuesto r = repuestoRepository.findById(id)
                .filter(x -> x.getTenant().getId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("Repuesto no encontrado"));

        r.setNombre(request.getNombre());
        r.setCodigo(request.getCodigo());
        r.setCategoria(request.getCategoria());
        r.setDescripcion(request.getDescripcion());
        r.setStock(request.getStock());
        r.setStockMinimo(request.getStockMinimo());
        r.setPrecioCosto(request.getPrecioCosto());
        r.setPrecioVenta(request.getPrecioVenta());

        return toResponse(repuestoRepository.save(r));
    }

    // ── Ajustar stock manualmente ───────────────────────────────

    @Transactional
    public RepuestoResponse ajustarStock(Long id, int cantidad, Long tenantId) {
        Repuesto r = repuestoRepository.findById(id)
                .filter(x -> x.getTenant().getId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("Repuesto no encontrado"));

        if (cantidad >= 0) {
            r.agregarStock(cantidad);
        } else {
            r.descontarStock(Math.abs(cantidad));
        }

        return toResponse(repuestoRepository.save(r));
    }

    // ── Eliminar ────────────────────────────────────────────────

    @Transactional
    public void eliminar(Long id, Long tenantId) {
        Repuesto r = repuestoRepository.findById(id)
                .filter(x -> x.getTenant().getId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("Repuesto no encontrado"));
        repuestoRepository.delete(r);
    }

    // ── Mapper ──────────────────────────────────────────────────

    private RepuestoResponse toResponse(Repuesto r) {
        return RepuestoResponse.builder()
                .id(r.getId())
                .nombre(r.getNombre())
                .codigo(r.getCodigo())
                .categoria(r.getCategoria())
                .descripcion(r.getDescripcion())
                .stock(r.getStock())
                .stockMinimo(r.getStockMinimo())
                .precioCosto(r.getPrecioCosto())
                .precioVenta(r.getPrecioVenta())
                .stockBajo(r.tieneStockBajo())
                .createdAt(r.getCreatedAt())
                .build();
    }
}