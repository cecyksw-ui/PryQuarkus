package com.resolutions.application.useCases;

import com.resolutions.application.ports.in.EstadoCuentaUseCase;
import com.resolutions.application.ports.out.ClienteRepositoryPort;
import com.resolutions.application.ports.out.CuentaRepositoryPort;
import com.resolutions.application.ports.out.MovimientoRepositoryPort;
import com.resolutions.application.ports.out.PersonaRepositoryPort;
import com.resolutions.model.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class EstadoCuentaUseCaseImpl implements EstadoCuentaUseCase {

    @Inject
    ClienteRepositoryPort clienteRepository;

    @Inject
    CuentaRepositoryPort cuentaRepository;

    @Inject
    MovimientoRepositoryPort movimientoRepository;
    
    @Inject
    PersonaRepositoryPort personaRepository;

    @Override
    public EstadoCuenta generarEstadoCuenta(Integer clienteId, LocalDate fechaInicio, LocalDate fechaFin) {
        // Validaciones
        if (clienteId == null) {
            throw new IllegalArgumentException("El ID del cliente es requerido");
        }
        if (fechaInicio == null) {
            throw new IllegalArgumentException("La fecha de inicio es requerida");
        }
        if (fechaFin == null) {
            throw new IllegalArgumentException("La fecha de fin es requerida");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }

        // Obtener cliente
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + clienteId));

        // Crear estado de cuenta
        String nombreCliente = cliente.getPersona() != null ? cliente.getPersona().getNombre() : "Cliente " + clienteId;
        EstadoCuenta estadoCuenta = new EstadoCuenta(
                clienteId, 
                nombreCliente, 
                fechaInicio, 
                fechaFin
        );

        // Obtener cuentas del cliente
        List<Cuenta> cuentasCliente = cuentaRepository.findByClienteId(clienteId);
        
        if (cuentasCliente.isEmpty()) {
            estadoCuenta.setCuentas(new ArrayList<>());
            estadoCuenta.calcularTotales();
            return estadoCuenta;
        }

        // Generar resumen por cada cuenta
        List<ResumenCuenta> resumenesCuentas = cuentasCliente.stream()
                .map(cuenta -> generarResumenCuenta(cuenta, fechaInicio, fechaFin))
                .collect(Collectors.toList());

        estadoCuenta.setCuentas(resumenesCuentas);
        estadoCuenta.calcularTotales();

        return estadoCuenta;
    }
    
    @Override
    public List<ReporteEstadoCuenta> generarReporteEstadoCuenta(Integer clienteId, LocalDate fechaInicio, LocalDate fechaFin) {
        // Validaciones
        if (clienteId == null) {
            throw new IllegalArgumentException("El ID del cliente es requerido");
        }
        if (fechaInicio == null) {
            throw new IllegalArgumentException("La fecha de inicio es requerida");
        }
        if (fechaFin == null) {
            throw new IllegalArgumentException("La fecha de fin es requerida");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }

        // Obtener cliente y persona
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + clienteId));
        
        Persona persona = personaRepository.findById(cliente.getPersonaId())
                .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada para el cliente: " + clienteId));
        
        String nombreCliente = persona.getNombre();

        // Obtener cuentas del cliente
        List<Cuenta> cuentasCliente = cuentaRepository.findByClienteId(clienteId);
        
        List<ReporteEstadoCuenta> reportes = new ArrayList<>();
        
        for (Cuenta cuenta : cuentasCliente) {
            // Obtener movimientos de la cuenta en el rango de fechas
            List<Movimiento> movimientos = movimientoRepository.findByCuentaIdAndFechaRange(
                    cuenta.getCuentaId(), 
                    fechaInicio, 
                    fechaFin
            );

            // Crear reporte para cada movimiento
            for (Movimiento movimiento : movimientos) {
                ReporteEstadoCuenta reporte = new ReporteEstadoCuenta();
                reporte.setFecha(movimiento.getFecha());
                reporte.setCliente(nombreCliente);
                reporte.setNumeroCuenta(cuenta.getNumeroCuenta());
                reporte.setTipo(cuenta.getTipoCuenta());
                reporte.setSaldoInicial(cuenta.getSaldoInicial());
                reporte.setEstado(cuenta.getEstado());
                reporte.setMovimiento(movimiento.getValor());
                reporte.setSaldoDisponible(movimiento.getSaldo());
                
                reportes.add(reporte);
            }
        }
        
        // Ordenar por fecha
        reportes.sort((r1, r2) -> r1.getFecha().compareTo(r2.getFecha()));
        
        return reportes;
    }

    private ResumenCuenta generarResumenCuenta(Cuenta cuenta, LocalDate fechaInicio, LocalDate fechaFin) {
        ResumenCuenta resumen = new ResumenCuenta(
                cuenta.getCuentaId(),
                cuenta.getNumeroCuenta(),
                cuenta.getTipoCuenta(),
                cuenta.getSaldoInicial()
        );

        // Obtener movimientos de la cuenta en el rango de fechas
        List<Movimiento> movimientos = movimientoRepository.findByCuentaIdAndFechaRange(
                cuenta.getCuentaId(), 
                fechaInicio, 
                fechaFin
        );

        // Ordenar movimientos por fecha
        movimientos.sort((m1, m2) -> m1.getFecha().compareTo(m2.getFecha()));

        resumen.setMovimientos(movimientos);

        return resumen;
    }
}