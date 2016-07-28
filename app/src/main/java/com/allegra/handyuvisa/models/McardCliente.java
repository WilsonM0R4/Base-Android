package com.allegra.handyuvisa.models;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * Created by sergiofarfan on 25/07/16.
 */
public class McardCliente implements KvmSerializable {

    public String idProducto;

    //************CONSTRUCTOR ***********
    public McardCliente(String idProducto){
        this.idProducto = idProducto;

    }

    //**************GLOBAL ATTRIBUTES********************
   // public int idCuenta;

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }


    public final static String ID_PRODUCTO="idProducto", PROPERTY="mCardCliente";

    //***************GETTERS Y SETTERS*************************
   /* public int getIdCuenta() {
        return idCuenta;
    }*/

  /*  public void setIdCuenta(int idCuenta) {
        this.idProducto = idCuenta;
    }*/

    //**************OVERRIDE METHODS*********************

    // devolver el valor de cada atributo de la clase a partir de su índice de orden
   @Override
    public Object getProperty(int index) {

        switch(index) {
            case 0:
                return idProducto;

        }
        return null;
    }
    //devolver simplemente el número de atributos de nuestra clase
    @Override
    public int getPropertyCount() {
        return 1;
    }

    //encargado de asignar el valor de cada atributo según su índice y el valor recibido como parámetro
    @Override
    public void setProperty(int index, Object value) {
        switch(index) {
            case 0:
                idProducto = (String) value;
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
        }
    }
}
