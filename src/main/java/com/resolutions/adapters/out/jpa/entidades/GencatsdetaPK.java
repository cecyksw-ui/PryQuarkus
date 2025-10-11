package com.resolutions.adapters.out.jpa.entidades;

import jakarta.persistence.Embeddable;

@Embeddable
public class GencatsdetaPK {
    private Integer codCat;
    private Integer codCor;

    public Integer getCodCat() {
        return codCat;
    }

    public void setCodCat(Integer codCat) {
        this.codCat = codCat;
    }

    public Integer getCodCor() {
        return codCor;
    }

    public void setCodCor(Integer codCor) {
        this.codCor = codCor;
    }
}
