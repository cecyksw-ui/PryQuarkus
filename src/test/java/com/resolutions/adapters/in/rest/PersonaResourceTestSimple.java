package com.resolutions.adapters.in.rest;

import com.resolutions.application.ports.in.PersonaUseCase;
import com.resolutions.model.Persona;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.jboss.logging.Logger;

import jakarta.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonaResourceTestSimple {

    @Mock
    private PersonaUseCase personaUseCase;

    @Mock
    private Logger logger;

    private PersonaResource personaResource;

    @BeforeEach
    void setUp() {
        personaResource = new PersonaResource();
        personaResource.personaUseCase = personaUseCase;
        personaResource.logger = logger;
    }

    @Test
    void testCreatePersona_Success() {
        // Given
        Persona persona = new Persona("PER001", "Juan Perez", "M", 30, "Calle 123", "555-1234");
        when(personaUseCase.createPersona(any(Persona.class))).thenReturn("PER001");

        // When
        Response response = personaResource.createPersona(persona);

        // Then
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        verify(personaUseCase).createPersona(persona);
    }

    @Test
    void testCreatePersona_Error() {
        // Given
        Persona persona = new Persona("", "Juan Perez", "M", 30, "Calle 123", "555-1234");
        when(personaUseCase.createPersona(any(Persona.class)))
                .thenThrow(new IllegalArgumentException("El ID de persona es requerido"));

        // When
        Response response = personaResource.createPersona(persona);

        // Then
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        verify(personaUseCase).createPersona(persona);
    }

    @Test
    void testGetPersonaById_Success() {
        // Given
        Persona persona = new Persona("PER001", "Juan Perez", "M", 30, "Calle 123", "555-1234");
        when(personaUseCase.getPersonaById("PER001")).thenReturn(persona);

        // When
        Response response = personaResource.getPersonaById("PER001");

        // Then
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
        verify(personaUseCase).getPersonaById("PER001");
    }

    @Test
    void testGetPersonaById_NotFound() {
        // Given
        when(personaUseCase.getPersonaById("PER999"))
                .thenThrow(new RuntimeException("Persona no encontrada"));

        // When
        Response response = personaResource.getPersonaById("PER999");

        // Then
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        verify(personaUseCase).getPersonaById("PER999");
    }

    @Test
    void testGetAllPersonas_Success() {
        // Given
        List<Persona> personas = Arrays.asList(
                new Persona("PER001", "Juan Perez", "M", 30, "Calle 123", "555-1234"),
                new Persona("PER002", "Maria Garcia", "F", 25, "Calle 456", "555-5678")
        );
        when(personaUseCase.getAllPersonas()).thenReturn(personas);

        // When
        Response response = personaResource.getAllPersonas();

        // Then
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
        verify(personaUseCase).getAllPersonas();
    }
}