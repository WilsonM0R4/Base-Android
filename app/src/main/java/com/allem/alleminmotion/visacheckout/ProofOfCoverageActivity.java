package com.allem.alleminmotion.visacheckout;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.allem.alleminmotion.visacheckout.utils.CustomizedTextView;

public class ProofOfCoverageActivity extends FrontBackAnimate  implements FrontBackAnimate.InflateReadyListener {

    //*************GLOBAL ATTRIBUTES***********
    TextView  txtCoverage1, txtCoverage2,txtCoverage3;
    CustomizedTextView textNameLastName;
    String valueOfMcard, nombre, apellido;
    int idMcard = 208;

    //getResources().getString(R.string.UnlimitedMcard),
    //Get idMcard from intent extras


    //********************OVERRIDE METHODS*******************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_proof_of_coverage);
        super.setView(R.layout.activity_proof_of_coverage, this);
        //**************Get SharedPreferences**************
        SharedPreferences prefs =
                getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

        nombre = prefs.getString("nombre", "Santiago");
        apellido = prefs.getString("apellido", "Castro");

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
            case 0://Don´t have
                break;

        }

    }


    void setValues(){

        txtCoverage1.setText(valueOfMcard);
        txtCoverage2.setText(valueOfMcard);
        txtCoverage3.setText(valueOfMcard);
        textNameLastName.setText(nombre + " "+ apellido);
    }


    public void onMenu(View view) {
        animate();
    }

}
