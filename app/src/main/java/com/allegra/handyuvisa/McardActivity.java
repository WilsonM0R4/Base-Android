package com.allegra.handyuvisa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


/**
 * Created by jsandoval on 23/05/16.
 */
public class McardActivity extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener{

    private static final String TAG = "McardActivity";
    private Button callPrivilege,callPremium,callExclusive,callUnlimited, infoPrivilege, infoPremium,
            infoExclusive, infoUnlimited;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.fragment_mcard, this);
    }

    @Override
    public void onDestroy() {
        //MyBus.getInstance().unregister(this);
        super.onDestroy();
    }

    public void onMenu(View view) {
        animate();
    }

    @Override
    public void initViews(View root) {initializePanels(root);}

    private void initializePanels(final View root){

        callPrivilege = (Button)root.findViewById(R.id.btnCallUsPrivilege);
        callPremium = (Button)root.findViewById(R.id.btnCallUsPremium);
        callExclusive = (Button)root.findViewById(R.id.btnCallUsExclusive);
        callUnlimited = (Button)root.findViewById(R.id.btnCallUsUnlimited);
        infoPrivilege = (Button)root.findViewById(R.id.btnMoreInfoPrivilege);
        infoPremium = (Button)root.findViewById(R.id.btnMoreInfoPremium);
        infoExclusive = (Button)root.findViewById(R.id.btnMoreInfoExclusive);
        infoUnlimited = (Button)root.findViewById(R.id.btnMoreInfoUnlimited);
        callUs();
        info();

    }

    private void sendCall(){
        Intent intent = new Intent(this,CallActivityMcard.class);
        startActivity(intent);
    }

    private void sendInfoPrivilege(){
        Intent intent = new Intent(this, Mcardprivilege.class);
        startActivity(intent);
    }

    private void sendInfoPremium(){
        Intent intent = new Intent(this, Mcardpremium.class);
        startActivity(intent);
    }

    private void sendInfoExclusive(){
        Intent intent = new Intent(this, Mcardexclusive.class);
        startActivity(intent);
    }

    private void sendInfoUnlimited(){
        Intent intent = new Intent(this, Mcardunlimited.class);
        startActivity(intent);
    }

    private void callUs(){

        callPrivilege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Juan","Llamando Privilege");
                sendCall();
            }
        });
        callPremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Juan","Llamando Premium");
                sendCall();
            }
        });
        callExclusive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Juan","Llamando Exclusive");
                sendCall();
            }
        });
        callUnlimited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Juan","Llamando Unlimited");
                sendCall();
            }
        });
    }

    private void info(){
        infoPrivilege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Juan","more info privilege");
                sendInfoPrivilege();
            }
        });
        infoPremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Juan", "more info premium");
                sendInfoPremium();
            }
        });
        infoExclusive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Juan", "more info exclusive");
                sendInfoExclusive();
            }
        });
        infoUnlimited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Juan", "more info unlimited");
                sendInfoUnlimited();
            }
        });

    }


}
