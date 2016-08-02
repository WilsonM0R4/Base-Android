package com.allegra.handyuvisa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allegra.handyuvisa.async.AsyncSoapObject;
import com.allegra.handyuvisa.async.AsyncTaskSoapObjectResultEvent;
import com.allegra.handyuvisa.async.MyBus;
import com.allegra.handyuvisa.models.McardCliente;
import com.allegra.handyuvisa.parsers.SoapObjectParsers;
import com.allegra.handyuvisa.utils.Constants;
import com.allegra.handyuvisa.utils.CustomizedTextView;
import com.allegra.handyuvisa.utils.Util;
import com.squareup.otto.Subscribe;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class ProofOfCoverageActivity extends FrontBackAnimate  implements FrontBackAnimate.InflateReadyListener {

    //*************GLOBAL ATTRIBUTES***********
    LinearLayout header, centerListView, bottomTexts;
    TextView  txtCoverage1, txtCoverage2,txtCoverage3;
    CustomizedTextView textNameLastName, txtGetYourCertificate;
    String valueOfMcard, nombre, apellido;
    int idMcard = 0, idCuenta = 0;
    private ArrayList<NameValuePair> postValues = new ArrayList<>();

    //********************OVERRIDE METHODS*******************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_proof_of_coverage);
        super.setView(R.layout.activity_proof_of_coverage, this);
        MyBus.getInstance().register(this);
        //**************Get SharedPreferences**************
        SharedPreferences prefs =
                getSharedPreferences("MisPreferencias", MODE_PRIVATE);

        nombre = prefs.getString("nombre", "Santiago");
        apellido = prefs.getString("apellido", "Castro");
        String strIdCuenta = prefs.getString("idCuenta", "0");
        Log.d("Sergio",strIdCuenta);
        idCuenta = Integer.valueOf(strIdCuenta);
        //Si hay internet: consumir el servicio para mCards
        if (Util.hasInternetConnectivity(getApplicationContext())) {
            //Launch SOAP request for mCard
            postValues.add(new BasicNameValuePair("idCuenta", String.valueOf(idCuenta)));
            AsyncSoapObject.getInstance2(Constants.SOAP_URL_MCARD_PROD, Constants.MCARD_NAMESPACE,
                    Constants.MCARD_METHOD, postValues, Constants.MCARD_CODE).execute();
        }
        /*valueOfMcard = prefs.getString("idMcard", "0");
        //Log.e("valueOfMcard",valueOfMcard);
        idMcard = Integer.valueOf(valueOfMcard);*/

    }

    @Override
    protected void onDestroy() {
        MyBus.getInstance().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void initViews(View root) {

        txtCoverage1 = (TextView) root.findViewById(R.id.id_value_coverage1);
        txtCoverage2 = (TextView) root.findViewById(R.id.id_value_coverage2);
        txtCoverage3 = (TextView) root.findViewById(R.id.id_value_coverage3);
        textNameLastName = (CustomizedTextView)root.findViewById(R.id.textNameLastNAme);
        header = (LinearLayout)root.findViewById(R.id.customCoverageHeader);
        centerListView = (LinearLayout)root.findViewById(R.id.customListView);
        bottomTexts = (LinearLayout)root.findViewById(R.id.customTextCoverage);
        //findValueOfMcard();
        //setValues();
    }


    ///*************SOAP RESULT***************

    @Subscribe
    public void onAsyncTaskResult(AsyncTaskSoapObjectResultEvent event) {
        if (event.getCodeRequest() == Constants.MCARD_CODE){
            if (event.getResult() != null) {
                //Get data
                McardCliente user = SoapObjectParsers.toMcardCliente(event.getResult());
                String strIdMcard = user.getIdProducto();
                //Save in SharedPreferences
                SharedPreferences prefs =
                        getSharedPreferences("MisPreferencias", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("idMcard", strIdMcard);
                editor.apply();
                idMcard = Integer.valueOf(strIdMcard);
                //******************
                findValueOfMcard();
                setValues();

            }
           /* else{
                //Save in SharedPreferences
                SharedPreferences prefs =
                        getSharedPreferences("MisPreferencias", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("idMcard","0");
                editor.apply();
            }*/
        }
    }

    //********************PROPER METHODS*******************

    void findValueOfMcard(){
        //********************* PRIVILEGE       PREMIUM         EXCLUSIVE            UNLIMITED
        String[] arrayMcards = {"USD $50,000",  "USD $100,000",  "USD $500,000", getResources().getString(R.string.UnlimitedMcard)};
        //********************      212             208             209             210

        switch(idMcard){
            case 212:
                valueOfMcard = arrayMcards[0];
                break;
            case 208:
                valueOfMcard = arrayMcards[1];
                break;
            case 209:
                valueOfMcard = arrayMcards[2];
                break;
            case 210:
                valueOfMcard = arrayMcards[3];
                break;
            case 0://Don´t have mCard
                setGetYourCertificateLayout();
                break;

        }

    }


    void setValues(){

        txtCoverage1.setText(valueOfMcard);
        txtCoverage2.setText(valueOfMcard);
        txtCoverage3.setText(valueOfMcard);
        textNameLastName.setText(nombre + " "+ apellido);
    }

    void setGetYourCertificateLayout(){
        //Change layout
        header.setVisibility(View.GONE);
        centerListView.setVisibility(View.GONE);
        bottomTexts.setVisibility(View.GONE);
        setContentView(R.layout.get_your_certificate);
        txtGetYourCertificate = (CustomizedTextView) findViewById(R.id.txtGetYourCertificate2);
        txtGetYourCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchMcardActivity();
            }
        });

    }

    void launchMcardActivity(){

        //Launch mCard Activity
        Intent i = new Intent(getApplicationContext(), Mcardhtml.class);
        startActivity(i);
    }

    public void onUpProof(View view) {
        onBackPressed();
    }


    public void onMenu(View view) {
        animate();
    }

}
