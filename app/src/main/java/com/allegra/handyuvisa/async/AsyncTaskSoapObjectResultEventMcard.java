package com.allegra.handyuvisa.async;

import org.ksoap2.serialization.SoapObject;

import java.util.Vector;

/**
 * Created by sergio on 1/08/16.
 */
public class AsyncTaskSoapObjectResultEventMcard {

    private int codeRequest, faultcode;
    private String faultstring;
    private Vector<SoapObject> soapObjectVector;

    public  AsyncTaskSoapObjectResultEventMcard (Vector<SoapObject> soapObjectVector, int codeRequest, int faultcode, String faultstring) {
        this.soapObjectVector = soapObjectVector;
        this.codeRequest = codeRequest;
        this.faultcode = faultcode;
        this.faultstring = faultstring;
    }


    public Vector<SoapObject> getResult() {
       /* SoapObject response = (SoapObject)envelope.getResponse();
        String resp = response.toString();*/

        return soapObjectVector;
    }

    public int getCodeRequest(){
        return codeRequest;
    }

    public int getFaultCode(){
        return faultcode;
    }

    public String getFaultString(){

        return faultstring;
    }
}
