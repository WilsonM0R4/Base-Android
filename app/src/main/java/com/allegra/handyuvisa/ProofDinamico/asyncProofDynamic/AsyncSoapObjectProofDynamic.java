package com.allegra.handyuvisa.ProofDinamico.asyncProofDynamic;

import android.os.AsyncTask;
import android.util.Log;

import com.allegra.handyuvisa.async.AsyncSoapObject;
import com.allegra.handyuvisa.async.MyBus;
import com.allegra.handyuvisa.models.Server;
import com.allegra.handyuvisa.utils.Constants;

import org.apache.http.NameValuePair;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by sergiofarfan on 11/22/16.
 */

public class AsyncSoapObjectProofDynamic extends AsyncTask<String,Void,Vector<SoapObject>> {

    private static final String TAG = "AsyncSoapObjProofDynami";
    private String url,namespace,method,soapaction,faultstring;
    private ArrayList<NameValuePair> postValues;
    private int codeRequest, faultcode;
    private PropertyInfo property;

    //**************DEFAULT CONSTRUCTOR*******************
    private AsyncSoapObjectProofDynamic(String url, String namespace, String method, ArrayList<NameValuePair> postValues, int codeRequest){
        this.url = url;
        this.namespace = namespace;
        this.method = method;
        this.postValues = postValues;
        this.soapaction = namespace+method;
        this.codeRequest = codeRequest;
        Log.d(TAG, "URL "+url);
        Log.d(TAG, "namespace "+namespace);
        Log.d(TAG, "method "+method);

    }

    //***************OVERRIDE METHODS*********************
    @Override
    protected Vector<SoapObject> doInBackground(String... strings) {
        Log.d(TAG, "URL "+url);
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
            //envelope.implicitTypes = true;
            envelope.setOutputSoapObject(request);
            //envelope.headerOut = buildAuthHeader(envelope);

            transporte.debug=true;
            transporte.call(soapaction, envelope);
            Log.d(TAG,"soapAction "+soapaction);
            Log.d(TAG, transporte.requestDump);
            //(SoapObject) envelope.getResponse();
            result = (Vector<SoapObject>) envelope.getResponse();
            Log.d(TAG, transporte.responseDump);
            if (result!=null) Log.d(TAG, "Response "+result.toString());
            else Log.d(TAG, "Es nulo");
            faultcode =-1;
            faultstring="OK";
        } catch (Exception e) {
            Log.d(TAG, "Llega a la excepcion");
            e.printStackTrace();
            faultcode=-1000;
            faultstring=e.getMessage();
        }
        return result;
    }


    @Override
    protected void onPostExecute(Vector<SoapObject> result) {
//        Log.d(TAG, "result "+result.toString());
        MyBus.getInstance().post(new AsyncTaskSoapObjectResultEventProofDynamic(result,codeRequest,faultcode,faultstring));
    }


    //******************PROPER METHODS*********************

    //*********************GET INFO PROOF OF COVERAGE**********************************
    public static AsyncSoapObjectProofDynamic getInstance(String url, String namespace, String method, ArrayList<NameValuePair> postValues, int codeRequest){
        //Server server = new Server(url,namespace, Constants.SOAP_AUTH_USER,Constants.SOAP_AUTH_PASS);
        Log.d(TAG, "LLega al getInstance");
        return new AsyncSoapObjectProofDynamic(url,namespace,method,postValues,codeRequest);
    }
}
