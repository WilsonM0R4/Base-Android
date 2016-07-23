package com.allem.alleminmotion.visacheckout.models;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * Created by victor on 01/03/15.
 * com.allem.allemevent.models
 */
public class CuentaClienteInfoAdicional implements KvmSerializable {

    public final static String PROPERTY="cuentaClienteInformacionAdicional";
    private final static String EMPRESA="empresa",CARGO="cargo",CELULAR="celular",
    CIUDAD="ciudad",CLASE="clase", CODIGO_PAIS = "codigoPais";
    private String empresa,cargo,celular,ciudad,clase, codigoPais;


    @Override
    public Object getProperty(int i) {
        switch(i){
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
          /*  case 5:
                return codigoPais;*/
        }
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 6;
    }

    @Override
    public void setProperty(int i, Object o) {
        switch(i){
            case 0:
                empresa = (String) o;
                break;
            case 1:
                cargo = (String) o;
                break;
            case 2:
                celular= (String) o;
                break;
            case 3:
                ciudad= (String) o;
                break;
            case 4:
                clase= (String) o;
                break;
            /*case 5:
                codigoPais= (String) o;
                break;*/
        }
    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {

        switch(i){
            case 0:
                propertyInfo.type= PropertyInfo.STRING_CLASS;
                propertyInfo.name=EMPRESA;
                break;
            case 1:
                propertyInfo.type= PropertyInfo.STRING_CLASS;
                propertyInfo.name=CARGO;
                break;
            case 2:
                propertyInfo.type= PropertyInfo.STRING_CLASS;
                propertyInfo.name=CELULAR;
                break;
            case 3:
                propertyInfo.type= PropertyInfo.STRING_CLASS;
                propertyInfo.name=CIUDAD;
                break;
            case 4:
                propertyInfo.type= PropertyInfo.STRING_CLASS;
                propertyInfo.name=CLASE;
                break;
            /*case 5:
                propertyInfo.type= PropertyInfo.STRING_CLASS;
                propertyInfo.name=CODIGO_PAIS;
                break;*/
        }
    }


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

    public void setCodigoPais(String codigoPais){
        this.codigoPais = codigoPais;
    }
}
