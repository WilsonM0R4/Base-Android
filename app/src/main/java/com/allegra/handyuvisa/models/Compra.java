package com.allegra.handyuvisa.models;

/**
 * Created by victor on 21/02/15.
 * com.allem.allemevent.models
 */
public class Compra {
    public int idCompra;
    public String fecha,referencia,comercio,valor,moneda,urlVoucherCompra,urlDetalleCompra,idTransaccion;

    public Compra(int idCompra, String fecha, String referencia, String comercio, String valor, String moneda, String urlVoucherCompra, String urlDetalleCompra, String idTransaccion){
        this.idCompra=idCompra;
        this.fecha=fecha;
        this.referencia=referencia;
        this.comercio=comercio;
        this.valor=valor;
        this.moneda=moneda;
        this.urlVoucherCompra=urlVoucherCompra;
        this.urlDetalleCompra=urlDetalleCompra;
        this.idTransaccion = idTransaccion;
    }

}
