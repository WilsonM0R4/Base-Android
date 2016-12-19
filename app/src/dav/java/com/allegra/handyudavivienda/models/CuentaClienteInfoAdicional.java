package com.allegra.handyuvisa.models;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * Created by victor on 01/03/15.
 * com.allem.allemevent.models
 */
public class CuentaClienteInfoAdicional implements KvmSerializable {

    public final static String PROPERTY = "cuentaClienteInformacionAdicional";
    private final static String EMPRESA = "empresa", NUM_EMPRESA = "empresa_nit", CARGO = "cargo", CELULAR = "celular",
            CIUDAD = "ciudad", CLASE = "clase", CODIGO_PAIS = "pais", CELULAR_CODIGO="celular_codigo";
    private String empresa;
    private String numempresa;
    private String cargo;
    private String pais;
    private String celular_codigo;
    private String celular;
    private String ciudad;
    private String clase;

    @Override
    public Object getProperty(int i) {
        switch (i) {
            case 0:
                return empresa;
            case 1:
                return numempresa;
            case 2:
                return cargo;
            case 3:
                return pais;
            case 4:
                return celular_codigo;
            case 5:
                return celular;
            case 6:
                return ciudad;
            case 7:
                return clase;
        }
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 8;
    }

    @Override
    public void setProperty(int i, Object o) {
        switch (i) {
            case 0:
                empresa = (String) o;
                break;
            case 1:
                numempresa = (String) o;
                break;
            case 2:
                cargo = (String) o;
                break;
            case 3:
                pais = (String) o;
                break;
            case 4:
                celular_codigo = (String) o;
                break;
            case 5:
                celular = (String) o;
                break;
            case 6:
                ciudad = (String) o;
            case 7:
                clase = (String) o;
                break;
        }
    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {

        switch (i) {
            case 0:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = EMPRESA;
                break;
            case 1:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = NUM_EMPRESA;
                break;
            case 2:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = CARGO;
                break;
            case 3:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = CODIGO_PAIS;
                break;
            case 4:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = CELULAR_CODIGO;
                break;
            case 5:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = CELULAR;
                break;
            case 6:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = CIUDAD;
                break;
            case 7:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = CLASE;
                break;
        }
    }

   /* @Override
    public String getInnerText() {
        return null;
    }

    @Override
    public void setInnerText(String s) {

    }*/


    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getNumEmpresa() {
        return numempresa;
    }

    public void setNumEmpresa(String numempresa) {
        this.numempresa = numempresa;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public String getCodigoPais(){
        return pais;
    }

    public void setCodigoPais(String pais){this.pais = pais;}

    public String getCelularCodigo (){
        return celular_codigo;
    }

    public void setCelularCodigo (String celular_codigo) { this.celular_codigo = celular_codigo;}

}
