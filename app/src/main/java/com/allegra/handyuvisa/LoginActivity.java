package com.allegra.handyuvisa;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.allegra.handyuvisa.ProofDinamico.asyncProofDynamic.AsyncSoapObjectProofDynamic;
import com.allegra.handyuvisa.ProofDinamico.asyncProofDynamic.AsyncTaskSoapObjectResultEventProofDynamic;
import com.allegra.handyuvisa.ProofDinamico.model.Cobertura;
import com.allegra.handyuvisa.ProofDinamico.model.Poliza;
import com.allegra.handyuvisa.async.AsyncSoapObject;
import com.allegra.handyuvisa.async.AsyncSoapObjectTest;
import com.allegra.handyuvisa.async.AsyncTaskSoapObjectResultEvent;
import com.allegra.handyuvisa.async.AsyncTaskSoapObjectResultEventMcard;
import com.allegra.handyuvisa.async.MyBus;
import com.allegra.handyuvisa.models.AllemUser;
import com.allegra.handyuvisa.models.McardCliente;
import com.allegra.handyuvisa.models.UserDataBase;
import com.allegra.handyuvisa.parsers.SoapObjectParsers;
import com.allegra.handyuvisa.utils.Connectivity;
import com.allegra.handyuvisa.utils.Constants;
import com.allegra.handyuvisa.utils.CustomizedTextView;
import com.allegra.handyuvisa.utils.KeySaver;
import com.allegra.handyuvisa.utils.UsuarioSQLiteHelper;
import com.allegra.handyuvisa.utils.Util;
import com.google.gson.Gson;
import com.splunk.mint.Mint;
import com.squareup.otto.Subscribe;
import com.urbanairship.UAirship;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by victor on 19/02/15.
 * com.allem.allemevent.fragactiv
 */
public class LoginActivity extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {

    private final String TAG = "LoginActivity";
    private ActionBar actionBar;
    private Context ctx;
    String nombre, apellido, typeOfId, numberOfId, numberOfMcard;
    //private ImageButton ib_visibilitypass;
    private boolean passIsVisible = false;
    private EditText username, password;
    private CustomizedTextView btn_login, btn_newaccount;
    private ArrayList<String> arrayListMemberships;
    private ProgressBar pb_login;
    private TextView version, forgotpass;
    int idCuenta = 0;
    private String idMcard, numMcard;
    int idMcard1 = 0;
    private String valueOfMcard;
    UsuarioSQLiteHelper db;
    SQLiteDatabase dbbase;
    private static final String SOUND_NOTIFICATIONS = "android.resource://com.allegra.handyuvisa/raw/allegra_sound";
    public UAirship uAirship;
    private ArrayList<NameValuePair> postValues;
    boolean mostrarAppCobertura = true, mostrarAppBeneficios = true, mostrarSoloPolizaPrincipal = true;


    //*************************OVERRIDE METHODS*********************
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d(TAG, "Enter LoginActivity");
        ctx = this;
        MyBus.getInstance().register(this);
        super.setView(R.layout.fragment_login, this);
        postValues = new ArrayList<>();
        db = new UsuarioSQLiteHelper(this);

        //TODO: Uncomment this before push
        //Splunk
       /* Mint.setApplicationEnvironment(Mint.appEnvironmentTesting);
        Mint.initAndStartSession(getApplicationContext(), Constants.SPLUNK_API_KEY);
        // Enable logging
        Mint.enableLogging(true);
        // Log last 100 messages
        Mint.setLogging(200);*/
        findValueOfMcard();
        //Error for test Splunk
        /*String str =  null;
        Log.d(TAG, str);*/

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

