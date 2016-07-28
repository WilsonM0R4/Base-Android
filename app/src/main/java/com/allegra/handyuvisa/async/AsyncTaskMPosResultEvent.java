package com.allegra.handyuvisa.async;

import java.util.HashMap;

public class AsyncTaskMPosResultEvent {
    private HashMap<String, String> result;
    private String apiName;
    private int errorCode;

    public AsyncTaskMPosResultEvent(HashMap<String, String> result, String apiName) {
        this.result = result;
        this.apiName = apiName;
    }

    public HashMap<String, String> getResult() {
        return result;
    }

    public String getApiName() {
        return apiName;
    }

}


