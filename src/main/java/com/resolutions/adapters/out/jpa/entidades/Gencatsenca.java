package com.resolutions.adapters.out.jpa.entidades;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "gencatsenca", schema = "arq_hex")
public class Gencatsenca {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer codCat;
    private String catDesc;
    private String codUsr;
    private Instant fechCrea;
    private Instant fechMod;

    public Integer getCodCat() {
        return codCat;
    }

    public void setCodCat(Integer codCat) {
        this.codCat = codCat;
    }

    public String getCatDesc() {
        return catDesc;
    }

    public void setCatDesc(String catDesc) {
        this.catDesc = catDesc;
    }


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gencatsenca gce = (Gencatsenca) o;
        // Use the ID for equality comparison
        return codCat != null && Objects.equals(codCat, gce.codCat);
    }

    @Override
    public int hashCode() {
        // Use the ID for hashCode calculation
        return codCat != null ? Objects.hash(codCat) : 0; // Handle transient state
    }

}
