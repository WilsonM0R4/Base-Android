package com.allem.alleminmotion.visacheckout.models;

import java.util.ArrayList;

/**
 * Created by marianocarrizo on 4/3/15.
 */
public class AllemCommerceUser {
/*
<RespuestaComercioCuentaRespuesta>
            <idComercio>7</idComercio>
            <idUsuario>4</idUsuario>
            <idSesion>ab489822564a67ff62cf6acbe7709240:5cb1716121547b2f8e3b64c00dbc</idSesion>
            <codigoComercio>000000</codigoComercio>
            <nit>9000000-1</nit>
            <razonSocial>Iatai Andina SAS.</razonSocial>
            <nombresUario>Demo APP</nombresUario>
            <apellidoUsuario>IATAI Prueba</apellidoUsuario>
            <emailUsuario>tech@iatai.com</emailUsuario>
            <respuestaCuentas>
               <cuenta>
                  <idCuenta>7</idCuenta>
                  <nombre>Demo</nombre>
               </cuenta>
            </respuestaCuentas>
         </RespuestaComercioCuentaRespuesta>
 */
    public String idComercio;
    public String nombre;
    public String apellido;
    public String email;

    public String idSesion;
    public int idUsuario;

    public String nit;
    public String razonSocial;
    public String codigoComercio;


    public ArrayList<AllemCommerceAccount> cuentas;

    public AllemCommerceUser(String idComercio, String nombre, String apellido, String email,
                             String idSesion, int idUsuario,
                             String nit, String razonSocial,
                             String codigoComercio) {
        this.idComercio = idComercio;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.idSesion = idSesion;
        this.idUsuario = idUsuario;
        this.nit = nit;
        this.razonSocial = razonSocial;
        this.codigoComercio = codigoComercio;
    }
}
