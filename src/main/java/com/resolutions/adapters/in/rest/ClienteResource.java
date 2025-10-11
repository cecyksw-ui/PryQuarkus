package com.resolutions.adapters.in.rest;

import com.resolutions.application.ports.in.ClienteUseCase;
import com.resolutions.model.Cliente;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Map;

@Path("/api/clientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClienteResource {

    @Inject
    ClienteUseCase clienteUseCase;

    @Inject
    Logger logger;

    @POST
    public Response createCliente(Cliente cliente) {
        try {
            Integer clienteId = clienteUseCase.createCliente(cliente);
            logger.infof("Cliente creado con ID: %d", clienteId);
            return Response.status(Response.Status.CREATED)
                    .entity(Map.of("clienteId", clienteId, "message", "Cliente creado exitosamente"))
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
    public Response getClienteById(@PathParam("clienteId") Integer clienteId) {
        try {
            Cliente cliente = clienteUseCase.getClienteById(clienteId);
            return Response.ok(cliente).build();
        } catch (RuntimeException e) {
            logger.errorf("Cliente no encontrado: %s", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @GET
    public Response getAllClientes(@QueryParam("estado") Boolean estado) {
        try {
            List<Cliente> clientes;
            if (estado != null) {
                clientes = clienteUseCase.getClientesByEstado(estado);
            } else {
                clientes = clienteUseCase.getAllClientes();
            }
            return Response.ok(clientes).build();
        } catch (Exception e) {
            logger.errorf("Error al obtener clientes: %s", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Error interno del servidor"))
                    .build();
        }
    }

    @PUT
    @Path("/{clienteId}")
    public Response updateCliente(@PathParam("clienteId") Integer clienteId, Cliente cliente) {
        try {
            clienteUseCase.updateCliente(clienteId, cliente);
            logger.infof("Cliente actualizado con ID: %d", clienteId);
            return Response.ok(Map.of("message", "Cliente actualizado exitosamente")).build();
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
    public Response deleteCliente(@PathParam("clienteId") Integer clienteId) {
        try {
            clienteUseCase.deleteCliente(clienteId);
            logger.infof("Cliente eliminado con ID: %d", clienteId);
            return Response.ok(Map.of("message", "Cliente eliminado exitosamente")).build();
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
}