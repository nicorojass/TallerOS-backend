package com.talleros.modules.vehiculo.controller;

import com.talleros.modules.vehiculo.dto.VehiculoRequest;
import com.talleros.modules.vehiculo.dto.VehiculoResponse;
import com.talleros.modules.vehiculo.service.VehiculoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
@RequiredArgsConstructor
public class VehiculoController {

    private final VehiculoService vehiculoService;

    // GET /api/vehiculos?tenantId=1
    @GetMapping
    public ResponseEntity<List<VehiculoResponse>> obtenerTodos(@RequestParam Long tenantId) {
        return ResponseEntity.ok(vehiculoService.obtenerTodos(tenantId));
    }

    // GET /api/vehiculos/1?tenantId=1
    @GetMapping("/{id}")
    public ResponseEntity<VehiculoResponse> obtenerPorId(
            @PathVariable Long id,
            @RequestParam Long tenantId) {
        return ResponseEntity.ok(vehiculoService.obtenerPorId(id, tenantId));
    }

    // GET /api/vehiculos/cliente/1?tenantId=1
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<VehiculoResponse>> obtenerPorCliente(
            @PathVariable Long clienteId,
            @RequestParam Long tenantId) {
        return ResponseEntity.ok(vehiculoService.obtenerPorCliente(clienteId, tenantId));
    }

    // GET /api/vehiculos/patente/ABC123?tenantId=1
    @GetMapping("/patente/{patente}")
    public ResponseEntity<VehiculoResponse> obtenerPorPatente(
            @PathVariable String patente,
            @RequestParam Long tenantId) {
        return ResponseEntity.ok(vehiculoService.obtenerPorPatente(patente, tenantId));
    }

    // POST /api/vehiculos?tenantId=1
    @PostMapping
    public ResponseEntity<VehiculoResponse> crear(
            @RequestBody @Valid VehiculoRequest request,
            @RequestParam Long tenantId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(vehiculoService.crear(request, tenantId));
    }

    // PUT /api/vehiculos/1?tenantId=1
    @PutMapping("/{id}")
    public ResponseEntity<VehiculoResponse> actualizar(
            @PathVariable Long id,
            @RequestBody @Valid VehiculoRequest request,
            @RequestParam Long tenantId) {
        return ResponseEntity.ok(vehiculoService.actualizar(id, request, tenantId));
    }

    // DELETE /api/vehiculos/1?tenantId=1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id,
            @RequestParam Long tenantId) {
        vehiculoService.eliminar(id, tenantId);
        return ResponseEntity.noContent().build();
    }
}