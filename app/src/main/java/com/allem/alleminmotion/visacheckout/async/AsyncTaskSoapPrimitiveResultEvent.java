package com.allem.alleminmotion.visacheckout.async;

import org.ksoap2.serialization.SoapPrimitive;

/**
 * Created by victor on 27/02/15.
 * com.allem.allemevent.async
 */
public class AsyncTaskSoapPrimitiveResultEvent {
    private SoapPrimitive result;
    private int codeRequest;
    private int faultcode;
    private String faultstring;

    public AsyncTaskSoapPrimitiveResultEvent(SoapPrimitive result,int codeRequest, int faultcode,String faultstring) {
        this.result = result;
        this.codeRequest = codeRequest;
        this.faultcode = faultcode;
        this.faultstring = faultstring;
    }

    public SoapPrimitive getResult() {return result;}

    public int getCodeRequest(){return codeRequest;}

    public int getFaultCode(){return faultcode;}

    public String getFaultString(){
        return faultstring;
    }

}
