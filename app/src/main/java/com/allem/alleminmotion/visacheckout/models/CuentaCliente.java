package com.allem.alleminmotion.visacheckout.models;



import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * Created by victor on 01/03/15.
 * com.allem.allemevent.models
 */
public class CuentaCliente implements KvmSerializable {
    public final static String PROPERTY="cuentaCliente";
    private final static String SEGMENTO="segmento",SALUDO="saludo",NOMBRE="nombre",
    APELLIDO="apellido",EMAIL="email",PASSWORD="password",USERAGENT="userAgent",CCIA="cuentaClienteInformacionAdicional";
    private String segmento,saludo,nombre,apellido,email,password,userAgent;
    private CuentaClienteInfoAdicional cuentaClienteInformacionAdicional;


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
                return email;
            case 5:
                return password;
            case 6:
                return userAgent;
            case 7:
                return cuentaClienteInformacionAdicional;
        }
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 8;
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
                email= (String) o;
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
                propertyInfo.name=EMAIL;
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

        }
    }


    public String getSegmento() {
        return segmento;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
