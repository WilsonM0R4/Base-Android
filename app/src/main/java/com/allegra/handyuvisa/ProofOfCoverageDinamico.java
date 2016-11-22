package com.allegra.handyuvisa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.allegra.handyuvisa.async.MyBus;
import com.allegra.handyuvisa.utils.CustomizedTextView;

/**
 * Created by jsandoval on 22/11/16.
 */

public class ProofOfCoverageDinamico extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {


    public ListView listCoberturas;
    LinearLayout header,centerListView, bottomTexts;
    String valueOfMcard, nombre, apellido, typeOfId, numberOfId, numberOfMcard;
    CustomizedTextView textNameLastName, txtGetYourCertificate, txtTypeOfId, txtNumberOfId, txtNumberOfMcard;
    int idCuenta = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setView(R.layout.fragment_proof_dinamico, this);
        MyBus.getInstance().register(this);
        SharedPreferences prefs =
                getSharedPreferences("MisPreferencias", MODE_PRIVATE);

        nombre = prefs.getString("nombre", "Santiago");
        apellido = prefs.getString("apellido", "Castro");
        typeOfId = prefs.getString("typeOfId", "CC");
        numberOfId = prefs.getString("numberOfId", "7887787");
        numberOfMcard = prefs.getString("numberOfMcard", "123456789");
        String strIdCuenta = prefs.getString("idCuenta", "0");
        Log.e("typeOfId", typeOfId);
        Log.e("numberOfId", numberOfId);
        Log.e("numberOfMcard", numberOfMcard);
        idCuenta = Integer.valueOf(strIdCuenta);
    }

    @Override
    public void initViews(View root) {
        textNameLastName = (CustomizedTextView) root.findViewById(R.id.textNameLastNAme);
        txtTypeOfId = (CustomizedTextView) root.findViewById(R.id.txtTypeOfId);
        txtNumberOfId = (CustomizedTextView) root.findViewById(R.id.txtNumberOfId);
        txtNumberOfMcard = (CustomizedTextView) root.findViewById(R.id.txtNumberOfMcard);
        header = (LinearLayout) root.findViewById(R.id.customCoverageHeaderDinamico);
        centerListView = (LinearLayout) root.findViewById(R.id.customCoverageListDinamico);
        listCoberturas = (ListView) root.findViewById(R.id.mainListView);
        bottomTexts = (LinearLayout) root.findViewById(R.id.customTextCoverage);
    }

    @Override
    protected void onDestroy() {
        MyBus.getInstance().unregister(this);
        super.onDestroy();
    }

    public void onUpProofDinamico(View view) {
        onBackPressed();
    }


    public void onMenu(View view) {
        animate();
    }

    void setValues() {

        textNameLastName.setText(nombre + " " + apellido);
        txtTypeOfId.setText(typeOfId);
        txtNumberOfId.setText(numberOfId);
        txtNumberOfMcard.setText(numberOfMcard);
    }

    void setGetYourCertificateLayout() {
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

    void launchMcardActivity() {

        //Launch mCard Activity
        Intent i = new Intent(getApplicationContext(), Mcardhtml.class);
        startActivity(i);
        finish();
    }

}
