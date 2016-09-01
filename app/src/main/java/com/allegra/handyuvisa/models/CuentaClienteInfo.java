package com.allegra.handyuvisa.models;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * Created by victor on 01/03/15.
 * com.allem.allemevent.models
 */
public class CuentaClienteInfo implements KvmSerializable {
    public final static String PROPERTY="cuentaClienteInfo";
    private final static String SEGMENTO="segmento",SALUDO="saludo",NOMBRE="nombre",
    APELLIDO="apellido", ID_SESSION ="idSesion",PASSWORD="password",CURRENT_PASSWORD="passwordActual",
            USERAGENT="userAgent",CCIA="cuentaClienteInformacionAdicional", TIPO_DOCUMENTO = "tipoDocumento",
            NUMERO_DOCUMENTO="numeroDocumento";
    private String segmento;
    private String saludo;
    private String nombre;
    private String apellido;
    private String idSesion;
    private String password;
    private String userAgent;
    private String currentPassword;
    private String numeroDocumento;
    private String tipoDocumento;
    private CuentaClienteInfoAdicional cuentaClienteInformacionAdicional;

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }




    @Override
    public Object getProperty(int i) {
        switch(i){
            case 0:
                return segmento;
            case 1:
                return saludo;
            case 2:
                return nombre;
            case 3:
                return apellido;
            case 4:
                return idSesion;
            case 5:
                return password;
            case 6:
                return userAgent;
            case 7:
                return cuentaClienteInformacionAdicional;
            case 8:
                return  currentPassword;
            case 9:
                return tipoDocumento;
            case 10:
                return  numeroDocumento;
        }
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 11;
    }

    @Override
    public void setProperty(int i, Object o) {
        switch(i){
            case 0:
                segmento = (String) o;
                break;
            case 1:
                saludo = (String) o;
                break;
            case 2:
                nombre= (String) o;
                break;
            case 3:
                apellido= (String) o;
                break;
            case 4:
                idSesion = (String) o;
                break;
            case 5:
                password= (String) o;
                break;
            case 6:
                userAgent= (String) o;
                break;
            case 7:
                cuentaClienteInformacionAdicional = (CuentaClienteInfoAdicional) o;
                break;
            case 8:
                currentPassword = (String) o;
                break;
            case 9:
                tipoDocumento = (String) o;
                break;
            case 10:
                numeroDocumento = (String) o;
                break;
        }
    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {
        switch(i){
            case 0:
                propertyInfo.type= PropertyInfo.STRING_CLASS;
                propertyInfo.name=SEGMENTO;
                break;
            case 1:
                propertyInfo.type= PropertyInfo.STRING_CLASS;
                propertyInfo.name=SALUDO;
                break;
            case 2:
                propertyInfo.type= PropertyInfo.STRING_CLASS;
                propertyInfo.name=NOMBRE;
                break;
            case 3:
                propertyInfo.type= PropertyInfo.STRING_CLASS;
                propertyInfo.name=APELLIDO;
                break;
            case 4:
                propertyInfo.type= PropertyInfo.STRING_CLASS;
                propertyInfo.name= ID_SESSION;
                break;
            case 5:
                propertyInfo.type= PropertyInfo.STRING_CLASS;
                propertyInfo.name=PASSWORD;
                break;
            case 6:
                propertyInfo.type= PropertyInfo.STRING_CLASS;
                propertyInfo.name=USERAGENT;
                break;
            case 7:
                propertyInfo.type=CuentaClienteInfoAdicional.class;
                propertyInfo.name=CCIA;
                break;

            case 8:
                propertyInfo.type= PropertyInfo.STRING_CLASS;
                propertyInfo.name=CURRENT_PASSWORD;
                break;
            case 9:
                propertyInfo.type=PropertyInfo.STRING_CLASS;
                propertyInfo.name=TIPO_DOCUMENTO;
                break;

            case 10:
                propertyInfo.type= PropertyInfo.STRING_CLASS;
                propertyInfo.name= NUMERO_DOCUMENTO;
                break;

        }
    }

    /*@Override
    public String getInnerText() {
        return null;
    }

    @Override
    public void setInnerText(String s) {

    }*/

    public String getSegmento() {
        return segmento;
    }
    public String getCurrentPassword() {
        return currentPassword;
    }
    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }


    public void setSegmento(String segmento) {
        this.segmento = segmento;
    }

    public String getSaludo() {
        return saludo;
    }

    public void setSaludo(String saludo) {
        this.saludo = saludo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getIdSesion() {
        return idSesion;
    }

    public void setIdSesion(String idSesion) {
        this.idSesion = idSesion;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public CuentaClienteInfoAdicional getCuentaClienteInformacionAdicional() {
        return cuentaClienteInformacionAdicional;
    }

    public void setCuentaClienteInformacionAdicional(CuentaClienteInfoAdicional cuentaClienteInformacionAdicional) {
        this.cuentaClienteInformacionAdicional = cuentaClienteInformacionAdicional;
    }
}
