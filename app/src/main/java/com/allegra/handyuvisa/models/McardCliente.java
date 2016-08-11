package com.allegra.handyuvisa.models;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * Created by sergiofarfan on 25/07/16.
 */
public class McardCliente implements KvmSerializable {

    //**************GLOBAL ATTRIBUTES********************
    public String idProducto;
    public String numeroMembresia;
    public final static String ID_PRODUCTO="idProducto", PROPERTY="mCardCliente", NUM_MEMBRESIA = "numeroMembresia";

    //************CONSTRUCTOR ***********
    public McardCliente(String idProducto, String numeroMembresia){
        this.idProducto = idProducto;
        this.numeroMembresia = numeroMembresia;

    }


    public String getIdProducto() {
        return idProducto;
    }
    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getNumeroMembresia(){return numeroMembresia;}
    public void setNumeroMembresia(String numeroMembresia){this.numeroMembresia = numeroMembresia;  }


    //**************OVERRIDE METHODS*********************

    // devolver el valor de cada atributo de la clase a partir de su índice de orden
   @Override
    public Object getProperty(int index) {

        switch(index) {
            case 0:
                return idProducto;
            case 1:
                return numeroMembresia;

        }
        return null;
    }
    //devolver simplemente el número de atributos de nuestra clase
    @Override
    public int getPropertyCount() {
        return 2;
    }

    //encargado de asignar el valor de cada atributo según su índice y el valor recibido como parámetro
    @Override
    public void setProperty(int index, Object value) {
        switch(index) {
            case 0:
                idProducto = (String) value;
                break;
            case 1:
                numeroMembresia= (String) value;
                break;
        }
    }
    // informar, según el índice recibido como parámetro, el tipo y nombre del atributo correspondiente.
    // El tipo de cada atributo se devolverá como un valor de la clase PropertyInfo
    @Override
    public void getPropertyInfo(int index, Hashtable properties, PropertyInfo info) {

        switch(index) {
            case 0:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = ID_PRODUCTO;
                break;
            case 1:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = NUM_MEMBRESIA;
                break;
        }
    }

    @Override
    public String getInnerText() {
        return null;
    }

    @Override
    public void setInnerText(String s) {

    }
}
