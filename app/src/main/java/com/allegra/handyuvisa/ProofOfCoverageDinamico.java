package com.allegra.handyuvisa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.allegra.handyuvisa.ProofDinamico.asyncProofDynamic.AsyncSoapObjectProofDynamic;
import com.allegra.handyuvisa.ProofDinamico.asyncProofDynamic.AsyncTaskSoapObjectResultEventProofDynamic;
import com.allegra.handyuvisa.ProofDinamico.model.Cobertura;
import com.allegra.handyuvisa.ProofDinamico.model.Poliza;
import com.allegra.handyuvisa.async.MyBus;
import com.allegra.handyuvisa.models.AllemUser;
import com.allegra.handyuvisa.parsers.SoapObjectParsers;
import com.allegra.handyuvisa.utils.Connectivity;
import com.allegra.handyuvisa.utils.Constants;
import com.allegra.handyuvisa.utils.CustomAdapterDinamico;
import com.allegra.handyuvisa.utils.CustomizedTextView;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by jsandoval on 22/11/16.
 */

public class ProofOfCoverageDinamico extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {

    ImageView iv_allegra_proof, load_circle_proof;
    ArrayList<Cobertura> dataModelstest;
    private static CustomAdapterDinamico adapter;
    RelativeLayout rl_body_proof;
    public ListView listCoberturas;
    ImageButton back;
    LinearLayout header,centerListView, bottomLayout;
    String valueOfMcard, nombre, apellido, typeOfId, numberOfId, numberOfMcard;
    CustomizedTextView textNameLastName, txtGetYourCertificate, txtTypeOfId, txtNumberOfId, txtNumberOfMcard;
    int idCuenta = 0;
    final String TAG = "ProofOfCoverageDinamico";
    private ArrayList<NameValuePair> postValues;
    boolean mostrarAppCobertura = true, mostrarAppBeneficios = true, mostrarSoloPolizaPrincipal = true;

