package com.resolutions.model;

import java.time.Instant;

public class Catalogo {
    private Integer codCat;
    private String catDesc;
    private String codUsr;
    private Instant fecha;

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

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }
}
