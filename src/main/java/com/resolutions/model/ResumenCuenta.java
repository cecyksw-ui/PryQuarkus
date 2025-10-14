package com.resolutions.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Resumen de cuenta para el estado de cuenta
 */
public class ResumenCuenta {
    
    private Integer cuentaId;
    private String numeroCuenta;
    private String tipoCuenta;
    private BigDecimal saldoInicial;
    private BigDecimal saldoActual;
    private BigDecimal totalCreditos;
    private BigDecimal totalDebitos;
    private List<Movimiento> movimientos;
    
    public ResumenCuenta() {
    }
    
    public ResumenCuenta(Integer cuentaId, String numeroCuenta, String tipoCuenta, BigDecimal saldoInicial) {
        this.cuentaId = cuentaId;
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        this.saldoInicial = saldoInicial;
        this.saldoActual = saldoInicial;
        this.totalCreditos = BigDecimal.ZERO;
        this.totalDebitos = BigDecimal.ZERO;
    }
    
    public Integer getCuentaId() {
        return cuentaId;
    }
    
    public void setCuentaId(Integer cuentaId) {
        this.cuentaId = cuentaId;
    }
    
    public String getNumeroCuenta() {
        return numeroCuenta;
    }
    
    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }
    
    public String getTipoCuenta() {
        return tipoCuenta;
    }
    
    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }
    
    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }
    
    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }
    
    public BigDecimal getSaldoActual() {
        return saldoActual;
    }
    
    public void setSaldoActual(BigDecimal saldoActual) {
        this.saldoActual = saldoActual;
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
    
    public List<Movimiento> getMovimientos() {
        return movimientos;
    }
    
    public void setMovimientos(List<Movimiento> movimientos) {
        this.movimientos = movimientos;
        calcularTotales();
    }
    
    /**
     * Calcula totales en base a los movimientos
     */
    private void calcularTotales() {
        if (movimientos != null) {
            this.totalCreditos = movimientos.stream()
                    .filter(m -> m.esCredito())
                    .map(Movimiento::getValor)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            this.totalDebitos = movimientos.stream()
                    .filter(m -> m.esDebito())
                    .map(m -> m.getValor().abs()) // Convertir a positivo para mostrar
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            // El saldo actual se toma del último movimiento si existe
            if (!movimientos.isEmpty()) {
                this.saldoActual = movimientos.get(movimientos.size() - 1).getSaldo();
            }
        }
    }
}