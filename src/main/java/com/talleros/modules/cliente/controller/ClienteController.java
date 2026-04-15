package com.talleros.modules.cliente.controller;

import com.talleros.modules.cliente.dto.ClienteRequest;
import com.talleros.modules.cliente.dto.ClienteResponse;
import com.talleros.modules.cliente.service.ClienteService;
import com.talleros.multitenancy.TenantContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;
    private final TenantContext tenantContext;

    @PostMapping
    public ResponseEntity<ClienteResponse> crear(@RequestBody @Valid ClienteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clienteService.crear(request, tenantContext.getCurrentTenantId()));
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listar() {
        return ResponseEntity.ok(clienteService.listar(tenantContext.getCurrentTenantId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.obtenerPorId(id, tenantContext.getCurrentTenantId()));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ClienteResponse>> buscar(@RequestParam String query) {
        return ResponseEntity.ok(clienteService.buscarPorNombreYApellido(query, tenantContext.getCurrentTenantId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> actualizar(
            @PathVariable Long id,
            @RequestBody @Valid ClienteRequest request) {
        return ResponseEntity.ok(clienteService.actualizar(id, request, tenantContext.getCurrentTenantId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        clienteService.eliminar(id, tenantContext.getCurrentTenantId());
        return ResponseEntity.noContent().build();
    }
}