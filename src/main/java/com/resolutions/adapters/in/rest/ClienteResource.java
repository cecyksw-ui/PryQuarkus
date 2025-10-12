package com.resolutions.adapters.in.rest;

import com.resolutions.adapters.in.rest.dto.ClienteRequestDto;
import com.resolutions.adapters.in.rest.dto.ClienteResponseDto;
import com.resolutions.application.ports.in.ClienteUseCase;
import com.resolutions.application.ports.in.PersonaUseCase;
import com.resolutions.model.Cliente;
import com.resolutions.model.Persona;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/api/clientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Clientes", description = "Operaciones CRUD para gestión de clientes (persona + cliente)")
public class ClienteResource {

    @Inject
    ClienteUseCase clienteUseCase;
    
    @Inject
    PersonaUseCase personaUseCase;

    @Inject
    Logger logger;

    @POST
    @Operation(summary = "Crear nuevo cliente", description = "Crea una nueva persona y cliente en el sistema")
    public Response createCliente(ClienteRequestDto clienteRequest) {
        try {
            // Validar datos requeridos
            if (clienteRequest.getNombres() == null || clienteRequest.getNombres().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("error", "El campo 'nombres' es requerido"))
                        .build();
            }
            
            if (clienteRequest.getContrasena() == null || clienteRequest.getContrasena().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("error", "El campo 'contrasena' es requerido"))
                        .build();
            }

            // Crear primero la persona
            Persona persona = new Persona();
            persona.setNombre(clienteRequest.getNombres());
            persona.setDireccion(clienteRequest.getDireccion());
            persona.setTelefono(clienteRequest.getTelefono());
            persona.setGenero(clienteRequest.getGenero());
            persona.setEdad(clienteRequest.getEdad());
            
            Integer personaId = personaUseCase.createPersona(persona);
            logger.infof("Persona creada con ID: %d", personaId);

            // Luego crear el cliente
            Cliente cliente = new Cliente();
            cliente.setPersonaId(personaId);
            cliente.setContrasena(clienteRequest.getContrasena());
            cliente.setEstado(clienteRequest.getEstado() != null ? clienteRequest.getEstado() : true);
            
            Integer clienteId = clienteUseCase.createCliente(cliente);
            logger.infof("Cliente creado con ID: %d", clienteId);

