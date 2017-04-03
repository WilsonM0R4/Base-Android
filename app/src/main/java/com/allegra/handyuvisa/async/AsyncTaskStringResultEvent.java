package com.allegra.handyuvisa.async;

/**
 * Created by victor on 22/02/15.
 * com.allem.allemevent.async
 */
public class AsyncTaskStringResultEvent {
    private String result;
    private int codeRequest;

    public AsyncTaskStringResultEvent(String result,int codeRequest) {
        this.result = result;
        this.codeRequest = codeRequest;
    }

    public String getResult() {
        return result;
    }

    public int getCodeRequest() {
        return codeRequest;
    }
}


