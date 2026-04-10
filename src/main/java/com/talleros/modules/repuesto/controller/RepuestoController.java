package com.talleros.modules.repuesto.controller;

import com.talleros.modules.repuesto.dto.RepuestoRequest;
import com.talleros.modules.repuesto.dto.RepuestoResponse;
import com.talleros.modules.repuesto.service.RepuestoService;
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

    // GET /api/repuestos?tenantId=1
    @GetMapping
    public ResponseEntity<List<RepuestoResponse>> obtenerTodos(@RequestParam Long tenantId) {
        return ResponseEntity.ok(repuestoService.obtenerTodos(tenantId));
    }

    // GET /api/repuestos/1?tenantId=1
    @GetMapping("/{id}")
    public ResponseEntity<RepuestoResponse> obtenerPorId(
            @PathVariable Long id,
            @RequestParam Long tenantId) {
        return ResponseEntity.ok(repuestoService.obtenerPorId(id, tenantId));
    }

    // GET /api/repuestos/buscar?nombre=filtro&tenantId=1
    @GetMapping("/buscar")
    public ResponseEntity<List<RepuestoResponse>> buscar(
            @RequestParam String nombre,
            @RequestParam Long tenantId) {
        return ResponseEntity.ok(repuestoService.buscar(nombre, tenantId));
    }

    // GET /api/repuestos/categoria/frenos?tenantId=1
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<RepuestoResponse>> obtenerPorCategoria(
            @PathVariable String categoria,
            @RequestParam Long tenantId) {
        return ResponseEntity.ok(repuestoService.obtenerPorCategoria(categoria, tenantId));
    }

    // GET /api/repuestos/stock-bajo?tenantId=1
    @GetMapping("/stock-bajo")
    public ResponseEntity<List<RepuestoResponse>> obtenerStockBajo(@RequestParam Long tenantId) {
        return ResponseEntity.ok(repuestoService.obtenerStockBajo(tenantId));
    }

    // POST /api/repuestos?tenantId=1
    @PostMapping
    public ResponseEntity<RepuestoResponse> crear(
            @RequestBody @Valid RepuestoRequest request,
            @RequestParam Long tenantId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(repuestoService.crear(request, tenantId));
    }

    // PUT /api/repuestos/1?tenantId=1
    @PutMapping("/{id}")
    public ResponseEntity<RepuestoResponse> actualizar(
            @PathVariable Long id,
            @RequestBody @Valid RepuestoRequest request,
            @RequestParam Long tenantId) {
        return ResponseEntity.ok(repuestoService.actualizar(id, request, tenantId));
    }

    // PATCH /api/repuestos/1/stock?cantidad=5&tenantId=1
    @PatchMapping("/{id}/stock")
    public ResponseEntity<RepuestoResponse> ajustarStock(
            @PathVariable Long id,
            @RequestParam int cantidad,
            @RequestParam Long tenantId) {
        return ResponseEntity.ok(repuestoService.ajustarStock(id, cantidad, tenantId));
    }

    // DELETE /api/repuestos/1?tenantId=1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id,
            @RequestParam Long tenantId) {
        repuestoService.eliminar(id, tenantId);
        return ResponseEntity.noContent().build();
    }
}