    //*********************************OVERRIDE METHODS********************************
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
        numberOfMcard = prefs.getString("numMcard", "123456789");
        String strIdCuenta = prefs.getString("idCuenta", "0");
        Log.e("typeOfId", typeOfId);
        Log.e("numberOfId", numberOfId);
        Log.e("numberOfMcard", numberOfMcard);
        idCuenta = Integer.valueOf(strIdCuenta);
        getValuesDynamicProofOfCoverage();
    }

    @Override
    public void initViews(View root) {

        back = (ImageButton)root.findViewById(R.id.iv_back_dinamico);
        textNameLastName = (CustomizedTextView) root.findViewById(R.id.textNameLastNAme);
        txtTypeOfId = (CustomizedTextView) root.findViewById(R.id.txtTypeOfId);
        txtNumberOfId = (CustomizedTextView) root.findViewById(R.id.txtNumberOfId);
        txtNumberOfMcard = (CustomizedTextView) root.findViewById(R.id.txtNumberOfMcard);
        header = (LinearLayout) root.findViewById(R.id.customCoverageHeaderDinamico);
        centerListView = (LinearLayout) root.findViewById(R.id.customCoverageListDinamico);
        listCoberturas = (ListView) root.findViewById(R.id.mainListView);
        bottomLayout = (LinearLayout)root.findViewById(R.id.customCoverageBottomDinamico);
        load_circle_proof = (ImageView)root.findViewById(R.id.load_circle_proof);
        rl_body_proof = (RelativeLayout) root.findViewById(R.id.rl_body_proof);
        iv_allegra_proof = (ImageView)root.findViewById(R.id.iv_allegra_proof);
        //bottomTexts = (LinearLayout) root.findViewById(R.id.customTextCoverage);

        dataModelstest= new ArrayList<>();

        load_circle_proof.post(new Runnable() {
            @Override
            public void run() {
                ((AnimationDrawable) load_circle_proof.getBackground()).start();
            }
        });

        listCoberturas.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onUpProofDinamico(view);
            }
        });
    }

    @Override
    protected void onDestroy() {
        MyBus.getInstance().unregister(this);
        super.onDestroy();
    }

    //Receive  SOAP response from SERVER for Dynamic Proof of coverage's info
    @Subscribe
    public void onAsyncTaskResult(AsyncTaskSoapObjectResultEventProofDynamic event) {

        if (event.getResult() != null) {
            Poliza poliza = SoapObjectParsers.toPoliza(event.getResult());
            //Already we have Poliza object, now save this number in SharedPreferences
            String numeroPoliza = poliza.getNumeroPoliza();
            //Save in SharedPreferences
            SharedPreferences prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("numMcard", numeroPoliza);
            //Get coberturas' list  y save it
            ArrayList<Cobertura> coberturas = poliza.getCoberturas();
            Gson gson = new Gson();
            String strCoberturas = gson.toJson(coberturas);
            editor.putString("coberturas",strCoberturas);
            editor.apply();
            //Validate if user have poliza
            if (numeroPoliza.equals("NO_TIENE")){
                Log.d("NO TENGO MCARD", "NO TENGO ");
                setGetYourCertificateLayout();
            } else{
                //Read from SharedPreferences
                SharedPreferences preferences = this.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                String lista = preferences.getString("coberturas", "3");
                Log.d(TAG, "Lista "+lista);
                Type type = new TypeToken<ArrayList<Cobertura>>(){}.getType();
                ArrayList<Cobertura> carsList = gson.fromJson(lista, type);
                //Fill adapter and show listView with coberturas
                adapter= new CustomAdapterDinamico(carsList,getApplicationContext());
                listCoberturas.setAdapter(adapter);
                Log.d("SI TENGO MCARD", "SI TENGO ");
                AllemUser allemUser = Constants.getUser(getApplicationContext());
                nombre = allemUser.nombre;
                apellido = allemUser.apellido;
                typeOfId = getTypeOfIdForDisplay(allemUser.idType);
                numberOfId = allemUser.idNumber;
                numberOfMcard = numeroPoliza;
                Log.d("nombre", nombre);
                Log.d("apellido", apellido);
                Log.d("typeOfId", typeOfId);
                Log.d("numberOfId", numberOfId);
                Log.d("numeroMcard", numberOfMcard);
                setValues();
                //Hide Loader
                rl_body_proof.setVisibility(View.VISIBLE);
                iv_allegra_proof.setVisibility(View.GONE);
                load_circle_proof.setVisibility(View.GONE);
            }

        }
    }

    //*********************PROPER METHODS********************

    //Performs SOAP  request to SERVER
    void getValuesDynamicProofOfCoverage(){

        SharedPreferences preferences = this.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        String idCuentaAIM = preferences.getString("idCuenta", "3");
        String idPortal = Constants.ID_PORTAL;
        Log.d(TAG, "El id es: "+idCuentaAIM);

        //ARM REQUEST
        postValues = new ArrayList<>();
        if (postValues.size() > 0) postValues.clear();
        postValues.add(new BasicNameValuePair("idCuentaAIM", idCuentaAIM));
        postValues.add(new BasicNameValuePair("idPortal", idPortal));
        postValues.add(new BasicNameValuePair("mostrarAppCobertura", Boolean.toString(mostrarAppCobertura)));
        postValues.add(new BasicNameValuePair("mostrarAppBeneficios", Boolean.toString(mostrarAppBeneficios)));
        postValues.add(new BasicNameValuePair("mostrarSoloPolizaPrincipal", Boolean.toString(mostrarSoloPolizaPrincipal)));

        //Boolean Flags

        Log.d(TAG, "Booleans "+mostrarAppCobertura);

        //If there is internet connection, send request
        if (Connectivity.isConnected(getApplicationContext()) || Connectivity.isConnectedWifi(getApplicationContext()) || Connectivity.isConnectedMobile(getApplicationContext())) {
            Log.d(TAG, "Entra al internet");
            AsyncSoapObjectProofDynamic.getInstance(Constants.getUrlDynamicProof(), Constants.NAMESPACE_PROOF,
                 Constants.METHOD_PROOF,  postValues, Constants.REQUEST_CODE_PROOF).execute();

        }else {
            Toast.makeText(getApplicationContext(), R.string.err_no_internet, Toast.LENGTH_SHORT).show();
        }

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
        bottomLayout.setVisibility(View.GONE);
        //bottomTexts.setVisibility(View.GONE);
        setContentView(R.layout.get_your_certificate_dynamic);
        txtGetYourCertificate = (CustomizedTextView) findViewById(R.id.txtGetYourCertificatedynamic);
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

    private String getTypeOfIdForDisplay(String idType) {

        String strType = "";
        switch (idType) {

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

}
