package com.allegra.handyuvisa.ProofDinamico.model;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * Created by sergiofarfan on 11/22/16.
 */

public class Cobertura implements KvmSerializable {

    private String nombre, valorTexto;
    public static final String NOMBRE = "nombre", VALOR_TEXTO = "valorTexto";

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
