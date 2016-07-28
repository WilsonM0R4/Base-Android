package com.allegra.handyuvisa.models;

/**
 * Created by victor on 21/02/15.
 * com.allem.allemevent.models
 */
public class Notifications {
    public String message,url;
    public long timeinmillis;

    public Notifications(String message, String url, long timeinmillis) {
        this.message = message;
        this.timeinmillis = timeinmillis;
        this.url=url;
    }
}
