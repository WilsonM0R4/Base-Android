package com.allegra.handyuvisa.models;

/**
 * Created by victor on 28/02/15.
 * com.allem.allemevent.models
 */
public class AllemUser {
    public String saludo;
    public String nombre;
    public String apellido;
    public String email;
    public String hashpassword;
    public String idSesion;
    public int idCuenta;
    public boolean estado;
    public String channel;
    public String celular;
    public String idNumber;
    public String idType;
    public String pais, celular_codigo;
    public String empresa;
    public String idEmpresa;

    /**
     *
     * @param saludo
     * @param nombre
     * @param apellido
     * @param email
     * @param hashpassword
     * @param idSesion
     * @param idCuenta
     * @param estado
     * @param empresa
     * @param idEmpresa
     */

    public AllemUser(String saludo, String nombre, String apellido, String email, String hashpassword, String idSesion, int idCuenta,
                     boolean estado, String celular, String idNumber, String idType, String pais, String celular_codigo, String empresa, String idEmpresa){
        this.saludo = saludo;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.hashpassword = hashpassword;
        this.idSesion = idSesion;
        this.idCuenta = idCuenta;
        this.estado = estado;
        this.celular= celular;
        this.idNumber= idNumber;
        this.idType= idType;
        this.pais = pais;
        this.celular_codigo = celular_codigo;
        this.empresa = empresa;
        this.idEmpresa = idEmpresa;
    }

}
