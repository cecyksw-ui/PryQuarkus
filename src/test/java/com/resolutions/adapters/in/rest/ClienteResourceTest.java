package com.resolutions.adapters.in.rest;

import com.resolutions.adapters.in.rest.dto.ClienteRequestDto;
import com.resolutions.adapters.in.rest.dto.ClienteResponseDto;
import com.resolutions.application.ports.in.ClienteUseCase;
import com.resolutions.application.ports.in.PersonaUseCase;
import com.resolutions.model.Cliente;
import com.resolutions.model.Persona;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para ClienteResource
 * Valida el comportamiento de los endpoints REST para gestión de clientes
 */
@ExtendWith(MockitoExtension.class)
class ClienteResourceTest {

    @Mock
    private ClienteUseCase clienteUseCase;

    @Mock
    private PersonaUseCase personaUseCase;

    @Mock
    private Logger logger;

    private ClienteResource clienteResource;

    private ClienteRequestDto clienteRequestDto;
    private Cliente cliente;
    private Persona persona;

    @BeforeEach
    void setUp() {
        clienteResource = new ClienteResource();
        clienteResource.clienteUseCase = clienteUseCase;
        clienteResource.personaUseCase = personaUseCase;
        clienteResource.logger = logger;

        // Datos de prueba
        clienteRequestDto = new ClienteRequestDto();
        clienteRequestDto.setNombres("Jose Lema");
        clienteRequestDto.setDireccion("Otavalo sn y principal");
        clienteRequestDto.setTelefono("098254785");
        clienteRequestDto.setGenero("M");
        clienteRequestDto.setEdad(30);
        clienteRequestDto.setContrasena("1234");
        clienteRequestDto.setEstado(true);

        persona = new Persona(1, "Jose Lema", "M", 30, "Otavalo sn y principal", "098254785");
        cliente = new Cliente(1, 1, "1234", true);
        cliente.setPersona(persona);
    }

    @Test
    @DisplayName("Crear cliente exitosamente - Jose Lema")
    void testCreateCliente_Success() {
        // Given
        when(personaUseCase.createPersona(any(Persona.class))).thenReturn(1);
        when(clienteUseCase.createCliente(any(Cliente.class))).thenReturn(1);

        // When
        Response response = clienteResource.createCliente(clienteRequestDto);

        // Then
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        Map<String, Object> responseEntity = (Map<String, Object>) response.getEntity();
        assertEquals(1, responseEntity.get("clienteId"));
        assertEquals(1, responseEntity.get("personaId"));
        assertEquals("Cliente creado exitosamente", responseEntity.get("message"));

        verify(personaUseCase).createPersona(any(Persona.class));
        verify(clienteUseCase).createCliente(any(Cliente.class));
    }

    @Test
    @DisplayName("Error al crear cliente - Nombres faltantes")
    void testCreateCliente_MissingNombres_BadRequest() {
        // Given
        clienteRequestDto.setNombres(null);

        // When
        Response response = clienteResource.createCliente(clienteRequestDto);

        // Then
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        
        Map<String, String> responseEntity = (Map<String, String>) response.getEntity();
        assertEquals("El campo 'nombres' es requerido", responseEntity.get("error"));

        verifyNoInteractions(personaUseCase, clienteUseCase);
    }

    @Test
    @DisplayName("Error al crear cliente - Contraseña faltante")
    void testCreateCliente_MissingPassword_BadRequest() {
        // Given
        clienteRequestDto.setContrasena("");

        // When
        Response response = clienteResource.createCliente(clienteRequestDto);

        // Then
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        
        Map<String, String> responseEntity = (Map<String, String>) response.getEntity();
        assertEquals("El campo 'contrasena' es requerido", responseEntity.get("error"));

        verifyNoInteractions(personaUseCase, clienteUseCase);
    }

    @Test
    @DisplayName("Error al crear cliente - IllegalArgumentException")
    void testCreateCliente_IllegalArgumentException_BadRequest() {
        // Given
        when(personaUseCase.createPersona(any(Persona.class))).thenReturn(1);
        when(clienteUseCase.createCliente(any(Cliente.class)))
                .thenThrow(new IllegalArgumentException("Ya existe un cliente para esta persona"));

        // When
        Response response = clienteResource.createCliente(clienteRequestDto);

        // Then
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        
        Map<String, String> responseEntity = (Map<String, String>) response.getEntity();
        assertEquals("Ya existe un cliente para esta persona", responseEntity.get("error"));
    }

    @Test
    @DisplayName("Error interno al crear cliente")
    void testCreateCliente_InternalError() {
        // Given
        when(personaUseCase.createPersona(any(Persona.class)))
                .thenThrow(new RuntimeException("Error de base de datos"));

        // When
        Response response = clienteResource.createCliente(clienteRequestDto);

        // Then
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        
        Map<String, String> responseEntity = (Map<String, String>) response.getEntity();
        assertEquals("Error interno del servidor", responseEntity.get("error"));
    }

    @Test
    @DisplayName("Obtener cliente por ID exitosamente")
    void testGetClienteById_Success() {
        // Given
        when(clienteUseCase.getClienteById(1)).thenReturn(cliente);

        // When
        Response response = clienteResource.getClienteById(1);

        // Then
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        
        ClienteResponseDto responseDto = (ClienteResponseDto) response.getEntity();
        assertEquals(1, responseDto.getClienteId());
        assertEquals("Jose Lema", responseDto.getNombres());
        assertEquals("Otavalo sn y principal", responseDto.getDireccion());
        assertEquals("098254785", responseDto.getTelefono());
        assertTrue(responseDto.getEstado());

        verify(clienteUseCase).getClienteById(1);
    }

