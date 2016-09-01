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
    private final static String EMPRESA = "empresa", CARGO = "cargo", CELULAR = "celular",
            CIUDAD = "ciudad", CLASE = "clase", CODIGO_PAIS = "pais", CELULAR_CODIGO="celular_codigo";
    private String empresa;
    private String cargo;
    private String celular;
    private String ciudad;
    private String clase;
    private String pais;
    private String celular_codigo;
    /*private String tipoDocumento;
    private String numeroDocumento;*/


  /*  public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }*/


    @Override
    public Object getProperty(int i) {
        switch (i) {
            case 0:
                return empresa;
            case 1:
                return cargo;
            case 2:
                return celular;
            case 3:
                return ciudad;
            case 4:
                return clase;
            case 5:
                return pais;
            case 6:
                return celular_codigo;
          /*  case 7:
                return tipoDocumento;
            case 8:
                return numeroDocumento;*/
        }
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 7;
    }

    @Override
    public void setProperty(int i, Object o) {
        switch (i) {
            case 0:
                empresa = (String) o;
                break;
            case 1:
                cargo = (String) o;
                break;
            case 2:
                celular = (String) o;
                break;
            case 3:
                ciudad = (String) o;
                break;
            case 4:
                clase = (String) o;
                break;
            case 5:
                pais = (String) o;
                break;
            case 6:
                celular_codigo = (String) o;
                break;
            /*case 7:
                tipoDocumento = (String) o;
                break;
            case 8:
                numeroDocumento = (String) o;
                break;*/
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
                propertyInfo.name = CARGO;
                break;
            case 2:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = CELULAR;
                break;
            case 3:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = CIUDAD;
                break;
            case 4:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = CLASE;
                break;
            case 5:
                propertyInfo.type= PropertyInfo.STRING_CLASS;
                propertyInfo.name=CODIGO_PAIS;
                break;
            case 6:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name=CELULAR_CODIGO;
                break;
           /* case 7:
                propertyInfo.type= PropertyInfo.STRING_CLASS;
                propertyInfo.name=TIPO_DOCUMENTO;
                break;
            case 8:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name=NUMERO_DOCUMENTO;
                break;*/
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

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
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
