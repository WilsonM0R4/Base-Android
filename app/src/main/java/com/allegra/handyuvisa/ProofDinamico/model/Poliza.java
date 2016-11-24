package com.allegra.handyuvisa.ProofDinamico.model;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by sergiofarfan on 11/22/16.
 */

public class Poliza implements KvmSerializable {

    private String numeroPoliza;
    private ArrayList<Cobertura> coberturas;
    public final static String NUMERO_POLIZA = "numeroPoliza", COBERTURAS = "coberturas";

    @Override
    public Object getProperty(int index) {
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 0;
    }

    @Override
    public void setProperty(int index, Object value) {

    }

    @Override
    public void getPropertyInfo(int index, Hashtable properties, PropertyInfo info) {

    }
}
