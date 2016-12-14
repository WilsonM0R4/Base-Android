package com.allegra.handyuvisa.async;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.allegra.handyuvisa.models.Server;
import com.allegra.handyuvisa.utils.Constants;

import org.apache.http.NameValuePair;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by victor on 27/02/15.
 * com.allem.allemevent.async
 */
public class AsyncSoapObjectTest extends AsyncTask<String,Void,Vector<SoapObject>> {

    private static final String TAG = "AsyncSoap" ;
    private String url,namespace,method,soapaction,faultstring;
    private ArrayList<NameValuePair> postValues;
    private int codeRequest, faultcode;
    private PropertyInfo property;
    private String userserver,passserver;
    private SoapObject soapObject;
    private Context mContext;

    public AsyncSoapObjectTest(Context context){
        mContext = context;
    }

    private AsyncSoapObjectTest(Server server, String method, ArrayList<NameValuePair> postValues, int codeRequest){
        this.url = server.wsdl;
        this.namespace=server.namespace;
        this.method = method;
        this.soapaction =namespace+method;
        this.codeRequest = codeRequest;
        this.postValues = postValues;
        this.userserver = server.user;
        this.passserver = server.pass;
    }

    @Override
    protected Vector<SoapObject> doInBackground(String... strings) {

        Log.d(TAG, "url: " + url);
        Log.d(TAG, "namespace: " + namespace);
        Log.d(TAG, "soapaction:" + soapaction);
        HttpTransportSE transporte = new HttpTransportSE(url);

        SoapObject request = new SoapObject(namespace, method);
        Vector<SoapObject> result = null;

        try {
            if (postValues!=null){
                for (int i=0;i<postValues.size();i++){
                    Log.d(TAG, postValues.get(i).getName() + ":" + postValues.get(i).getValue());
                    request.addProperty(postValues.get(i).getName(),postValues.get(i).getValue());
                }
            }

            if (property!=null){
                request.addProperty(property);
            }

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.headerOut = buildAuthHeader(envelope);

            transporte.debug=true;
            SslConnection.allowSSLCertificate();
            transporte.call(soapaction, envelope);
            Log.d(TAG, transporte.requestDump);
            Object obj = envelope.getResponse();

            if (obj==null) {//0 mcards
                result = new Vector<SoapObject>();
                Log.e("Lista", "0 mcards");
            }

            if(obj instanceof Vector){//2 Mcards or more
                result = (Vector<SoapObject>) envelope.getResponse();
                int size = result.size();
                Log.e("Tamaño","Es "+String.valueOf(size));
            }else if(obj instanceof SoapObject){//One Mcard: Privilege
                result = new Vector<SoapObject>();
                result.add((SoapObject) obj);
                int size = result.size();
                Log.e("Tamaño","Es "+String.valueOf(size));
            }


            Log.d(TAG, transporte.responseDump);
            faultcode =-1;
            faultstring="OK";
        } catch (Exception e) {

            //faultcode=Integer.valueOf(error);
            faultcode=-1000;
            faultstring=e.getMessage();
            //Log.e("faultstring", faultstring);
        }

        return result;
    }

    @Override
    protected void onPostExecute(Vector<SoapObject> result) {
        if (result != null) {
            //One mcard: Privilege



            //2 or more Mcards
            //Log.e("Sergini", result.toString());
            MyBus.getInstance().post(new AsyncTaskSoapObjectResultEventMcard(result, codeRequest, faultcode,faultstring));

        } /*else{//0 Mcards
                //Save in SharedPreferences
                SharedPreferences prefs = mContext.getSharedPreferences("MisPreferencias", mContext.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("idMcard", "0");
                editor.apply();
            Log.e("Sergini null", "Null");
        }*/
    }

    public static AsyncSoapObjectTest getInstance2(String url,String namespace,String method,ArrayList<NameValuePair> postValues,int codeRequest){
        //Log.e("Sergio","Entra a getInstance2");
        Server server = new Server(url,namespace, Constants.SOAP_AUTH_EMAIL_MCARD,Constants.SOAP_AUTH_PASSWORD_MCARD);
        return new AsyncSoapObjectTest (server,method,postValues,codeRequest);
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
        //Log.e("Faul","Entra al header");
        return headers;
    }
}
