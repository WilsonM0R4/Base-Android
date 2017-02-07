package com.allegra.handyuvisa.ProofDinamico.asyncProofDynamic;

import org.ksoap2.serialization.SoapObject;

import java.util.Vector;

/**
 * Created by sergiofarfan on 11/23/16.
 */

public class AsyncTaskSoapObjectResultEventProofDynamic {

    private Vector<SoapObject> result;
    private int codeRequest, faultcode;
    private String faultstring;

    public AsyncTaskSoapObjectResultEventProofDynamic(Vector<SoapObject> result, int codeRequest, int faultcode, String faultstring) {
        this.result = result;
        this.codeRequest = codeRequest;
        this.faultcode = faultcode;
        this.faultstring = faultstring;
    }


    public Vector<SoapObject> getResult() {
        return result;
    }

    public int getCodeRequest(){
        return codeRequest;
    }

    public String getFaultString(){
        return faultstring;
    }
}
