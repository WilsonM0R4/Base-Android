package com.allegra.handyuvisa.async;

import android.os.AsyncTask;

import com.allegra.handyuvisa.models.Server;
import com.allegra.handyuvisa.utils.Constants;

import org.apache.http.NameValuePair;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;

import java.util.ArrayList;

/**
 * Created by victor on 27/02/15.
 * com.allem.allemevent.async
 */
public class AsyncSoapPrimitive extends AsyncTask<String,Void,SoapPrimitive> {

    private static final String TAG = "AsyncSoap" ;
    private String url,namespace,method,soapaction,faultstring;
    private ArrayList<NameValuePair> postValues;
    private PropertyInfo property;
    private int codeRequest, faultcode;
    private String userserver,passserver;

    private AsyncSoapPrimitive(Server server,String url, String method, ArrayList<NameValuePair> postValues, PropertyInfo property,int codeRequest){
        this.url = server.wsdl;
        this.namespace=server.namespace;
        this.method = method;
        this.postValues = postValues;
        this.soapaction =namespace+method;
        this.codeRequest = codeRequest;
        this.property= property;
        this.userserver=server.user;
        this.passserver=server.pass;
    }

    public static AsyncSoapPrimitive getInstance(String url,String namespace,String method,ArrayList<NameValuePair> postValues, int codeRequest){
        Server server = new Server(url,namespace, Constants.SOAP_AUTH_USER,Constants.SOAP_AUTH_PASS);
        return new AsyncSoapPrimitive(server,url,method,postValues,null,codeRequest);
    }

    public static AsyncSoapPrimitive getInstance(String url,String namespace,String method,PropertyInfo property, int codeRequest){
        Server server = new Server(url,namespace,Constants.SOAP_AUTH_USER,Constants.SOAP_AUTH_PASS);
        return new AsyncSoapPrimitive(server,url,method,null,property,codeRequest);
    }

    public static AsyncSoapPrimitive getInstance(String url,String namespace,String method,ArrayList<NameValuePair> postValues,PropertyInfo property, int codeRequest){
        Server server = new Server(url,namespace,Constants.SOAP_AUTH_USER,Constants.SOAP_AUTH_PASS);
        return new AsyncSoapPrimitive(server,url,method,null,property,codeRequest);
    }

    @Override
    protected SoapPrimitive doInBackground(String... strings) {
      //  Log.d(TAG, "url: " + url);
      //  Log.d(TAG, "namespace: " + namespace);
      //  Log.d(TAG, "soapaction:" + soapaction);
        SoapPrimitive result = null;
        SoapObject request = new SoapObject(namespace, method);

        try {
            if(postValues!=null){
                for (int i=0;i<postValues.size();i++){
                   // Log.d(TAG, postValues.get(i).getName() + ":" + postValues.get(i).getValue());
                    request.addProperty(postValues.get(i).getName(),postValues.get(i).getValue());
                }
            }
            if(property!=null){
                request.addProperty(property);
            }

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            //envelope.dotNet=true;
            envelope.setOutputSoapObject(request);

            envelope.headerOut = buildAuthHeader(envelope);

            HttpTransportSE transporte = new HttpTransportSE(url);
            transporte.debug=true;
            SslConnection.allowSSLCertificate();
            transporte.call(soapaction, envelope);
           // Log.d(TAG, transporte.requestDump);
            result = (SoapPrimitive) envelope.getResponse();
           // Log.d(TAG, transporte.responseDump);
            faultcode=-1;
            faultstring="OK";
        } catch (Exception e) {
            String error = e.toString();

            //faultcode=Integer.valueOf(error);
            faultcode=-1000;
            faultstring=e.getMessage();
        }

        return result;
    }

    @Override
    protected void onPostExecute(SoapPrimitive result) {
        MyBus.getInstance().post(new AsyncTaskSoapPrimitiveResultEvent((SoapPrimitive)result,codeRequest,faultcode,faultstring));
    }

    private Element[] buildAuthHeader(SoapSerializationEnvelope envelope) {

        Element headers[] = new Element[1];
        headers[0]= new Element().createElement("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Security");
        headers[0].setAttribute(envelope.env, "mustUnderstand", "1");
        Element security=headers[0];

        Element to = new Element().createElement(security.getNamespace(), "UsernameToken");
        to.setAttribute("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "Id", "UsernameToken-2");

        Element action1 = new Element().createElement(security.getNamespace(), "Username");
        action1.addChild(Node.TEXT, userserver);
        to.addChild(Node.ELEMENT,action1);

        Element action2 = new Element().createElement(security.getNamespace(), "Password");
        action2.setAttribute(null, "Type", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");
        action2.addChild(Node.TEXT, passserver);
        to.addChild(Node.ELEMENT,action2);
        headers[0].addChild(Node.ELEMENT, to);
        return headers;
    }
}
