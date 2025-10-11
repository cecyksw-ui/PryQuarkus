package com.resolutions.adapters.in.rest;

import com.resolutions.application.ports.in.EstadoCuentaUseCase;
import com.resolutions.model.EstadoCuenta;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Path("/reportes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReporteResource {

    @Inject
    EstadoCuentaUseCase estadoCuentaUseCase;

    @GET
    @Path("/estado_cuenta")
    public Response generarEstadoCuenta(
            @QueryParam("cliente_id") Integer clienteId,
            @QueryParam("fecha_inicio") String fechaInicioStr,
            @QueryParam("fecha_fin") String fechaFinStr) {
        
        try {
            // Validar parámetros requeridos
            if (clienteId == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"El parámetro cliente_id es requerido\"}")
                        .build();
            }
            
            if (fechaInicioStr == null || fechaInicioStr.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"El parámetro fecha_inicio es requerido (formato: YYYY-MM-DD)\"}")
                        .build();
            }
            
            if (fechaFinStr == null || fechaFinStr.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"El parámetro fecha_fin es requerido (formato: YYYY-MM-DD)\"}")
                        .build();
            }

            // Parsear fechas
            LocalDate fechaInicio;
            LocalDate fechaFin;
            
            try {
                fechaInicio = LocalDate.parse(fechaInicioStr, DateTimeFormatter.ISO_LOCAL_DATE);
                fechaFin = LocalDate.parse(fechaFinStr, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException e) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Formato de fecha inválido. Use YYYY-MM-DD\"}")
                        .build();
            }

            // Generar estado de cuenta
            EstadoCuenta estadoCuenta = estadoCuentaUseCase.generarEstadoCuenta(clienteId, fechaInicio, fechaFin);
            
            return Response.ok(estadoCuenta).build();
            
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error interno del servidor: " + e.getMessage() + "\"}")
                    .build();
        }
    }
}