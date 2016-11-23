package com.allegra.handyuvisa.ProofDinamico.asyncProofDynamic;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by sergiofarfan on 11/23/16.
 */

public class AsyncTaskSoapObjectResultEventProofDynamic {

    private SoapObject result;
    private int codeRequest, faultcode;
    private String faultstring;

    public AsyncTaskSoapObjectResultEventProofDynamic(SoapObject result, int codeRequest,int faultcode, String faultstring) {
        this.result = result;
        this.codeRequest = codeRequest;
        this.faultcode = faultcode;
        this.faultstring = faultstring;
    }


    public SoapObject getResult() {
        return result;
    }

    public int getCodeRequest(){
        return codeRequest;
    }

    public String getFaultString(){
        return faultstring;
    }
}
