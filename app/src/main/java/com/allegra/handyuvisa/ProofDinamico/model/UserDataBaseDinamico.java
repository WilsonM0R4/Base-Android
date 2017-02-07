package com.allegra.handyuvisa.ProofDinamico.model;

/**
 * Created by jsandoval on 28/11/16.
 */

public class UserDataBaseDinamico {

    private String nombre, apellido, tipoid, id, nummcard, descripcion, valortexto;
    public Cobertura cobertura = new Cobertura(descripcion, valortexto);

    public UserDataBaseDinamico(){

    }

    public UserDataBaseDinamico(String nombre, String apellido, String tipoid, String id, String nummcard, Cobertura cobertura){


        super();
        this.nombre = nombre;
        this.apellido = apellido;
        this.tipoid = tipoid;
        this.id = id;
        this.nummcard = nummcard;
        this.cobertura = cobertura;
    }

    public String toString() {
        return "user [nombredb=" + nombre + ", apellidodb=" + apellido + ", tipoiddb=" + tipoid + ", iddb=" + id + ", nummcarddb=" + nummcard + ", cobertura=" + cobertura + "]";
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

    public Cobertura getCobertura(){
        return cobertura;
    }

    public void setCobertura(Cobertura cobertura) {
        this.cobertura = cobertura;
    }

}
