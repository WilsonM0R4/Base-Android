package com.allegra.handyuvisa.models;

/**
 * Created by victor on 28/02/15.
 * com.allem.allemevent.models
 */
public class Server {
    public String wsdl;
    public String namespace;
    public String user;
    public String pass;
    /**
     *
     * @param wsdl
     * @param namespace
     * @param user
     * @param pass
     */
    public Server(String wsdl, String namespace, String user, String pass) {
        this.wsdl = wsdl;
        this.user = user;
        this.pass = pass;
        this.namespace=namespace;
    }
}
