package com.talleros.modules.cliente.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.talleros.modules.cliente.dto.ClienteRequest;
import com.talleros.modules.cliente.dto.ClienteResponse;
import com.talleros.modules.cliente.service.ClienteService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {
    
    @Autowired 
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteResponse> crear(
            @RequestBody @Valid ClienteRequest request,
            @RequestParam Long tenantId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clienteService.crear(request, tenantId));
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listar(@RequestParam Long tenantId) {
        return ResponseEntity.ok(clienteService.listar(tenantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> obtenerPorId(@PathVariable Long id, @RequestParam Long tenantId) {
        return ResponseEntity.ok(clienteService.obtenerPorId(id, tenantId));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ClienteResponse>> buscar(
            @RequestParam String query,
            @RequestParam Long tenantId) {
        return ResponseEntity.ok(clienteService.buscarPorNombreYApellido(query, tenantId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> actualizar(
            @PathVariable Long id,
            @RequestBody @Valid ClienteRequest request,
            @RequestParam Long tenantId) {
        return ResponseEntity.ok(clienteService.actualizar(id, request, tenantId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id, @RequestParam Long tenantId) {
        clienteService.eliminar(id, tenantId);
        return ResponseEntity.noContent().build();
    }

}