        try {
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            TextView version = (TextView) root.findViewById(R.id.version);
            if (version != null) {
                version.setText("Version: " + versionName);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, "No Version number found");
        }
        forgotpass = (TextView) root.findViewById(R.id.forgotPassword);
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, LoginForgotActivity.class);
                startActivity(intent);
            }
        });
        //ib_visibilitypass = (ImageButton) root.findViewById(R.id.ib_visibilitypass);
        username = (EditText) root.findViewById(R.id.et_email);
        password = (EditText) root.findViewById(R.id.et_password);
        btn_login = (CustomizedTextView) root.findViewById(R.id.btn_login);
        btn_newaccount = (CustomizedTextView) root.findViewById(R.id.btn_register);
        btn_login.setEnabled(false);
        pb_login = (ProgressBar) root.findViewById(R.id.pb_login);
        pb_login.setVisibility(View.GONE);
        if (KeySaver.getStringSavedShare(ctx, Constants.KEY_EMAIL) != null) {
            username.setText(KeySaver.getStringSavedShare(ctx, Constants.KEY_EMAIL));
            //password.setFocusableInTouchMode(true);
            password.requestFocus();
            password.postDelayed(new Runnable() {
                public void run() {
                    final InputMethodManager inputMethodManager = (InputMethodManager) ctx.getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(password, InputMethodManager.SHOW_IMPLICIT);
                }
            }, 300);
        }
        setListeners();
    }


    private void setListeners() {

           /*ib_visibilitypass.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   if (passIsVisible) {
                       password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                       passIsVisible = false;
                       ib_visibilitypass.setImageResource(R.drawable.ic_visibility_off_black_18dp);
                   } else {
                       password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                       passIsVisible = true;
                       ib_visibilitypass.setImageResource(R.drawable.ic_visibility_black_18dp);
                   }
                   password.setSelection(password.length());
               }
           });*/
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(editable.toString())
                        .matches() && password.length() > 0) {
                    btn_login.setEnabled(true);
                } else {
                    btn_login.setEnabled(false);
                }
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0 && android.util.Patterns.EMAIL_ADDRESS.matcher(username.getText().toString())
                        .matches()) {
                    btn_login.setEnabled(true);
                } else {
                    btn_login.setEnabled(false);
                }
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Util.hasInternetConnectivity(ctx)) {
                    setWaitinUI(true);
                    if (postValues.size() > 0) postValues.clear();
                    postValues.add(new BasicNameValuePair("email", username.getText().toString()));
                    postValues.add(new BasicNameValuePair("password", password.getText().toString()));
                    AsyncSoapObject.getInstance(Constants.getALLEM_BASE(), Constants.NAMESPACE_ALLEM,
                            Constants.METHOD_INICIAR_SESION, postValues, Constants.ACTIVITY_LOGIN).execute();

                } else {
                    Toast.makeText(ctx, R.string.err_no_internet, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_newaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ctx, LoginNewUser.class);
                LoginActivity.this.startActivityForResult(i, Constants.ACTIVITY_LOGIN_NEW_USER);
            }
        });


    }

    private void setWaitinUI(boolean b) {

        if (b) pb_login.setVisibility(View.VISIBLE);
        else pb_login.setVisibility(View.GONE);
        username.setEnabled(!b);
        password.setEnabled(!b);
        btn_login.setEnabled(!b);
        btn_newaccount.setEnabled(!b);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.ACTIVITY_LOGIN_NEW_USER) {
            Intent returnIntent = new Intent();
            if (resultCode == RESULT_OK) {
                setResult(resultCode, returnIntent);
                finish();
            }
        }
        /*if (requestCode == Constants.REQUEST_CODE_HOTELS) {
            Intent returnIntent = new Intent();
            if (resultCode == RESULT_OK) {
                Log.e("Sergio", "Acá sí");
                setResult(resultCode, returnIntent);
                finish();
            }
        }*/

    }

    //SOAP Response from new Request Dynamic Proof of coverage
    @Subscribe
    public void onAsyncTaskResult(AsyncTaskSoapObjectResultEventProofDynamic event) {
        //Log.d(TAG, event.getFaultString());

        if (event.getResult() != null) {
            Poliza poliza = SoapObjectParsers.toPoliza(event.getResult());
            String str = event.getResult().toString();
            Log.d(TAG, "Resultado: "+str);
            AllemUser allemUser = Constants.getUser(getApplicationContext());
            //AllemUser allemUser = SoapObjectParsers.toAllemUser(event.getResult());
            //((VisaCheckoutApp) this.getApplication()).setIdSession(allemUser.idSesion);
            /*nombre = allemUser.nombre;
            apellido = allemUser.apellido;
            typeOfId = getTypeOfIdForDisplay(allemUser.idType);
            numberOfId = allemUser.idNumber;
            Log.d("nombre", nombre);
            Log.d("apellido", apellido);
            Log.d("typeOfId", typeOfId);
            Log.d("numberOfId", numberOfId);
            */
            //******************
            Log.d(TAG, "Llega al Poliza call");

            //Ya se tiene el objeto Póliza, ahora guardar el número en SharedPreferences
            String numeroPoliza = poliza.getNumeroPoliza();
            Log.d(TAG, "numero: "+numeroPoliza);
            //Save in SharedPreferences
            SharedPreferences prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("numMcard", numeroPoliza);

            //Obtener el listado de coberturas y guardarlo
            ArrayList<Cobertura> coberturas = poliza.getCoberturas();
            Gson gson = new Gson();
            String strCoberturas = gson.toJson(coberturas);
            Log.d("TAG","Coberturas = " + strCoberturas);
            editor.putString("coberturas",strCoberturas);
            editor.apply();

        }
    }

    //Response from SOAP Service, get Mcards purchased by an user
    /*@Subscribe
    public void onAsyncTaskResult(AsyncTaskSoapObjectResultEventMcard event) {
        Log.d("SergioMcardEntra", event.getFaultString());
        if (event.getCodeRequest() == Constants.MCARD_CODE) {
            Log.d("SergioMcard", event.getFaultString());
            //At least one mCard
            if (event.getResult() != null) {

                //Get data
                McardCliente mcardCliente = SoapObjectParsers.toMcardCliente(event.getResult());
                idMcard = mcardCliente.getIdProducto();
                numMcard = mcardCliente.getNumeroMembresia();
                //Save in SharedPreferences
                SharedPreferences prefs =
                        getSharedPreferences("MisPreferencias", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                if (idMcard.equals("")) {
                    editor.putString("idMcard", "0");
                    editor.putString("numMcard", "XXXYXYY");
                } else {
                    editor.putString("idMcard", idMcard);
                    editor.putString("numMcard", numMcard);
                }

                editor.apply();
                Log.d("idMcard", "Es" + idMcard);
                Log.d("numMcard", "Es" + numMcard);
            }
        }
    }*/

    //Response from SOAP Service, get user login info
    @Subscribe
    public void onAsyncTaskResult(AsyncTaskSoapObjectResultEvent event) {

        Log.d("SergioEntra", event.getFaultString());
        if (event.getCodeRequest() == Constants.ACTIVITY_LOGIN) {

            if (event.getResult() != null) {

                //Create an AllemUser object and set values
                AllemUser user = SoapObjectParsers.toAllemUser(event.getResult());
                //McardCliente mcardCliente = SoapObjectParsers.toMcardCliente(event.getResult());
                ((VisaCheckoutApp) this.getApplication()).setIdSession(user.idSesion);
                ((VisaCheckoutApp) this.getApplication()).setIdCuenta(user.idCuenta);
                ((VisaCheckoutApp) this.getApplication()).setRawPassword(password.getText().toString());

                //Get values for work with these
                String name = user.email.substring(0, user.email.indexOf('@'));
                String domain = user.email.substring(user.email.indexOf('@') + 1, user.email.length()).replace(".", "");
                String channel = name + domain + user.idCuenta;
                //Notifications UrbanAirship
                UAirship.shared().getPushManager().editTags()
                        .addTag(channel)
                        .apply();
                Log.d("CHANNEL: ", channel);
                String password = user.hashpassword;
                String cel_code = user.celular_codigo;
                String typeOfId = user.idType;
                //String numMcard = mcardCliente.getNumeroMembresia();
                String numberOfId = user.idNumber;

                String nombre = user.nombre;
                String apellido = user.apellido;
                //Save in SharedPreferences
                SharedPreferences prefs =
                        getSharedPreferences("MisPreferencias", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                editor.putString("apellido", apellido);
                editor.putString("nombre", nombre);
                editor.putString("typeOfId", getTypeOfDocumentFromIdCode(typeOfId));
                editor.putString("numberOfId", numberOfId);

                idCuenta = user.idCuenta;
                editor.putString("idCuenta", String.valueOf(idCuenta));
                editor.apply();
                //Launch SOAP request for mCard
                if (postValues.size() > 0) postValues.clear();
                postValues.add(new BasicNameValuePair("idCuenta", String.valueOf(idCuenta)));
               /* AsyncSoapObjectTest asyncSoapObjectTest = new AsyncSoapObjectTest(getApplicationContext());
                asyncSoapObjectTest.getInstance2(Constants.getMcardUrl(), Constants.MCARD_NAMESPACE,
                        Constants.MCARD_METHOD, postValues, Constants.MCARD_CODE).execute();*/
                getValuesDynamicProofOfCoverage();
                Constants.saveUser(ctx, user, channel);
                valueOfMcard = prefs.getString("idMcard", "0");
                idMcard1 = Integer.valueOf(valueOfMcard);
                db.addUser(new UserDataBase(nombre, apellido, getTypeOfDocumentFromIdCode(typeOfId), numberOfId, prefs.getString("numMcard", numMcard), findValueOfMcard(), findValueOfMcard(), findValueOfMcard()));
                setWaitinUI(false);
                //*CHECK IF DATA BASE EXIST*/
                Intent returnIntent = new Intent();
                Log.e(TAG, "Acá sí");
                setResult(RESULT_OK, returnIntent);
                finish();

            } else {
                Toast.makeText(ctx, event.getFaultString(), Toast.LENGTH_LONG).show();
            }

        }
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


    public String findValueOfMcard() {

        //********************* PRIVILEGE       PREMIUM         EXCLUSIVE            UNLIMITED
        String[] arrayMcards = {"USD $100,000", "USD $250,000", "USD $1'000,000", "USD $2'000,000"};
        //********************      212             208             209             210

        switch (idMcard1) {
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


        }
        return valueOfMcard;

    }

    public String getTypeOfDocumentFromIdCode(String typeOfId) {
        String defaultType = "";

        switch (typeOfId) {
            case "1":
                return "CC";
            case "2":
                return "CE";
            case "3":
                return "NIT";
            case "4":
                return "TI";
            case "5":
                return "PS";
            case "10":
                return "NUIP";
            case "9":
                return "OTRO";
        }

        return defaultType;
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Log.d(TAG, "back pressed");
        setResult(RESULT_CANCELED);
        finish();
        overridePendingTransition(R.animator.back_slide_in, R.animator.front_slide_out);
    }

    public void onMenu(View view) {
        animate();
    }


}