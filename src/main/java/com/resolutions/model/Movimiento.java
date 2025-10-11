package com.resolutions.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "movimiento", schema = "arq_hex")
public class Movimiento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movimiento_id")
    private Integer movimientoId;
    
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha = LocalDate.now();
    
    @Column(name = "tipo_movimiento", length = 20, nullable = false)
    private String tipoMovimiento;
    
    @Column(name = "valor", precision = 10, scale = 2, nullable = false)
    private BigDecimal valor;
    
    @Column(name = "saldo", precision = 10, scale = 2, nullable = false)
    private BigDecimal saldo;
    
    @Column(name = "cuenta_id", nullable = false)
    private Integer cuentaId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_id", referencedColumnName = "cuenta_id", insertable = false, updatable = false)
    private Cuenta cuenta;

    public Movimiento() {
    }

    public Movimiento(Integer movimientoId, LocalDate fecha, String tipoMovimiento, BigDecimal valor, BigDecimal saldo, Integer cuentaId) {
        this.movimientoId = movimientoId;
        this.fecha = fecha;
        this.tipoMovimiento = tipoMovimiento;
        this.valor = valor;
        this.saldo = saldo;
        this.cuentaId = cuentaId;
    }

    public Integer getMovimientoId() {
        return movimientoId;
    }

    public void setMovimientoId(Integer movimientoId) {
        this.movimientoId = movimientoId;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Integer getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(Integer cuentaId) {
        this.cuentaId = cuentaId;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    @Override
    public String toString() {
        return "Movimiento{" +
                "movimientoId=" + movimientoId +
                ", fecha=" + fecha +
                ", tipoMovimiento='" + tipoMovimiento + '\'' +
                ", valor=" + valor +
                ", saldo=" + saldo +
                ", cuentaId=" + cuentaId +
                ", cuenta=" + cuenta +
                '}';
    }
}