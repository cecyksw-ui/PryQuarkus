package com.resolutions.adapters.in.rest;

import com.resolutions.application.ports.in.MovimientoUseCase;
import com.resolutions.model.Movimiento;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Path("/api/movimientos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovimientoResource {

    @Inject
    MovimientoUseCase movimientoUseCase;

    @Inject
    Logger logger;

    @POST
    public Response createMovimiento(Movimiento movimiento) {
        try {
            Integer movimientoId = movimientoUseCase.createMovimiento(movimiento);
            logger.infof("Movimiento creado con ID: %d", movimientoId);
            return Response.status(Response.Status.CREATED)
                    .entity(Map.of("movimientoId", movimientoId, "message", "Movimiento creado exitosamente"))
                    .build();
        } catch (IllegalArgumentException e) {
            logger.errorf("Error al crear movimiento: %s", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        } catch (Exception e) {
            logger.errorf("Error interno al crear movimiento: %s", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Error interno del servidor"))
                    .build();
        }
    }

    @POST
    @Path("/transaccion")
    public Response createTransaccion(Map<String, Object> transaccionData) {
        try {
            String tipoMovimiento = (String) transaccionData.get("tipoMovimiento");
            Number montoNumber = (Number) transaccionData.get("monto");
            Number cuentaIdNumber = (Number) transaccionData.get("cuentaId");
            
            if (tipoMovimiento == null || montoNumber == null || cuentaIdNumber == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("error", "Los campos tipoMovimiento, monto y cuentaId son requeridos"))
                        .build();
            }
            
            java.math.BigDecimal monto = new java.math.BigDecimal(montoNumber.toString());
            Integer cuentaId = cuentaIdNumber.intValue();
            
            Integer movimientoId = movimientoUseCase.createMovimientoConValidacion(tipoMovimiento, monto, cuentaId);
            logger.infof("Transacción creada con ID: %d", movimientoId);
            
            return Response.status(Response.Status.CREATED)
                    .entity(Map.of("movimientoId", movimientoId, "message", "Transacción procesada exitosamente"))
                    .build();
                    
        } catch (IllegalStateException e) {
            // Error de validación de saldo
            logger.errorf("Error de saldo: %s", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        } catch (IllegalArgumentException e) {
            logger.errorf("Error de validación: %s", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        } catch (Exception e) {
            logger.errorf("Error interno al procesar transacción: %s", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Error interno del servidor"))
                    .build();
        }
    }

    @GET
    @Path("/{movimientoId}")
    public Response getMovimientoById(@PathParam("movimientoId") Integer movimientoId) {
        try {
            Movimiento movimiento = movimientoUseCase.getMovimientoById(movimientoId);
            return Response.ok(movimiento).build();
        } catch (RuntimeException e) {
            logger.errorf("Movimiento no encontrado: %s", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @GET
    public Response getMovimientos(@QueryParam("cuentaId") Integer cuentaId,
                                  @QueryParam("fechaInicio") String fechaInicio,
                                  @QueryParam("fechaFin") String fechaFin) {
        try {
            List<Movimiento> movimientos;
            
            if (cuentaId != null) {
                movimientos = movimientoUseCase.getMovimientosByCuentaId(cuentaId);
            } else if (fechaInicio != null && fechaFin != null) {
                LocalDate inicio = LocalDate.parse(fechaInicio);
                LocalDate fin = LocalDate.parse(fechaFin);
                movimientos = movimientoUseCase.getMovimientosByFechaRange(inicio, fin);
            } else {
                movimientos = movimientoUseCase.getAllMovimientos();
            }
            
            return Response.ok(movimientos).build();
        } catch (Exception e) {
            logger.errorf("Error al obtener movimientos: %s", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Error interno del servidor"))
                    .build();
        }
    }

    @PUT
    @Path("/{movimientoId}")
    public Response updateMovimiento(@PathParam("movimientoId") Integer movimientoId, Movimiento movimiento) {
        try {
            movimientoUseCase.updateMovimiento(movimientoId, movimiento);
            logger.infof("Movimiento actualizado con ID: %d", movimientoId);
            return Response.ok(Map.of("message", "Movimiento actualizado exitosamente")).build();
        } catch (RuntimeException e) {
            logger.errorf("Error al actualizar movimiento: %s", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        } catch (Exception e) {
            logger.errorf("Error interno al actualizar movimiento: %s", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Error interno del servidor"))
                    .build();
        }
    }

    @DELETE
    @Path("/{movimientoId}")
    public Response deleteMovimiento(@PathParam("movimientoId") Integer movimientoId) {
        try {
            movimientoUseCase.deleteMovimiento(movimientoId);
            logger.infof("Movimiento eliminado con ID: %d", movimientoId);
            return Response.ok(Map.of("message", "Movimiento eliminado exitosamente")).build();
        } catch (RuntimeException e) {
            logger.errorf("Error al eliminar movimiento: %s", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        } catch (Exception e) {
            logger.errorf("Error interno al eliminar movimiento: %s", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Error interno del servidor"))
                    .build();
        }
    }
}