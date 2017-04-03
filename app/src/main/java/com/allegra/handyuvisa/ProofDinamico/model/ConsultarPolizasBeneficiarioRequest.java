package com.allegra.handyuvisa.ProofDinamico.model;

/**
 * Created by sergiofarfan on 11/22/16.
 */

public class ConsultarPolizasBeneficiarioRequest {

    String idCuenta, idPortal;
    boolean mostrarAppCobertura, mostrarAppBeneficios, mostrarSoloPolizaPrincipal;



    public String getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(String idCuenta) {
        this.idCuenta = idCuenta;
    }

    public String getIdPortal() {
        return idPortal;
    }

    public void setIdPortal(String idPortal) {
        this.idPortal = idPortal;
    }

    public boolean isMostrarAppCobertura() {
        return mostrarAppCobertura;
    }

    public void setMostrarAppCobertura(boolean mostrarAppCobertura) {
        this.mostrarAppCobertura = mostrarAppCobertura;
    }

    public boolean isMostrarAppBeneficios() {
        return mostrarAppBeneficios;
    }

    public void setMostrarAppBeneficios(boolean mostrarAppBeneficios) {
        this.mostrarAppBeneficios = mostrarAppBeneficios;
    }

    public boolean isMostrarSoloPolizaPrincipal() {
        return mostrarSoloPolizaPrincipal;
    }

    public void setMostrarSoloPolizaPrincipal(boolean mostrarSoloPolizaPrincipal) {
        this.mostrarSoloPolizaPrincipal = mostrarSoloPolizaPrincipal;
    }
}