            return Response.status(Response.Status.CREATED)
                    .entity(Map.of(
                        "clienteId", clienteId,
                        "personaId", personaId,
                        "message", "Cliente creado exitosamente"
                    ))
                    .build();
                    
        } catch (IllegalArgumentException e) {
            logger.errorf("Error al crear cliente: %s", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        } catch (Exception e) {
            logger.errorf("Error interno al crear cliente: %s", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Error interno del servidor"))
                    .build();
        }
    }

    @GET
    @Path("/{clienteId}")
    @Operation(summary = "Obtener cliente por ID", description = "Retorna los datos completos del cliente incluyendo información de persona")
    public Response getClienteById(@PathParam("clienteId") Integer clienteId) {
        try {
            Cliente cliente = clienteUseCase.getClienteById(clienteId);
            if (cliente == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("error", "Cliente no encontrado"))
                        .build();
            }

            ClienteResponseDto response = convertToResponseDto(cliente);
            return Response.ok(response).build();

        } catch (RuntimeException e) {
            logger.errorf("Error al obtener cliente: %s", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        } catch (Exception e) {
            logger.errorf("Error interno al obtener cliente: %s", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Error interno del servidor"))
                    .build();
        }
    }

    @GET
    @Operation(summary = "Listar todos los clientes", description = "Retorna la lista completa de clientes")
    public Response getAllClientes() {
        try {
            List<Cliente> clientes = clienteUseCase.getAllClientes();
            List<ClienteResponseDto> response = clientes.stream()
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList());
            
            return Response.ok(response).build();

        } catch (Exception e) {
            logger.errorf("Error al obtener clientes: %s", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Error interno del servidor"))
                    .build();
        }
    }

    @PUT
    @Path("/{clienteId}")
    @Operation(summary = "Actualizar cliente", description = "Actualiza los datos del cliente y persona")
    public Response updateCliente(@PathParam("clienteId") Integer clienteId, ClienteRequestDto clienteRequest) {
        try {
            Cliente clienteExistente = clienteUseCase.getClienteById(clienteId);
            if (clienteExistente == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("error", "Cliente no encontrado"))
                        .build();
            }

            // Actualizar datos de persona si están presentes
            if (clienteRequest.getNombres() != null || clienteRequest.getDireccion() != null || 
                clienteRequest.getTelefono() != null) {
                
                Persona persona = personaUseCase.getPersonaById(clienteExistente.getPersonaId());
                if (persona != null) {
                    if (clienteRequest.getNombres() != null) {
                        persona.setNombre(clienteRequest.getNombres());
                    }
                    if (clienteRequest.getDireccion() != null) {
                        persona.setDireccion(clienteRequest.getDireccion());
                    }
                    if (clienteRequest.getTelefono() != null) {
                        persona.setTelefono(clienteRequest.getTelefono());
                    }
                    if (clienteRequest.getGenero() != null) {
                        persona.setGenero(clienteRequest.getGenero());
                    }
                    if (clienteRequest.getEdad() != null) {
                        persona.setEdad(clienteRequest.getEdad());
                    }
                    
                    personaUseCase.updatePersona(persona);
                }
            }

            // Actualizar datos de cliente
            if (clienteRequest.getContrasena() != null) {
                clienteExistente.setContrasena(clienteRequest.getContrasena());
            }
            if (clienteRequest.getEstado() != null) {
                clienteExistente.setEstado(clienteRequest.getEstado());
            }

            clienteUseCase.updateCliente(clienteExistente);

            return Response.ok(Map.of("message", "Cliente actualizado exitosamente")).build();

        } catch (IllegalArgumentException e) {
            logger.errorf("Error al actualizar cliente: %s", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        } catch (RuntimeException e) {
            logger.errorf("Error al actualizar cliente: %s", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        } catch (Exception e) {
            logger.errorf("Error interno al actualizar cliente: %s", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Error interno del servidor"))
                    .build();
        }
    }

    @DELETE
    @Path("/{clienteId}")
    @Operation(summary = "Eliminar cliente", description = "Elimina un cliente del sistema")
    public Response deleteCliente(@PathParam("clienteId") Integer clienteId) {
        try {
            clienteUseCase.deleteCliente(clienteId);
            return Response.ok(Map.of("message", "Cliente eliminado exitosamente")).build();

        } catch (IllegalArgumentException e) {
            logger.errorf("Error al eliminar cliente: %s", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        } catch (RuntimeException e) {
            logger.errorf("Error al eliminar cliente: %s", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        } catch (Exception e) {
            logger.errorf("Error interno al eliminar cliente: %s", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Error interno del servidor"))
                    .build();
        }
    }

    private ClienteResponseDto convertToResponseDto(Cliente cliente) {
        ClienteResponseDto dto = new ClienteResponseDto();
        dto.setClienteId(cliente.getClienteId());
        dto.setEstado(cliente.getEstado());
        
        // Si tiene persona asociada, obtener sus datos
        if (cliente.getPersona() != null) {
            dto.setNombres(cliente.getPersona().getNombre());
            dto.setDireccion(cliente.getPersona().getDireccion());
            dto.setTelefono(cliente.getPersona().getTelefono());
        } else if (cliente.getPersonaId() != null) {
            // Si no está cargada la relación, buscar la persona
            try {
                Persona persona = personaUseCase.getPersonaById(cliente.getPersonaId());
                if (persona != null) {
                    dto.setNombres(persona.getNombre());
                    dto.setDireccion(persona.getDireccion());
                    dto.setTelefono(persona.getTelefono());
                }
            } catch (Exception e) {
                logger.warnf("No se pudo cargar la persona para el cliente %d: %s", cliente.getClienteId(), e.getMessage());
            }
        }
        
        return dto;
    }
}