package com.allem.alleminmotion.visacheckout.models;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * Created by jsandoval on 26/07/16.
 */
public class CuentaClienteRecover implements KvmSerializable {

    public final static String PROPERTY="cuentaClienteRecover";
    private final static String EMAIL ="email";
    private String email;


    @Override
    public Object getProperty(int index) {
        switch(index) {
            case 0:
            return email;
        }
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 0;
    }

    @Override
    public void setProperty(int index, Object value) {
        switch (index) {
            case 4:
                email = (String) value;
                break;
        }
    }

    @Override
    public void getPropertyInfo(int index, Hashtable properties, PropertyInfo info) {

        switch (index) {
            case 4:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = EMAIL;
                break;
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
