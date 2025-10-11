package com.resolutions.adapters.in.rest;

import com.resolutions.application.ports.in.PersonaUseCase;
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

@Path("/api/personas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Personas", description = "Operaciones CRUD para entidades Persona")
public class PersonaResource {

    @Inject
    PersonaUseCase personaUseCase;

    @Inject
    Logger logger;

    @POST
    @Operation(summary = "Crear nueva persona", description = "Crea una nueva persona en el sistema")
    public Response createPersona(Persona persona) {
        try {
            String personaId = personaUseCase.createPersona(persona);
            logger.infof("Persona creada con ID: %s", personaId);
            return Response.status(Response.Status.CREATED)
                    .entity(Map.of("personaId", personaId, "message", "Persona creada exitosamente"))
                    .build();
        } catch (IllegalArgumentException e) {
            logger.errorf("Error al crear persona: %s", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        } catch (Exception e) {
            logger.errorf("Error interno al crear persona: %s", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Error interno del servidor"))
                    .build();
        }
    }

    @GET
    @Path("/{personaId}")
    public Response getPersonaById(@PathParam("personaId") String personaId) {
        try {
            Persona persona = personaUseCase.getPersonaById(personaId);
            return Response.ok(persona).build();
        } catch (RuntimeException e) {
            logger.errorf("Persona no encontrada: %s", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @GET
    public Response getAllPersonas() {
        try {
            List<Persona> personas = personaUseCase.getAllPersonas();
            return Response.ok(personas).build();
        } catch (Exception e) {
            logger.errorf("Error al obtener personas: %s", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Error interno del servidor"))
                    .build();
        }
    }

    @PUT
    @Path("/{personaId}")
    public Response updatePersona(@PathParam("personaId") String personaId, Persona persona) {
        try {
            personaUseCase.updatePersona(personaId, persona);
            logger.infof("Persona actualizada con ID: %s", personaId);
            return Response.ok(Map.of("message", "Persona actualizada exitosamente")).build();
        } catch (RuntimeException e) {
            logger.errorf("Error al actualizar persona: %s", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        } catch (Exception e) {
            logger.errorf("Error interno al actualizar persona: %s", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Error interno del servidor"))
                    .build();
        }
    }

    @DELETE
    @Path("/{personaId}")
    public Response deletePersona(@PathParam("personaId") String personaId) {
        try {
            personaUseCase.deletePersona(personaId);
            logger.infof("Persona eliminada con ID: %s", personaId);
            return Response.ok(Map.of("message", "Persona eliminada exitosamente")).build();
        } catch (RuntimeException e) {
            logger.errorf("Error al eliminar persona: %s", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        } catch (Exception e) {
            logger.errorf("Error interno al eliminar persona: %s", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Error interno del servidor"))
                    .build();
        }
    }
}