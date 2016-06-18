package com.allem.alleminmotion.visacheckout.models;

/**
 * Created by victor on 03/03/15.
 * com.allem.allemevent.models
 */
public class Transaccion {
    public int idTransaccion,numeroAutorizacion,idEstadoTransaccion;
    public String referencia,estadoTransaccion,moneda,urlRedireccion;
    public float valor;

    /**
     *
     * @param idTransaccion
     * @param numeroAutorizacion
     * @param idEstadoTransaccion
     * @param referencia
     * @param estadoTransaccion
     * @param moneda
     * @param urlRedireccion
     * @param valor
     */
    public Transaccion(int idTransaccion, int numeroAutorizacion, int idEstadoTransaccion, String referencia, String estadoTransaccion, String moneda, String urlRedireccion, float valor) {
        this.idTransaccion = idTransaccion;
        this.numeroAutorizacion = numeroAutorizacion;
        this.idEstadoTransaccion = idEstadoTransaccion;
        this.referencia = referencia;
        this.estadoTransaccion = estadoTransaccion;
        this.moneda = moneda;
        this.urlRedireccion = urlRedireccion;
        this.valor = valor;
    }
}
