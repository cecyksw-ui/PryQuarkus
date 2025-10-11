package com.resolutions.application.useCases;

import com.resolutions.application.ports.out.ClienteRepositoryPort;
import com.resolutions.application.ports.out.PersonaRepositoryPort;
import com.resolutions.model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteUseCaseImplTest {

    @Mock
    private ClienteRepositoryPort clienteRepository;

    @Mock
    private PersonaRepositoryPort personaRepository;

    private ClienteUseCaseImpl clienteUseCase;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        clienteUseCase = new ClienteUseCaseImpl();
        clienteUseCase.clienteRepository = clienteRepository;
        clienteUseCase.personaRepository = personaRepository;
        cliente = new Cliente(1, "PER001", "password123", true);
    }

    @Test
    void testCreateCliente_Success() {
        // Given
        when(personaRepository.existsById("PER001")).thenReturn(true);
        when(clienteRepository.findByPersonaId("PER001")).thenReturn(Optional.empty());
        when(clienteRepository.save(any(Cliente.class))).thenReturn(1);

        // When
        Integer result = clienteUseCase.createCliente(cliente);

        // Then
        assertEquals(1, result);
        verify(personaRepository).existsById("PER001");
        verify(clienteRepository).findByPersonaId("PER001");
        verify(clienteRepository).save(cliente);
    }

    @Test
    void testCreateCliente_ThrowsException_WhenPersonaIdIsNull() {
        // Given
        cliente.setPersonaId(null);

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> clienteUseCase.createCliente(cliente)
        );
        assertEquals("El ID de persona es requerido", exception.getMessage());
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void testCreateCliente_ThrowsException_WhenContrasenaIsNull() {
        // Given
        cliente.setContrasena(null);

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> clienteUseCase.createCliente(cliente)
        );
        assertEquals("La contraseña es requerida", exception.getMessage());
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void testCreateCliente_ThrowsException_WhenPersonaNotExists() {
        // Given
        when(personaRepository.existsById("PER001")).thenReturn(false);

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> clienteUseCase.createCliente(cliente)
        );
        assertEquals("No existe una persona con ID: PER001", exception.getMessage());
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void testCreateCliente_ThrowsException_WhenClienteAlreadyExists() {
        // Given
        when(personaRepository.existsById("PER001")).thenReturn(true);
        when(clienteRepository.findByPersonaId("PER001")).thenReturn(Optional.of(cliente));

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> clienteUseCase.createCliente(cliente)
        );
        assertEquals("Ya existe un cliente para la persona con ID: PER001", exception.getMessage());
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void testCreateCliente_SetsDefaultEstado() {
        // Given
        cliente.setEstado(null);
        when(personaRepository.existsById("PER001")).thenReturn(true);
        when(clienteRepository.findByPersonaId("PER001")).thenReturn(Optional.empty());
        when(clienteRepository.save(any(Cliente.class))).thenReturn(1);

        // When
        clienteUseCase.createCliente(cliente);

        // Then
        assertTrue(cliente.getEstado());
        verify(clienteRepository).save(cliente);
    }

    @Test
    void testGetClienteById_Success() {
        // Given
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));

        // When
        Cliente result = clienteUseCase.getClienteById(1);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getClienteId());
        assertEquals("PER001", result.getPersonaId());
        verify(clienteRepository).findById(1);
    }

    @Test
    void testGetClienteById_ThrowsException_WhenNotFound() {
        // Given
        when(clienteRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> clienteUseCase.getClienteById(999)
        );
        assertEquals("Cliente no encontrado con ID: 999", exception.getMessage());
    }

    @Test
    void testGetAllClientes_Success() {
        // Given
        List<Cliente> clientes = Arrays.asList(
            new Cliente(1, "PER001", "pass1", true),
            new Cliente(2, "PER002", "pass2", false)
        );
        when(clienteRepository.findAll()).thenReturn(clientes);

        // When
        List<Cliente> result = clienteUseCase.getAllClientes();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(clienteRepository).findAll();
    }

    @Test
    void testGetClientesByEstado_Success() {
        // Given
        List<Cliente> clientesActivos = Arrays.asList(
            new Cliente(1, "PER001", "pass1", true)
        );
        when(clienteRepository.findByEstado(true)).thenReturn(clientesActivos);

        // When
        List<Cliente> result = clienteUseCase.getClientesByEstado(true);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getEstado());
        verify(clienteRepository).findByEstado(true);
    }

    @Test
    void testUpdateCliente_Success() {
        // Given
        when(clienteRepository.existsById(1)).thenReturn(true);

        // When
        clienteUseCase.updateCliente(1, cliente);

        // Then
        verify(clienteRepository).existsById(1);
        verify(clienteRepository).update(1, cliente);
        assertEquals(1, cliente.getClienteId());
    }

    @Test
    void testUpdateCliente_ThrowsException_WhenNotFound() {
        // Given
        when(clienteRepository.existsById(999)).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> clienteUseCase.updateCliente(999, cliente)
        );
        assertEquals("Cliente no encontrado con ID: 999", exception.getMessage());
        verify(clienteRepository, never()).update(anyInt(), any());
    }

    @Test
    void testDeleteCliente_Success() {
        // Given
        when(clienteRepository.existsById(1)).thenReturn(true);

        // When
        clienteUseCase.deleteCliente(1);

        // Then
        verify(clienteRepository).existsById(1);
        verify(clienteRepository).deleteById(1);
    }

    @Test
    void testDeleteCliente_ThrowsException_WhenNotFound() {
        // Given
        when(clienteRepository.existsById(999)).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> clienteUseCase.deleteCliente(999)
        );
        assertEquals("Cliente no encontrado con ID: 999", exception.getMessage());
        verify(clienteRepository, never()).deleteById(anyInt());
    }
}