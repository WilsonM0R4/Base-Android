package com.allegra.handyuvisa.models;

import java.math.BigDecimal;

/**
 * Created by victor on 21/02/15.
 * com.allem.allemevent.models
 */
public class SolicitudCobro {
    public String moneda, nit, razonSocial,referencia, usuarioCuenta, contrasenaCuenta;
    public BigDecimal valor,iva, baseDevolucion;
    public int idComercio,idCuenta;

    public SolicitudCobro(String moneda, String nit, String razonSocial, String referencia, BigDecimal valor, BigDecimal iva, int idComercio, int idCuenta, BigDecimal baseDevolucion, String usuarioCuenta, String contrasenaCuenta) {
        this.moneda = moneda;
        this.nit = nit;
        this.razonSocial = razonSocial;
        this.referencia = referencia;
        this.valor = valor;
        this.iva = iva;
        this.idComercio = idComercio;
        this.idCuenta = idCuenta;
        this.baseDevolucion = baseDevolucion;
        this.usuarioCuenta = usuarioCuenta;
        this.contrasenaCuenta = contrasenaCuenta;
    }
}
