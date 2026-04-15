package com.talleros.modules.orden.controller;

import com.talleros.modules.orden.dto.OrdenRequest;
import com.talleros.modules.orden.dto.OrdenResponse;
import com.talleros.modules.orden.model.OrdenDeTrabajo.EstadoOT;
import com.talleros.modules.orden.service.OrdenService;
import com.talleros.multitenancy.TenantContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
@RequiredArgsConstructor
public class OrdenController {

    private final OrdenService ordenService;
    private final TenantContext tenantContext;

    @GetMapping
    public ResponseEntity<List<OrdenResponse>> obtenerTodas() {
        return ResponseEntity.ok(ordenService.obtenerTodas(tenantContext.getCurrentTenantId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ordenService.obtenerPorId(id, tenantContext.getCurrentTenantId()));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<OrdenResponse>> obtenerPorEstado(@PathVariable EstadoOT estado) {
        return ResponseEntity.ok(ordenService.obtenerPorEstado(estado, tenantContext.getCurrentTenantId()));
    }

    @GetMapping("/vehiculo/{vehiculoId}")
    public ResponseEntity<List<OrdenResponse>> obtenerPorVehiculo(@PathVariable Long vehiculoId) {
        return ResponseEntity.ok(ordenService.obtenerPorVehiculo(vehiculoId, tenantContext.getCurrentTenantId()));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<OrdenResponse>> obtenerPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(ordenService.obtenerPorCliente(clienteId, tenantContext.getCurrentTenantId()));
    }

    @GetMapping("/hoy")
    public ResponseEntity<List<OrdenResponse>> obtenerDelDia() {
        return ResponseEntity.ok(ordenService.obtenerDelDia(tenantContext.getCurrentTenantId()));
    }

    @PostMapping
    public ResponseEntity<OrdenResponse> crear(@RequestBody @Valid OrdenRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ordenService.crear(request, tenantContext.getCurrentTenantId()));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<OrdenResponse> cambiarEstado(
            @PathVariable Long id,
            @RequestParam EstadoOT nuevoEstado) {
        return ResponseEntity.ok(ordenService.cambiarEstado(id, nuevoEstado, tenantContext.getCurrentTenantId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrdenResponse> actualizar(
            @PathVariable Long id,
            @RequestBody @Valid OrdenRequest request) {
        return ResponseEntity.ok(ordenService.actualizar(id, request, tenantContext.getCurrentTenantId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ordenService.eliminar(id, tenantContext.getCurrentTenantId());
        return ResponseEntity.noContent().build();
    }
}