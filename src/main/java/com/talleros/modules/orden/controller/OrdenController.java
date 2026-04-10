package com.talleros.modules.orden.controller;

import com.talleros.modules.orden.dto.OrdenRequest;
import com.talleros.modules.orden.dto.OrdenResponse;
import com.talleros.modules.orden.model.OrdenDeTrabajo.EstadoOT;
import com.talleros.modules.orden.service.OrdenService;
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

    // GET /api/ordenes?tenantId=1
    @GetMapping
    public ResponseEntity<List<OrdenResponse>> obtenerTodas(@RequestParam Long tenantId) {
        return ResponseEntity.ok(ordenService.obtenerTodas(tenantId));
    }

    // GET /api/ordenes/1?tenantId=1
    @GetMapping("/{id}")
    public ResponseEntity<OrdenResponse> obtenerPorId(
            @PathVariable Long id,
            @RequestParam Long tenantId) {
        return ResponseEntity.ok(ordenService.obtenerPorId(id, tenantId));
    }

    // GET /api/ordenes/estado/EN_PROCESO?tenantId=1  (Kanban)
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<OrdenResponse>> obtenerPorEstado(
            @PathVariable EstadoOT estado,
            @RequestParam Long tenantId) {
        return ResponseEntity.ok(ordenService.obtenerPorEstado(estado, tenantId));
    }

    // GET /api/ordenes/vehiculo/1?tenantId=1  (historial)
    @GetMapping("/vehiculo/{vehiculoId}")
    public ResponseEntity<List<OrdenResponse>> obtenerPorVehiculo(
            @PathVariable Long vehiculoId,
            @RequestParam Long tenantId) {
        return ResponseEntity.ok(ordenService.obtenerPorVehiculo(vehiculoId, tenantId));
    }

    // GET /api/ordenes/cliente/1?tenantId=1
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<OrdenResponse>> obtenerPorCliente(
            @PathVariable Long clienteId,
            @RequestParam Long tenantId) {
        return ResponseEntity.ok(ordenService.obtenerPorCliente(clienteId, tenantId));
    }

    // GET /api/ordenes/hoy?tenantId=1  (dashboard)
    @GetMapping("/hoy")
    public ResponseEntity<List<OrdenResponse>> obtenerDelDia(@RequestParam Long tenantId) {
        return ResponseEntity.ok(ordenService.obtenerDelDia(tenantId));
    }

    // POST /api/ordenes?tenantId=1
    @PostMapping
    public ResponseEntity<OrdenResponse> crear(
            @RequestBody @Valid OrdenRequest request,
            @RequestParam Long tenantId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ordenService.crear(request, tenantId));
    }

    // PATCH /api/ordenes/1/estado?nuevoEstado=EN_PROCESO&tenantId=1
    @PatchMapping("/{id}/estado")
    public ResponseEntity<OrdenResponse> cambiarEstado(
            @PathVariable Long id,
            @RequestParam EstadoOT nuevoEstado,
            @RequestParam Long tenantId) {
        return ResponseEntity.ok(ordenService.cambiarEstado(id, nuevoEstado, tenantId));
    }

    // PUT /api/ordenes/1?tenantId=1
    @PutMapping("/{id}")
    public ResponseEntity<OrdenResponse> actualizar(
            @PathVariable Long id,
            @RequestBody @Valid OrdenRequest request,
            @RequestParam Long tenantId) {
        return ResponseEntity.ok(ordenService.actualizar(id, request, tenantId));
    }

    // DELETE /api/ordenes/1?tenantId=1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id,
            @RequestParam Long tenantId) {
        ordenService.eliminar(id, tenantId);
        return ResponseEntity.noContent().build();
    }
}