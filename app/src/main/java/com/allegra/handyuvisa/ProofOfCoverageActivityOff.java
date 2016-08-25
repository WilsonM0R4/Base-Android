package com.allegra.handyuvisa;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.allegra.handyuvisa.async.MyBus;
import com.allegra.handyuvisa.utils.CustomizedTextView;

public class ProofOfCoverageActivityOff extends FrontBackAnimate  implements FrontBackAnimate.InflateReadyListener {

    //*************GLOBAL ATTRIBUTES***********
    TextView  txtCoverage1, txtCoverage2,txtCoverage3;
    CustomizedTextView textNameLastName, txtTypeOfId, txtNumberOfId, txtNumberOfMcard;
    String nombre, apellido, tipoid, numid, numcard, value1, value2, value3;

    //********************OVERRIDE METHODS*******************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_proof_of_coverage);
        super.setView(R.layout.activity_proof_of_coverage, this);
        Intent intent = getIntent();
        nombre = intent.getExtras().getString("nombre");
        apellido = intent.getExtras().getString("apellido");
        tipoid = intent.getExtras().getString("tipoid");
        numid = intent.getExtras().getString("numid");
        numcard = intent.getExtras().getString("numcard");
        value1 = intent.getExtras().getString("value1");
        value2 = intent.getExtras().getString("value2");
        value3 = intent.getExtras().getString("value3");
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
        txtNumberOfMcard = (CustomizedTextView)root.findViewById(R.id.txtNumberOfMcard);

        textNameLastName.setText(nombre +" "+apellido);
        txtTypeOfId.setText(tipoid);
        txtNumberOfId.setText(numid);
        txtNumberOfMcard.setText(numcard);
        txtCoverage1.setText(value1);
        txtCoverage2.setText(value2);
        txtCoverage3.setText(value3);

    }

    //********************PROPER METHODS*******************


    public void onUpProof(View view) {
        onBackPressed();
    }


    public void onMenu(View view) {
        animate();
    }

}
