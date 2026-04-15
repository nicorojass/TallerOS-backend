package com.talleros.modules.turno.controller;

import com.talleros.modules.turno.dto.TurnoRequest;
import com.talleros.modules.turno.dto.TurnoResponse;
import com.talleros.modules.turno.model.Turno.EstadoTurno;
import com.talleros.modules.turno.service.TurnoService;
import com.talleros.multitenancy.TenantContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/turnos")
@RequiredArgsConstructor
public class TurnoController {

    private final TurnoService turnoService;
    private final TenantContext tenantContext;

    @GetMapping
    public ResponseEntity<List<TurnoResponse>> obtenerTodos() {
        return ResponseEntity.ok(turnoService.obtenerTodos(tenantContext.getCurrentTenantId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TurnoResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(turnoService.obtenerPorId(id, tenantContext.getCurrentTenantId()));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<TurnoResponse>> obtenerPorEstado(@PathVariable EstadoTurno estado) {
        return ResponseEntity.ok(turnoService.obtenerPorEstado(estado, tenantContext.getCurrentTenantId()));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<TurnoResponse>> obtenerPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(turnoService.obtenerPorCliente(clienteId, tenantContext.getCurrentTenantId()));
    }

    @GetMapping("/hoy")
    public ResponseEntity<List<TurnoResponse>> obtenerDelDia() {
        return ResponseEntity.ok(turnoService.obtenerDelDia(tenantContext.getCurrentTenantId()));
    }

    @GetMapping("/rango")
    public ResponseEntity<List<TurnoResponse>> obtenerPorRango(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(turnoService.obtenerPorRango(inicio, fin, tenantContext.getCurrentTenantId()));
    }

    @PostMapping
    public ResponseEntity<TurnoResponse> crear(@RequestBody @Valid TurnoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(turnoService.crear(request, tenantContext.getCurrentTenantId()));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<TurnoResponse> cambiarEstado(
            @PathVariable Long id,
            @RequestParam EstadoTurno nuevoEstado) {
        return ResponseEntity.ok(turnoService.cambiarEstado(id, nuevoEstado, tenantContext.getCurrentTenantId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TurnoResponse> actualizar(
            @PathVariable Long id,
            @RequestBody @Valid TurnoRequest request) {
        return ResponseEntity.ok(turnoService.actualizar(id, request, tenantContext.getCurrentTenantId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        turnoService.eliminar(id, tenantContext.getCurrentTenantId());
        return ResponseEntity.noContent().build();
    }
}