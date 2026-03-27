package com.talleros.modules.cliente.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.talleros.modules.cliente.dto.ClienteRequest;
import com.talleros.modules.cliente.dto.ClienteResponse;
import com.talleros.modules.cliente.model.Cliente;
import com.talleros.modules.cliente.repository.ClienteRepository;
import com.talleros.modules.tenant.model.Tenant;
import com.talleros.modules.tenant.repository.TenantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteService {
    
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private TenantRepository tenantRepository;
    
    // MAPPER 
    public ClienteResponse toResponse(Cliente cliente) {
        return ClienteResponse.builder()
                .id(cliente.getId())
                .nombre(cliente.getNombre())
                .apellido(cliente.getApellido())
                .telefono(cliente.getTelefono())
                .email(cliente.getEmail())
                .dni(cliente.getDni())
                .build();
    }

    public ClienteResponse crear(ClienteRequest request, Long tenantId) {
        if (clienteRepository.existsByTelefonoAndTenantId(request.getTelefono(), tenantId)) {
            throw new RuntimeException("Ya existe un cliente con ese teléfono");
        }

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Taller no encontrado"));

        Cliente cliente = Cliente.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .telefono(request.getTelefono())
                .email(request.getEmail())
                .dni(request.getDni())
                .tenant(tenant)
                .build();

        return toResponse(clienteRepository.save(cliente));
    }

    public List<ClienteResponse> listar(Long tenantId) {
        return clienteRepository.findAllByTenantId(tenantId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ClienteResponse obtenerPorId(Long id, Long tenantId) {
        Cliente cliente = clienteRepository.findById(id)
                .filter(c -> c.getTenant().getId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        return toResponse(cliente);
    }

    public List<ClienteResponse> buscarPorNombreYApellido(String texto, Long tenantId) {
        return clienteRepository
                .findByTenantIdAndNombreContainingIgnoreCaseOrTenantIdAndApellidoContainingIgnoreCase(
                        tenantId, texto, tenantId, texto)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ClienteResponse actualizar(Long id, ClienteRequest request, Long tenantId) {
        Cliente cliente = clienteRepository.findById(id)
                .filter(c -> c.getTenant().getId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        cliente.setNombre(request.getNombre());
        cliente.setApellido(request.getApellido());
        cliente.setTelefono(request.getTelefono());
        cliente.setEmail(request.getEmail());
        cliente.setDni(request.getDni());

        return toResponse(clienteRepository.save(cliente));
    }

    public void eliminar(Long id, Long tenantId) {
        Cliente cliente = clienteRepository.findById(id)
                .filter(c -> c.getTenant().getId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        clienteRepository.delete(cliente);
    }


}
