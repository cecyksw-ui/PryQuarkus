package com.resolutions.adapters.out.persistence;

import com.resolutions.application.ports.out.ClienteRepositoryPort;
import com.resolutions.model.Cliente;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ClienteJpaRepository implements ClienteRepositoryPort {

    @Inject
    EntityManager entityManager;

    @Override
    @Transactional
    public Integer save(Cliente cliente) {
        entityManager.persist(cliente);
        entityManager.flush();
        return cliente.getClienteId();
    }

    @Override
    public Optional<Cliente> findById(Integer clienteId) {
        Cliente cliente = entityManager.find(Cliente.class, clienteId);
        return Optional.ofNullable(cliente);
    }

    @Override
    public List<Cliente> findAll() {
        return entityManager.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
    }

    @Override
    @Transactional
    public void update(Integer clienteId, Cliente cliente) {
        Cliente existingCliente = entityManager.find(Cliente.class, clienteId);
        if (existingCliente != null) {
            existingCliente.setPersonaId(cliente.getPersonaId());
            existingCliente.setContrasena(cliente.getContrasena());
            existingCliente.setEstado(cliente.getEstado());
            entityManager.merge(existingCliente);
        }
    }

    @Override
    @Transactional
    public void deleteById(Integer clienteId) {
        Cliente cliente = entityManager.find(Cliente.class, clienteId);
        if (cliente != null) {
            entityManager.remove(cliente);
        }
    }

    @Override
    public boolean existsById(Integer clienteId) {
        return findById(clienteId).isPresent();
    }

    @Override
    public List<Cliente> findByEstado(Boolean estado) {
        return entityManager.createQuery("SELECT c FROM Cliente c WHERE c.estado = :estado", Cliente.class)
                .setParameter("estado", estado)
                .getResultList();
    }

    @Override
    public Optional<Cliente> findByPersonaId(Integer personaId) {
        List<Cliente> result = entityManager.createQuery("SELECT c FROM Cliente c WHERE c.personaId = :personaId", Cliente.class)
                .setParameter("personaId", personaId)
                .getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }
}