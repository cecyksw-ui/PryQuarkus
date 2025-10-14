package com.resolutions.application.useCases;

import com.resolutions.application.ports.out.ClienteRepositoryPort;
import com.resolutions.application.ports.out.PersonaRepositoryPort;
import com.resolutions.model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para ClienteUseCaseImpl
 * Valida toda la lógica de negocio para operaciones CRUD de clientes
 */
@ExtendWith(MockitoExtension.class)
class ClienteUseCaseImplTest {

    @Mock
    private ClienteRepositoryPort clienteRepository;

    @Mock
    private PersonaRepositoryPort personaRepository;

    private ClienteUseCaseImpl clienteUseCase;

    private Cliente cliente1;
    private Cliente cliente2;
    private Cliente cliente3;

    @BeforeEach
    void setUp() {
        clienteUseCase = new ClienteUseCaseImpl();
        clienteUseCase.clienteRepository = clienteRepository;
        clienteUseCase.personaRepository = personaRepository;
        
        // Datos de prueba basados en los casos de uso reales
        // Cliente 1: Jose Lema
        cliente1 = new Cliente(1, 1, "1234", true);
        
        // Cliente 2: Marianela Montalvo  
        cliente2 = new Cliente(2, 2, "5678", true);
        
        // Cliente 3: Juan Osorio
        cliente3 = new Cliente(3, 3, "1245", true);
    }

    @Test
    @DisplayName("Crear cliente exitosamente - Jose Lema")
    void testCreateCliente_JoseLema_Success() {
        // Given - Cliente 1: Jose Lema
        when(personaRepository.existsById(1)).thenReturn(true);
        when(clienteRepository.findByPersonaId(1)).thenReturn(Optional.empty());
        when(clienteRepository.save(any(Cliente.class))).thenReturn(1);

        // When
        Integer result = clienteUseCase.createCliente(cliente1);

        // Then
        assertEquals(1, result);
        verify(personaRepository).existsById(1);
        verify(clienteRepository).findByPersonaId(1);
        verify(clienteRepository).save(cliente1);
    }

    @Test
    @DisplayName("Crear cliente exitosamente - Marianela Montalvo")
    void testCreateCliente_MarianelaMontalvo_Success() {
        // Given - Cliente 2: Marianela Montalvo
        when(personaRepository.existsById(2)).thenReturn(true);
        when(clienteRepository.findByPersonaId(2)).thenReturn(Optional.empty());
        when(clienteRepository.save(any(Cliente.class))).thenReturn(2);

        // When
        Integer result = clienteUseCase.createCliente(cliente2);

        // Then
        assertEquals(2, result);
        assertEquals("5678", cliente2.getContrasena());
        verify(personaRepository).existsById(2);
        verify(clienteRepository).findByPersonaId(2);
        verify(clienteRepository).save(cliente2);
    }

    @Test
    @DisplayName("Error al crear cliente - Persona no existe")
    void testCreateCliente_PersonaNotExists_ThrowsException() {
        // Given
        when(personaRepository.existsById(999)).thenReturn(false);

        Cliente clienteInvalido = new Cliente(null, 999, "password", true);

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> clienteUseCase.createCliente(clienteInvalido)
        );

        assertEquals("No existe una persona con ID: 999", exception.getMessage());
        verify(personaRepository).existsById(999);
        verify(clienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Error al crear cliente - Contraseña nula")
    void testCreateCliente_NullPassword_ThrowsException() {
        // Given
        Cliente clienteInvalido = new Cliente(null, 1, null, true);

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> clienteUseCase.createCliente(clienteInvalido)
        );

        assertEquals("La contraseña es requerida", exception.getMessage());
        verifyNoInteractions(personaRepository, clienteRepository);
    }

    @Test
    @DisplayName("Obtener cliente por ID exitosamente")
    void testGetClienteById_Success() {
        // Given
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente1));

        // When
        Cliente result = clienteUseCase.getClienteById(1);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getClienteId());
        assertEquals(1, result.getPersonaId());
        assertEquals("1234", result.getContrasena());
        assertTrue(result.getEstado());
        verify(clienteRepository).findById(1);
    }

    @Test
    @DisplayName("Error al obtener cliente - No encontrado")
    void testGetClienteById_NotFound_ThrowsException() {
        // Given
        when(clienteRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> clienteUseCase.getClienteById(999)
        );

        assertEquals("Cliente no encontrado con ID: 999", exception.getMessage());
        verify(clienteRepository).findById(999);
    }

    @Test
    @DisplayName("Obtener todos los clientes exitosamente")
    void testGetAllClientes_Success() {
        // Given
        List<Cliente> expectedClientes = Arrays.asList(cliente1, cliente2, cliente3);
        when(clienteRepository.findAll()).thenReturn(expectedClientes);

        // When
        List<Cliente> result = clienteUseCase.getAllClientes();

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(clienteRepository).findAll();
    }

    @Test
    @DisplayName("Actualizar cliente exitosamente")
    void testUpdateCliente_Success() {
        // Given
        Cliente clienteActualizado = new Cliente(1, 1, "nueva1234", false);
        when(clienteRepository.existsById(1)).thenReturn(true);

        // When
        clienteUseCase.updateCliente(clienteActualizado);

        // Then
        verify(clienteRepository).existsById(1);
        verify(clienteRepository).update(1, clienteActualizado);
    }

    @Test
    @DisplayName("Eliminar cliente exitosamente")
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
    @DisplayName("Obtener clientes por estado activo")
    void testGetClientesByEstado_Active_Success() {
        // Given
        List<Cliente> clientesActivos = Arrays.asList(cliente1, cliente2, cliente3);
        when(clienteRepository.findByEstado(true)).thenReturn(clientesActivos);

        // When
        List<Cliente> result = clienteUseCase.getClientesByEstado(true);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.stream().allMatch(Cliente::getEstado));
        verify(clienteRepository).findByEstado(true);
    }
}