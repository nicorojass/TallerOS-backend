package com.talleros.modules.vehiculo.controller;

import com.talleros.modules.vehiculo.dto.VehiculoRequest;
import com.talleros.modules.vehiculo.dto.VehiculoResponse;
import com.talleros.modules.vehiculo.service.VehiculoService;
import com.talleros.multitenancy.TenantContext;
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
    private final TenantContext tenantContext;

    @GetMapping
    public ResponseEntity<List<VehiculoResponse>> obtenerTodos() {
        return ResponseEntity.ok(vehiculoService.obtenerTodos(tenantContext.getCurrentTenantId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehiculoResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(vehiculoService.obtenerPorId(id, tenantContext.getCurrentTenantId()));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<VehiculoResponse>> obtenerPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(vehiculoService.obtenerPorCliente(clienteId, tenantContext.getCurrentTenantId()));
    }

    @GetMapping("/patente/{patente}")
    public ResponseEntity<VehiculoResponse> obtenerPorPatente(@PathVariable String patente) {
        return ResponseEntity.ok(vehiculoService.obtenerPorPatente(patente, tenantContext.getCurrentTenantId()));
    }

    @PostMapping
    public ResponseEntity<VehiculoResponse> crear(@RequestBody @Valid VehiculoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(vehiculoService.crear(request, tenantContext.getCurrentTenantId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehiculoResponse> actualizar(
            @PathVariable Long id,
            @RequestBody @Valid VehiculoRequest request) {
        return ResponseEntity.ok(vehiculoService.actualizar(id, request, tenantContext.getCurrentTenantId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        vehiculoService.eliminar(id, tenantContext.getCurrentTenantId());
        return ResponseEntity.noContent().build();
    }
}