    @Test
    @DisplayName("Cliente no encontrado por ID")
    void testGetClienteById_NotFound() {
        // Given
        when(clienteUseCase.getClienteById(999))
                .thenThrow(new RuntimeException("Cliente no encontrado"));

        // When
        Response response = clienteResource.getClienteById(999);

        // Then
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        
        Map<String, String> responseEntity = (Map<String, String>) response.getEntity();
        assertEquals("Cliente no encontrado", responseEntity.get("error"));
    }

    @Test
    @DisplayName("Cliente retorna null por ID")
    void testGetClienteById_Null_NotFound() {
        // Given
        when(clienteUseCase.getClienteById(1)).thenReturn(null);

        // When
        Response response = clienteResource.getClienteById(1);

        // Then
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        
        Map<String, String> responseEntity = (Map<String, String>) response.getEntity();
        assertEquals("Cliente no encontrado", responseEntity.get("error"));
    }

    @Test
    @DisplayName("Obtener todos los clientes exitosamente")
    void testGetAllClientes_Success() {
        // Given
        Cliente cliente2 = new Cliente(2, 2, "5678", true);
        Persona persona2 = new Persona(2, "Marianela Montalvo", "F", 28, "13 junio y Equinoccial", "098875187");
        cliente2.setPersona(persona2);
        
        List<Cliente> clientes = Arrays.asList(cliente, cliente2);
        when(clienteUseCase.getAllClientes()).thenReturn(clientes);

        // When
        Response response = clienteResource.getAllClientes();

        // Then
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        List<ClienteResponseDto> responseList = (List<ClienteResponseDto>) response.getEntity();
        assertEquals(2, responseList.size());
        assertEquals("Jose Lema", responseList.get(0).getNombres());
        assertEquals("Marianela Montalvo", responseList.get(1).getNombres());

        verify(clienteUseCase).getAllClientes();
    }

    @Test
    @DisplayName("Error interno al obtener todos los clientes")
    void testGetAllClientes_InternalError() {
        // Given
        when(clienteUseCase.getAllClientes())
                .thenThrow(new RuntimeException("Error de base de datos"));

        // When
        Response response = clienteResource.getAllClientes();

        // Then
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        
        Map<String, String> responseEntity = (Map<String, String>) response.getEntity();
        assertEquals("Error interno del servidor", responseEntity.get("error"));
    }

    @Test
    @DisplayName("Actualizar cliente exitosamente")
    void testUpdateCliente_Success() {
        // Given
        ClienteRequestDto updateRequest = new ClienteRequestDto();
        updateRequest.setNombres("Jose Lema Actualizado");
        updateRequest.setContrasena("nueva1234");
        updateRequest.setEstado(false);

        when(clienteUseCase.getClienteById(1)).thenReturn(cliente);
        when(personaUseCase.getPersonaById(1)).thenReturn(persona);

        // When
        Response response = clienteResource.updateCliente(1, updateRequest);

        // Then
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        
        Map<String, String> responseEntity = (Map<String, String>) response.getEntity();
        assertEquals("Cliente actualizado exitosamente", responseEntity.get("message"));

        verify(clienteUseCase).getClienteById(1);
        verify(personaUseCase).updatePersona(any(Persona.class));
        verify(clienteUseCase).updateCliente(any(Cliente.class));
    }

    @Test
    @DisplayName("Error al actualizar cliente - No encontrado")
    void testUpdateCliente_NotFound() {
        // Given
        ClienteRequestDto updateRequest = new ClienteRequestDto();
        updateRequest.setNombres("Test");
        
        when(clienteUseCase.getClienteById(999)).thenReturn(null);

        // When
        Response response = clienteResource.updateCliente(999, updateRequest);

        // Then
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        
        Map<String, String> responseEntity = (Map<String, String>) response.getEntity();
        assertEquals("Cliente no encontrado", responseEntity.get("error"));
    }

    @Test
    @DisplayName("Eliminar cliente exitosamente")
    void testDeleteCliente_Success() {
        // Given
        doNothing().when(clienteUseCase).deleteCliente(1);

        // When
        Response response = clienteResource.deleteCliente(1);

        // Then
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        
        Map<String, String> responseEntity = (Map<String, String>) response.getEntity();
        assertEquals("Cliente eliminado exitosamente", responseEntity.get("message"));

        verify(clienteUseCase).deleteCliente(1);
    }

    @Test
    @DisplayName("Error al eliminar cliente - IllegalArgumentException")
    void testDeleteCliente_IllegalArgumentException_BadRequest() {
        // Given
        doThrow(new IllegalArgumentException("Cliente no encontrado"))
                .when(clienteUseCase).deleteCliente(999);

        // When
        Response response = clienteResource.deleteCliente(999);

        // Then
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        
        Map<String, String> responseEntity = (Map<String, String>) response.getEntity();
        assertEquals("Cliente no encontrado", responseEntity.get("error"));
    }

    @Test
    @DisplayName("Error al eliminar cliente - RuntimeException")
    void testDeleteCliente_RuntimeException_NotFound() {
        // Given
        doThrow(new RuntimeException("Cliente no encontrado con ID: 999"))
                .when(clienteUseCase).deleteCliente(999);

        // When
        Response response = clienteResource.deleteCliente(999);

        // Then
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        
        Map<String, String> responseEntity = (Map<String, String>) response.getEntity();
        assertEquals("Cliente no encontrado con ID: 999", responseEntity.get("error"));
    }
}