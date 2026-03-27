package com.talleros.modules.cliente.repository;

import com.talleros.modules.cliente.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Todos los clientes de un taller
    List<Cliente> findAllByTenantId(Long tenantId);

    // Buscar cliente por teléfono dentro de un taller
    Optional<Cliente> findByTelefonoAndTenantId(String telefono, Long tenantId);

    // Verificar si ya existe el teléfono en el taller
    boolean existsByTelefonoAndTenantId(String telefono, Long tenantId);

    // Buscar por nombre o apellido (para el buscador)
    List<Cliente> findByTenantIdAndNombreContainingIgnoreCaseOrTenantIdAndApellidoContainingIgnoreCase(
        Long tenantId, String nombre, Long tenantId2, String apellido
    );
}
