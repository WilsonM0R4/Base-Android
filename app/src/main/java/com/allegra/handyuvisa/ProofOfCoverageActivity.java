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
import com.allegra.handyuvisa.async.AsyncSoapObjectTest;
import com.allegra.handyuvisa.async.AsyncTaskSoapObjectResultEvent;
import com.allegra.handyuvisa.async.AsyncTaskSoapObjectResultEventMcard;
import com.allegra.handyuvisa.async.MyBus;
import com.allegra.handyuvisa.models.AllemUser;
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
    CustomizedTextView textNameLastName, txtGetYourCertificate, txtTypeOfId, txtNumberOfId;
    String valueOfMcard, nombre, apellido, typeOfId, numberOfId;
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
        typeOfId = prefs.getString("typeOfId", "CC");
        numberOfId = prefs.getString("numberOfId", "7887787");
        String strIdCuenta = prefs.getString("idCuenta", "0");
        Log.e("typeOfId",typeOfId);
        Log.e("numberOfId",numberOfId);
        idCuenta = Integer.valueOf(strIdCuenta);
        //Si hay internet: consumir el servicio para mCards
        if (Util.hasInternetConnectivity(getApplicationContext())) {
            //Launch SOAP request for mCard
            postValues.add(new BasicNameValuePair("idCuenta", String.valueOf(idCuenta)));
            AsyncSoapObjectTest.getInstance2(Constants.SOAP_URL_MCARD_PROD, Constants.MCARD_NAMESPACE,
                    Constants.MCARD_METHOD, postValues, Constants.MCARD_CODE).execute();
        } else {
        valueOfMcard = prefs.getString("idMcard", "0");
        Log.e("valueOfMcard",valueOfMcard);
        idMcard = Integer.valueOf(valueOfMcard);
            findValueOfMcard();
            setValues();
        }

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
        txtTypeOfId = (CustomizedTextView)root.findViewById(R.id.txtTypeOfId);
        txtNumberOfId = (CustomizedTextView)root.findViewById(R.id.txtNumberOfId);
        header = (LinearLayout)root.findViewById(R.id.customCoverageHeader);
        centerListView = (LinearLayout)root.findViewById(R.id.customListView);
        bottomTexts = (LinearLayout)root.findViewById(R.id.customTextCoverage);
        /*findValueOfMcard();
        setValues();*/
    }


    ///*************SOAP RESULT***************

    @Subscribe
    public void onAsyncTaskResult(AsyncTaskSoapObjectResultEventMcard event) {
        if (event.getCodeRequest() == Constants.MCARD_CODE){
            if (event.getResult() != null) {
                //Get data
                McardCliente mcardCliente = SoapObjectParsers.toMcardCliente(event.getResult());
                String strIdMcard = mcardCliente.getIdProducto();
                //Save in SharedPreferences
                SharedPreferences prefs =
                        getSharedPreferences("MisPreferencias", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                if (strIdMcard.equals("")){
                    strIdMcard = "0";
                }
                editor.putString("idMcard", strIdMcard);
                editor.apply();
                Log.e("strIdMcard","Es "+strIdMcard);
                idMcard = Integer.valueOf(strIdMcard);
                valueOfMcard = strIdMcard;
                //******Update values for user
                AllemUser allemUser = Constants.getUser(getApplicationContext());
                //AllemUser allemUser = SoapObjectParsers.toAllemUser(event.getResult());
                //((VisaCheckoutApp) this.getApplication()).setIdSession(allemUser.idSesion);
                nombre = allemUser.nombre;
                apellido = allemUser.apellido;
                typeOfId = getTypeOfIdForDisplay(allemUser.idType);
                numberOfId = allemUser.idNumber;
                Log.e("nombre",nombre);
                Log.e("apellido",apellido);
                Log.e("typeOfId",typeOfId);
                Log.e("numberOfId",numberOfId);
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

    private String getTypeOfIdForDisplay(String idType){

        String strType = "";
        switch(idType) {

            case "1":
                return "CC";
            //break;
            case "2":
                return "CE";
            case "3":
                return "NIT";
            //break;
            case "4":
                return "TI";
            case "5":
                return "PS";
            //break;
            case "10":
                return "NUIP";
            case "9":
                return "OTRO";
            //break;

        }


        return strType;

    }

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
        txtTypeOfId.setText(typeOfId);
        txtNumberOfId.setText(numberOfId);
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
        finish();
    }

    public void onUpProof(View view) {
        onBackPressed();
    }


    public void onMenu(View view) {
        animate();
    }

}
