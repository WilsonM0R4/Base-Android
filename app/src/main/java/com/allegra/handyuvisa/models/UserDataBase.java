package com.allegra.handyuvisa.models;

/**
 * Created by jsandoval on 22/08/16.
 */
public class UserDataBase  {

    private String nombre, apellido, tipoid, id, nummcard, value1, value2, value3;

    public UserDataBase(){

    }

    public UserDataBase(String nombre, String apellido, String tipoid, String id, String nummcard, String value1, String value2, String value3){

        super();
        this.nombre = nombre;
        this.apellido = apellido;
        this.tipoid = tipoid;
        this.id = id;
        this.nummcard = nummcard;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;

    }

    public String toString() {
        return "user [nombredb=" + nombre + ", apellidodb=" + apellido + ", tipoiddb=" + tipoid + ", iddb=" + id + ", nummcarddb=" + nummcard + ", value1=" + value1 + ", value2=" + value2 + ", value3=" + value3 + "]";
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

    public String getTipoid() {
        return tipoid;
    }

    public void setTipoid(String tipoid) {
        this.tipoid = tipoid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNummcard() {
        return nummcard;
    }

    public void setNummcard(String nummcard) {
        this.nummcard = nummcard;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getValue3() {
        return value3;
    }

    public void setValue3(String value3) {
        this.value3 = value3;
    }
}
