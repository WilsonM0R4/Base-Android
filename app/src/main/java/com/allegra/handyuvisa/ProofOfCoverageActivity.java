package com.allegra.handyuvisa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allegra.handyuvisa.utils.CustomizedTextView;

public class ProofOfCoverageActivity extends FrontBackAnimate  implements FrontBackAnimate.InflateReadyListener {

    //*************GLOBAL ATTRIBUTES***********
    LinearLayout header, centerListView, bottomTexts;
    TextView  txtCoverage1, txtCoverage2,txtCoverage3;
    CustomizedTextView textNameLastName, txtGetYourCertificate;
    String valueOfMcard, nombre, apellido;
    int idMcard = 0;

    //********************OVERRIDE METHODS*******************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_proof_of_coverage);
        super.setView(R.layout.activity_proof_of_coverage, this);
        //**************Get SharedPreferences**************
        SharedPreferences prefs =
                getSharedPreferences("MisPreferencias", MODE_PRIVATE);

        nombre = prefs.getString("nombre", "Santiago");
        apellido = prefs.getString("apellido", "Castro");
        valueOfMcard = prefs.getString("idMcard","0");
        //Log.e("valueOfMcard",valueOfMcard);
        idMcard = Integer.valueOf(valueOfMcard);

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
        findValueOfMcard();
        setValues();
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
