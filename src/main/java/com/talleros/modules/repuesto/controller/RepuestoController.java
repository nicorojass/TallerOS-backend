package com.talleros.modules.repuesto.controller;

import com.talleros.modules.repuesto.dto.RepuestoRequest;
import com.talleros.modules.repuesto.dto.RepuestoResponse;
import com.talleros.modules.repuesto.service.RepuestoService;
import com.talleros.multitenancy.TenantContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repuestos")
@RequiredArgsConstructor
public class RepuestoController {

    private final RepuestoService repuestoService;
    private final TenantContext tenantContext;

    @GetMapping
    public ResponseEntity<List<RepuestoResponse>> obtenerTodos() {
        return ResponseEntity.ok(repuestoService.obtenerTodos(tenantContext.getCurrentTenantId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RepuestoResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(repuestoService.obtenerPorId(id, tenantContext.getCurrentTenantId()));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<RepuestoResponse>> buscar(@RequestParam String nombre) {
        return ResponseEntity.ok(repuestoService.buscar(nombre, tenantContext.getCurrentTenantId()));
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<RepuestoResponse>> obtenerPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(repuestoService.obtenerPorCategoria(categoria, tenantContext.getCurrentTenantId()));
    }

    @GetMapping("/stock-bajo")
    public ResponseEntity<List<RepuestoResponse>> obtenerStockBajo() {
        return ResponseEntity.ok(repuestoService.obtenerStockBajo(tenantContext.getCurrentTenantId()));
    }

    @PostMapping
    public ResponseEntity<RepuestoResponse> crear(@RequestBody @Valid RepuestoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(repuestoService.crear(request, tenantContext.getCurrentTenantId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RepuestoResponse> actualizar(
            @PathVariable Long id,
            @RequestBody @Valid RepuestoRequest request) {
        return ResponseEntity.ok(repuestoService.actualizar(id, request, tenantContext.getCurrentTenantId()));
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<RepuestoResponse> ajustarStock(
            @PathVariable Long id,
            @RequestParam int cantidad) {
        return ResponseEntity.ok(repuestoService.ajustarStock(id, cantidad, tenantContext.getCurrentTenantId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        repuestoService.eliminar(id, tenantContext.getCurrentTenantId());
        return ResponseEntity.noContent().build();
    }
}