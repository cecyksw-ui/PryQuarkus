package com.resolutions.model;

import java.math.BigDecimal;
import jakarta.persistence.*;

@Entity
@Table(name = "cuenta", schema = "arq_hex")
public class Cuenta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cuenta_id")
    private Integer cuentaId;
    
    @Column(name = "numero_cuenta", length = 20, unique = true, nullable = false)
    private String numeroCuenta;
    
    @Column(name = "tipo_cuenta", length = 20)
    private String tipoCuenta;
    
    @Column(name = "saldo_inicial", precision = 10, scale = 2)
    private BigDecimal saldoInicial = BigDecimal.ZERO;
    
    @Column(name = "estado", nullable = false)
    private Boolean estado = true;
    
    @Column(name = "cliente_id", nullable = false)
    private Integer clienteId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", referencedColumnName = "cliente_id", insertable = false, updatable = false)
    private Cliente cliente;

    public Cuenta() {
    }

    public Cuenta(Integer cuentaId, String numeroCuenta, String tipoCuenta, BigDecimal saldoInicial, Boolean estado, Integer clienteId) {
        this.cuentaId = cuentaId;
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        this.saldoInicial = saldoInicial;
        this.estado = estado;
        this.clienteId = clienteId;
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

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public String toString() {
        return "Cuenta{" +
                "cuentaId=" + cuentaId +
                ", numeroCuenta='" + numeroCuenta + '\'' +
                ", tipoCuenta='" + tipoCuenta + '\'' +
                ", saldoInicial=" + saldoInicial +
                ", estado=" + estado +
                ", clienteId=" + clienteId +
                ", cliente=" + cliente +
                '}';
    }
}