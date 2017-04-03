package com.allegra.handyuvisa.async;

import java.util.HashMap;

/**
 * Created by sergiofarfan on 12/19/16.
 */

public class AsyncTaskMPosResultEventChat {

    private HashMap<String, String> result;
    private String apiName;
    private int errorCode;

    public AsyncTaskMPosResultEventChat(HashMap<String, String> result, String apiName) {
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
