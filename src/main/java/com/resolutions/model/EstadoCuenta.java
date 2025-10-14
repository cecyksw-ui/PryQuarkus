package com.resolutions.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Modelo para el reporte de estado de cuenta
 */
public class EstadoCuenta {
    
    private Integer clienteId;
    private String nombreCliente;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private List<ResumenCuenta> cuentas;
    private BigDecimal totalCreditos;
    private BigDecimal totalDebitos;
    private BigDecimal saldoTotal;
    
    public EstadoCuenta() {
    }
    
    public EstadoCuenta(Integer clienteId, String nombreCliente, LocalDate fechaInicio, LocalDate fechaFin) {
        this.clienteId = clienteId;
        this.nombreCliente = nombreCliente;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.totalCreditos = BigDecimal.ZERO;
        this.totalDebitos = BigDecimal.ZERO;
        this.saldoTotal = BigDecimal.ZERO;
    }
    
    public Integer getClienteId() {
        return clienteId;
    }
    
    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }
    
    public String getNombreCliente() {
        return nombreCliente;
    }
    
    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }
    
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }
    
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    
    public LocalDate getFechaFin() {
        return fechaFin;
    }
    
    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }
    
    public List<ResumenCuenta> getCuentas() {
        return cuentas;
    }
    
    public void setCuentas(List<ResumenCuenta> cuentas) {
        this.cuentas = cuentas;
    }
    
    public BigDecimal getTotalCreditos() {
        return totalCreditos;
    }
    
    public void setTotalCreditos(BigDecimal totalCreditos) {
        this.totalCreditos = totalCreditos;
    }
    
    public BigDecimal getTotalDebitos() {
        return totalDebitos;
    }
    
    public void setTotalDebitos(BigDecimal totalDebitos) {
        this.totalDebitos = totalDebitos;
    }
    
    public BigDecimal getSaldoTotal() {
        return saldoTotal;
    }
    
    public void setSaldoTotal(BigDecimal saldoTotal) {
        this.saldoTotal = saldoTotal;
    }
    
    /**
     * Calcula los totales en base a las cuentas
     */
    public void calcularTotales() {
        if (cuentas != null) {
            this.totalCreditos = cuentas.stream()
                    .map(ResumenCuenta::getTotalCreditos)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            this.totalDebitos = cuentas.stream()
                    .map(ResumenCuenta::getTotalDebitos)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            this.saldoTotal = cuentas.stream()
                    .map(ResumenCuenta::getSaldoActual)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }
}