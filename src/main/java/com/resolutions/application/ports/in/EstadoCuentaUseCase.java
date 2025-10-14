package com.resolutions.application.ports.in;

import com.resolutions.model.EstadoCuenta;
import com.resolutions.model.ReporteEstadoCuenta;
import java.time.LocalDate;
import java.util.List;

public interface EstadoCuentaUseCase {
    EstadoCuenta generarEstadoCuenta(Integer clienteId, LocalDate fechaInicio, LocalDate fechaFin);
    List<ReporteEstadoCuenta> generarReporteEstadoCuenta(Integer clienteId, LocalDate fechaInicio, LocalDate fechaFin);
}