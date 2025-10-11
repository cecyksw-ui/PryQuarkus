package com.resolutions.application.ports.out;

import com.resolutions.model.Cliente;
import java.util.List;
import java.util.Optional;

public interface ClienteRepositoryPort {
    Integer save(Cliente cliente);
    Optional<Cliente> findById(Integer clienteId);
    List<Cliente> findAll();
    void update(Integer clienteId, Cliente cliente);
    void deleteById(Integer clienteId);
    boolean existsById(Integer clienteId);
    List<Cliente> findByEstado(Boolean estado);
    Optional<Cliente> findByPersonaId(String personaId);
}