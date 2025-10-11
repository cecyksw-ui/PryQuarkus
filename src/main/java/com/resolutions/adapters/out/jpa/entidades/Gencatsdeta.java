package com.resolutions.adapters.out.jpa.entidades;

import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;

public class Gencatsdeta {
    @EmbeddedId
    private GencatsdetaPK gencatsdetaPK;
    private String corDesc;
    @Embedded
    private CamposAudit camposAudit;


    public GencatsdetaPK getGencatsdetaPK() {
        return gencatsdetaPK;
    }

    public void setGencatsdetaPK(GencatsdetaPK gencatsdetaPK) {
        this.gencatsdetaPK = gencatsdetaPK;
    }

    public String getCorDesc() {
        return corDesc;
    }

    public void setCorDesc(String corDesc) {
        this.corDesc = corDesc;
    }

    public CamposAudit getCamposAudit() {
        return camposAudit;
    }

    public void setCamposAudit(CamposAudit camposAudit) {
        this.camposAudit = camposAudit;
    }
}
