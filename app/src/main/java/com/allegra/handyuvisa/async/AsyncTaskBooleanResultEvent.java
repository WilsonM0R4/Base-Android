package com.allegra.handyuvisa.async;

/**
 * Created by victor on 22/02/15.
 * com.allem.allemevent.async
 */
public class AsyncTaskBooleanResultEvent {
    private Boolean result;
    private int codeRequest;
    public AsyncTaskBooleanResultEvent(Boolean result,int codeRequest) {
        this.result = result;
        this.codeRequest=codeRequest;
    }

    public Boolean getResult() {
        return result;
    }

    public int getCodeRequest(){
        return codeRequest;
    }
}
