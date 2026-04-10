package com.talleros.modules.turno.controller;

import com.talleros.modules.turno.dto.TurnoRequest;
import com.talleros.modules.turno.dto.TurnoResponse;
import com.talleros.modules.turno.model.Turno.EstadoTurno;
import com.talleros.modules.turno.service.TurnoService;
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

    // GET /api/turnos?tenantId=1
    @GetMapping
    public ResponseEntity<List<TurnoResponse>> obtenerTodos(@RequestParam Long tenantId) {
        return ResponseEntity.ok(turnoService.obtenerTodos(tenantId));
    }

    // GET /api/turnos/1?tenantId=1
    @GetMapping("/{id}")
    public ResponseEntity<TurnoResponse> obtenerPorId(
            @PathVariable Long id,
            @RequestParam Long tenantId) {
        return ResponseEntity.ok(turnoService.obtenerPorId(id, tenantId));
    }

    // GET /api/turnos/estado/PENDIENTE?tenantId=1
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<TurnoResponse>> obtenerPorEstado(
            @PathVariable EstadoTurno estado,
            @RequestParam Long tenantId) {
        return ResponseEntity.ok(turnoService.obtenerPorEstado(estado, tenantId));
    }

    // GET /api/turnos/cliente/1?tenantId=1
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<TurnoResponse>> obtenerPorCliente(
            @PathVariable Long clienteId,
            @RequestParam Long tenantId) {
        return ResponseEntity.ok(turnoService.obtenerPorCliente(clienteId, tenantId));
    }

    // GET /api/turnos/hoy?tenantId=1
    @GetMapping("/hoy")
    public ResponseEntity<List<TurnoResponse>> obtenerDelDia(@RequestParam Long tenantId) {
        return ResponseEntity.ok(turnoService.obtenerDelDia(tenantId));
    }

    // GET /api/turnos/rango?inicio=2025-01-01T00:00:00&fin=2025-01-07T23:59:59&tenantId=1
    @GetMapping("/rango")
    public ResponseEntity<List<TurnoResponse>> obtenerPorRango(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin,
            @RequestParam Long tenantId) {
        return ResponseEntity.ok(turnoService.obtenerPorRango(inicio, fin, tenantId));
    }

    // POST /api/turnos?tenantId=1
    @PostMapping
    public ResponseEntity<TurnoResponse> crear(
            @RequestBody @Valid TurnoRequest request,
            @RequestParam Long tenantId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(turnoService.crear(request, tenantId));
    }

    // PATCH /api/turnos/1/estado?nuevoEstado=CONFIRMADO&tenantId=1
    @PatchMapping("/{id}/estado")
    public ResponseEntity<TurnoResponse> cambiarEstado(
            @PathVariable Long id,
            @RequestParam EstadoTurno nuevoEstado,
            @RequestParam Long tenantId) {
        return ResponseEntity.ok(turnoService.cambiarEstado(id, nuevoEstado, tenantId));
    }

    // PUT /api/turnos/1?tenantId=1
    @PutMapping("/{id}")
    public ResponseEntity<TurnoResponse> actualizar(
            @PathVariable Long id,
            @RequestBody @Valid TurnoRequest request,
            @RequestParam Long tenantId) {
        return ResponseEntity.ok(turnoService.actualizar(id, request, tenantId));
    }

    // DELETE /api/turnos/1?tenantId=1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id,
            @RequestParam Long tenantId) {
        turnoService.eliminar(id, tenantId);
        return ResponseEntity.noContent().build();
    }
}