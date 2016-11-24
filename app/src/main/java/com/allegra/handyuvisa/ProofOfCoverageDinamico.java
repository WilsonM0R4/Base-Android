package com.allegra.handyuvisa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import java.lang.reflect.Type;
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
import com.allegra.handyuvisa.utils.DataModelTest;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jsandoval on 22/11/16.
 */

public class ProofOfCoverageDinamico extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {

    //ArrayList<DataModelTest> dataModelstest;
    ArrayList<Cobertura> dataModelstest;
    private static CustomAdapterDinamico adapter;

    public ListView listCoberturas;
    LinearLayout header,centerListView, bottomLayout;
    String valueOfMcard, nombre, apellido, typeOfId, numberOfId, numberOfMcard;
    CustomizedTextView textNameLastName, txtGetYourCertificate, txtTypeOfId, txtNumberOfId, txtNumberOfMcard;
    int idCuenta = 0;
    final String TAG = "ProofOfCoverageDinamico";
    private ArrayList<NameValuePair> postValues;
    boolean mostrarAppCobertura = true, mostrarAppBeneficios = true, mostrarSoloPolizaPrincipal = true;

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


        textNameLastName = (CustomizedTextView) root.findViewById(R.id.textNameLastNAme);
        txtTypeOfId = (CustomizedTextView) root.findViewById(R.id.txtTypeOfId);
        txtNumberOfId = (CustomizedTextView) root.findViewById(R.id.txtNumberOfId);
        txtNumberOfMcard = (CustomizedTextView) root.findViewById(R.id.txtNumberOfMcard);
        header = (LinearLayout) root.findViewById(R.id.customCoverageHeaderDinamico);
        centerListView = (LinearLayout) root.findViewById(R.id.customCoverageListDinamico);
        listCoberturas = (ListView) root.findViewById(R.id.mainListView);
        bottomLayout = (LinearLayout)root.findViewById(R.id.customCoverageBottomDinamico);
        //bottomTexts = (LinearLayout) root.findViewById(R.id.customTextCoverage);

        dataModelstest= new ArrayList<>();

        /*dataModelstest.add(new DataModelTest("Apple Pie", "Android 1.0"));
        dataModelstest.add(new DataModelTest("Banana Bread", "Android 1.1"));
        dataModelstest.add(new DataModelTest("Cupcake", "Android 1.5"));
        dataModelstest.add(new DataModelTest("Donut","Android 1.6"));
        dataModelstest.add(new DataModelTest("Eclair", "Android 2.0"));
        dataModelstest.add(new DataModelTest("Froyo", "Android 2.2"));
        dataModelstest.add(new DataModelTest("Gingerbread", "Android 2.3"));
        dataModelstest.add(new DataModelTest("Honeycomb","Android 3.0"));
        dataModelstest.add(new DataModelTest("Ice Cream Sandwich", "Android 4.0"));
        dataModelstest.add(new DataModelTest("Jelly Bean", "Android 4.2"));
        dataModelstest.add(new DataModelTest("Kitkat", "Android 4.4"));
        dataModelstest.add(new DataModelTest("Lollipop","Android 5.0"));
        dataModelstest.add(new DataModelTest("Marshmallow", "Android 6.0"));

        adapter= new CustomAdapterDinamico(dataModelstest,getApplicationContext());*/

        //Read from SharedPreferences
        SharedPreferences preferences = this.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        String lista = preferences.getString("coberturas", "3");
        Log.d(TAG, "Lista "+lista);
        Type type = new TypeToken<ArrayList<Cobertura>>(){}.getType();
        Gson gson = new Gson();
        ArrayList<Cobertura> carsList = gson.fromJson(lista, type);
        adapter= new CustomAdapterDinamico(carsList,getApplicationContext());
        listCoberturas.setAdapter(adapter);


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
    }

    @Override
    protected void onDestroy() {
        MyBus.getInstance().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void onAsyncTaskResult(AsyncTaskSoapObjectResultEventProofDynamic event) {
        Log.d(TAG, event.getFaultString());

            if (event.getResult() != null) {
                String str = event.getResult().toString();
                Log.d(TAG, "Resultado: "+str);
                AllemUser allemUser = Constants.getUser(getApplicationContext());
                Poliza poliza = SoapObjectParsers.toPoliza(event.getResult());
                String numMcard = poliza.getNumeroPoliza();
                //AllemUser allemUser = SoapObjectParsers.toAllemUser(event.getResult());
                //((VisaCheckoutApp) this.getApplication()).setIdSession(allemUser.idSesion);
                nombre = allemUser.nombre;
                apellido = allemUser.apellido;
                typeOfId = getTypeOfIdForDisplay(allemUser.idType);
                numberOfId = allemUser.idNumber;
                numberOfMcard = numMcard;
                Log.d("nombre", nombre);
                Log.d("apellido", apellido);
                Log.d("typeOfId", typeOfId);
                Log.d("numberOfId", numberOfId);
                Log.d("numeroMcard", numberOfMcard);
                setValues();

            }
    }

    //*********************PROPER METHODS********************

    void getValuesDynamicProofOfCoverage(){

        SharedPreferences preferences = this.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        String idCuentaAIM = preferences.getString("idCuenta", "3");
        String idPortal = Constants.ID_PORTAL;
        Log.d(TAG, "El id es: "+idCuentaAIM);

        //ARM REQUEST
        postValues = new ArrayList<>();
        if (postValues.size() > 0) postValues.clear();
        postValues.add(new BasicNameValuePair("idCuentaAIM", idCuentaAIM));//"10489"
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
