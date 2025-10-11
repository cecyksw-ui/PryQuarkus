package com.resolutions.adapters.out.jpa.entidades;

import jakarta.persistence.Embeddable;

import java.time.Instant;

@Embeddable
public class CamposAudit {
    private String codUsr;
    private Instant fechCrea;
    private Instant fechMod;

    public String getCodUsr() {
        return codUsr;
    }

    public void setCodUsr(String codUsr) {
        this.codUsr = codUsr;
    }

    public Instant getFechCrea() {
        return fechCrea;
    }

    public void setFechCrea(Instant fechCrea) {
        this.fechCrea = fechCrea;
    }

    public Instant getFechMod() {
        return fechMod;
    }

    public void setFechMod(Instant fechMod) {
        this.fechMod = fechMod;
    }
}
