package com.resolutions.application.ports.in;

import com.resolutions.model.EstadoCuenta;
import java.time.LocalDate;

public interface EstadoCuentaUseCase {
    EstadoCuenta generarEstadoCuenta(Integer clienteId, LocalDate fechaInicio, LocalDate fechaFin);
}