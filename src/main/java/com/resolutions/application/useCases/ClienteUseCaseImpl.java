package com.resolutions.application.useCases;

import com.resolutions.application.ports.in.ClienteUseCase;
import com.resolutions.application.ports.out.ClienteRepositoryPort;
import com.resolutions.application.ports.out.PersonaRepositoryPort;
import com.resolutions.model.Cliente;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ClienteUseCaseImpl implements ClienteUseCase {

    @Inject
    ClienteRepositoryPort clienteRepository;

    @Inject
    PersonaRepositoryPort personaRepository;

    @Override
    public Integer createCliente(Cliente cliente) {
        if (cliente.getPersonaId() == null || cliente.getPersonaId().trim().isEmpty()) {
            throw new IllegalArgumentException("El ID de persona es requerido");
        }
        if (cliente.getContrasena() == null || cliente.getContrasena().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es requerida");
        }
        if (!personaRepository.existsById(cliente.getPersonaId())) {
            throw new IllegalArgumentException("No existe una persona con ID: " + cliente.getPersonaId());
        }
        if (clienteRepository.findByPersonaId(cliente.getPersonaId()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un cliente para la persona con ID: " + cliente.getPersonaId());
        }
        if (cliente.getEstado() == null) {
            cliente.setEstado(true);
        }
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente getClienteById(Integer clienteId) {
        return clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + clienteId));
    }

    @Override
    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    @Override
    public void updateCliente(Integer clienteId, Cliente cliente) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new RuntimeException("Cliente no encontrado con ID: " + clienteId);
        }
        cliente.setClienteId(clienteId);
        clienteRepository.update(clienteId, cliente);
    }

    @Override
    public void deleteCliente(Integer clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new RuntimeException("Cliente no encontrado con ID: " + clienteId);
        }
        clienteRepository.deleteById(clienteId);
    }

    @Override
    public List<Cliente> getClientesByEstado(Boolean estado) {
        return clienteRepository.findByEstado(estado);
    }
}