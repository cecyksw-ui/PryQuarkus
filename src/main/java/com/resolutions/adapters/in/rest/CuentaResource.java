package com.resolutions.adapters.in.rest;

import com.resolutions.application.ports.in.CuentaUseCase;
import com.resolutions.model.Cuenta;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Map;

@Path("/api/cuentas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CuentaResource {

    @Inject
    CuentaUseCase cuentaUseCase;

    @Inject
    Logger logger;

    @POST
    public Response createCuenta(Cuenta cuenta) {
        try {
            Integer cuentaId = cuentaUseCase.createCuenta(cuenta);
            logger.infof("Cuenta creada con ID: %d", cuentaId);
            return Response.status(Response.Status.CREATED)
                    .entity(Map.of("cuentaId", cuentaId, "message", "Cuenta creada exitosamente"))
                    .build();
        } catch (IllegalArgumentException e) {
            logger.errorf("Error al crear cuenta: %s", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        } catch (Exception e) {
            logger.errorf("Error interno al crear cuenta: %s", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Error interno del servidor"))
                    .build();
        }
    }

    @GET
    @Path("/{cuentaId}")
    public Response getCuentaById(@PathParam("cuentaId") Integer cuentaId) {
        try {
            Cuenta cuenta = cuentaUseCase.getCuentaById(cuentaId);
            return Response.ok(cuenta).build();
        } catch (RuntimeException e) {
            logger.errorf("Cuenta no encontrada: %s", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/numero/{numeroCuenta}")
    public Response getCuentaByNumero(@PathParam("numeroCuenta") String numeroCuenta) {
        try {
            Cuenta cuenta = cuentaUseCase.getCuentaByNumeroCuenta(numeroCuenta);
            return Response.ok(cuenta).build();
        } catch (RuntimeException e) {
            logger.errorf("Cuenta no encontrada: %s", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @GET
    public Response getAllCuentas(@QueryParam("clienteId") Integer clienteId) {
        try {
            List<Cuenta> cuentas;
            if (clienteId != null) {
                cuentas = cuentaUseCase.getCuentasByClienteId(clienteId);
            } else {
                cuentas = cuentaUseCase.getAllCuentas();
            }
            return Response.ok(cuentas).build();
        } catch (Exception e) {
            logger.errorf("Error al obtener cuentas: %s", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Error interno del servidor"))
                    .build();
        }
    }

    @PUT
    @Path("/{cuentaId}")
    public Response updateCuenta(@PathParam("cuentaId") Integer cuentaId, Cuenta cuenta) {
        try {
            cuentaUseCase.updateCuenta(cuentaId, cuenta);
            logger.infof("Cuenta actualizada con ID: %d", cuentaId);
            return Response.ok(Map.of("message", "Cuenta actualizada exitosamente")).build();
        } catch (RuntimeException e) {
            logger.errorf("Error al actualizar cuenta: %s", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        } catch (Exception e) {
            logger.errorf("Error interno al actualizar cuenta: %s", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Error interno del servidor"))
                    .build();
        }
    }

    @DELETE
    @Path("/{cuentaId}")
    public Response deleteCuenta(@PathParam("cuentaId") Integer cuentaId) {
        try {
            cuentaUseCase.deleteCuenta(cuentaId);
            logger.infof("Cuenta eliminada con ID: %d", cuentaId);
            return Response.ok(Map.of("message", "Cuenta eliminada exitosamente")).build();
        } catch (RuntimeException e) {
            logger.errorf("Error al eliminar cuenta: %s", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        } catch (Exception e) {
            logger.errorf("Error interno al eliminar cuenta: %s", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Error interno del servidor"))
                    .build();
        }
    }
}