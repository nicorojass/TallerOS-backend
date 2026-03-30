package com.talleros.modules.vehiculo.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.talleros.modules.vehiculo.model.Vehiculo;
import java.util.List;
import java.util.Optional;


@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    // Todos los vehículos de un taller
    List<Vehiculo> findAllByTenantId(Long tenantId);

    // Vehículos de un cliente específico
    List<Vehiculo> findAllByClienteIdAndTenantId(Long clienteId, Long tenantId);

    // Buscar por patente dentro del taller
    Optional<Vehiculo> findByPatenteAndTenantId(String patente, Long tenantId);

    // Verificar si ya existe la patente en el taller
    boolean existsByPatenteAndTenantId(String patente, Long tenantId);
}
