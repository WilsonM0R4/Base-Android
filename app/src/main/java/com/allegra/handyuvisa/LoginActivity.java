package com.allegra.handyuvisa;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
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

import com.allegra.handyuvisa.async.AsyncSoapObject;
import com.allegra.handyuvisa.async.AsyncSoapObjectTest;
import com.allegra.handyuvisa.async.AsyncTaskSoapObjectResultEvent;
import com.allegra.handyuvisa.async.AsyncTaskSoapObjectResultEventMcard;
import com.allegra.handyuvisa.async.MyBus;
import com.allegra.handyuvisa.models.AllemUser;
import com.allegra.handyuvisa.models.McardCliente;
import com.allegra.handyuvisa.models.UserDataBase;
import com.allegra.handyuvisa.parsers.SoapObjectParsers;
import com.allegra.handyuvisa.utils.Constants;
import com.allegra.handyuvisa.utils.CustomizedTextView;
import com.allegra.handyuvisa.utils.KeySaver;
import com.allegra.handyuvisa.utils.UsuarioSQLiteHelper;
import com.allegra.handyuvisa.utils.Util;
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
    private int idCuenta;
    //private ImageButton ib_visibilitypass;
    private boolean passIsVisible = false;
    private EditText username, password;
    private CustomizedTextView  btn_login, btn_newaccount;
    private ArrayList<NameValuePair> postValues;
    private ArrayList<String> arrayListMemberships;
    private ProgressBar pb_login;
    private TextView version, forgotpass;
    final String SPLUNK_API_KEY = "e74061f2";
    UsuarioSQLiteHelper db;

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
        Mint.setApplicationEnvironment(Mint.appEnvironmentTesting);
        Mint.initAndStartSession(getApplicationContext(), SPLUNK_API_KEY);
        // Enable logging
        Mint.enableLogging(true);
        // Log last 100 messages
        Mint.setLogging(200);

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
        forgotpass = (TextView)root.findViewById(R.id.forgotPassword);
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
                    AsyncSoapObject.getInstance(Constants.URL_LOGIN_PROD, Constants.NAMESPACE_ALLEM,//Constants.getWSDL()
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

    //Response from SOAP Service, get Mcards purchased by an user
    @Subscribe
    public void onAsyncTaskResult(AsyncTaskSoapObjectResultEventMcard event) {
        Log.d("SergioMcardEntra", event.getFaultString());
        if (event.getCodeRequest() == Constants.MCARD_CODE){
            Log.d("SergioMcard", event.getFaultString());
            //At least one mCard
            if (event.getResult() != null) {

                //Get data
                McardCliente mcardCliente = SoapObjectParsers.toMcardCliente(event.getResult());
                String idMcard = mcardCliente.getIdProducto();
                String numMcard = mcardCliente.getNumeroMembresia();
                //Save in SharedPreferences
                SharedPreferences prefs =
                        getSharedPreferences("MisPreferencias", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                if (idMcard.equals("")){
                    editor.putString("idMcard", "0");
                    editor.putString("numMcard","XXXYXYY");
                } else{
                    editor.putString("idMcard", idMcard);
                    editor.putString("numMcard",numMcard);
                }

                editor.apply();
                Log.d("idMcard", "Es"+ idMcard);
                Log.d("numMcard", "Es"+numMcard);
            }
        }
    }

    //Response from SOAP Service, get user login info
    @Subscribe
    public void onAsyncTaskResult(AsyncTaskSoapObjectResultEvent event) {
        Log.d("SergioEntra", event.getFaultString());
        if (event.getCodeRequest() == Constants.ACTIVITY_LOGIN) {
            setWaitinUI(false);
            if (event.getResult() != null) {

                //Create an AllemUser object and set values
                AllemUser user = SoapObjectParsers.toAllemUser(event.getResult());
                McardCliente mcardCliente = SoapObjectParsers.toMcardCliente(event.getResult());
                ((VisaCheckoutApp) this.getApplication()).setIdSession(user.idSesion);
                ((VisaCheckoutApp) this.getApplication()).setIdCuenta(user.idCuenta);
                ((VisaCheckoutApp) this.getApplication()).setRawPassword(password.getText().toString());
                //Get values for work with these
                String name = user.email.substring(0, user.email.indexOf('@'));
                String domain = user.email.substring(user.email.indexOf('@') + 1, user.email.length()).replace(".", "");
                String channel = name + domain + user.idCuenta;
                UAirship.shared().getNamedUser().setId(channel);
                String password = user.hashpassword;
                String cel_code = user.celular_codigo;
                String typeOfId = user.idType;
                String numMcard = mcardCliente.getNumeroMembresia();
                String numberOfId = user.idNumber;
                //Log.d("Sergio", "Es: "+cel_code);

                String nombre = user.nombre;
                String apellido = user.apellido;
                //Save in SharedPreferences
                SharedPreferences prefs =
                        getSharedPreferences("MisPreferencias", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                editor.putString("apellido", apellido);
                editor.putString("nombre", nombre);
                editor.putString("typeOfId",getTypeOfDocumentFromIdCode(typeOfId));
                editor.putString("numberOfId",numberOfId);
                //editor.putString("celular_codigo",cel_code);
                idCuenta = user.idCuenta;
                editor.putString("idCuenta",String.valueOf(idCuenta));
                editor.apply();
                //Launch SOAP request for mCard
                if (postValues.size() > 0) postValues.clear();
                postValues.add(new BasicNameValuePair("idCuenta", String.valueOf(idCuenta)));
                AsyncSoapObjectTest asyncSoapObjectTest = new AsyncSoapObjectTest(getApplicationContext());
                asyncSoapObjectTest.getInstance2(Constants.SOAP_URL_MCARD_PROD, Constants.MCARD_NAMESPACE,
                        Constants.MCARD_METHOD, postValues, Constants.MCARD_CODE).execute();
                Constants.saveUser(ctx, user, channel);
                db.addUser(new UserDataBase(nombre,apellido,getTypeOfDocumentFromIdCode(typeOfId),numberOfId,numMcard,"$100.000", "$100.000","$100.000"));
                //********
                Intent returnIntent = new Intent();
                    Log.e("Sergio", "Acá sí");
                    setResult(RESULT_OK, returnIntent);
                    finish();

            } else {
                Toast.makeText(ctx, event.getFaultString(), Toast.LENGTH_LONG).show();
            }

        }
    }

    public String getTypeOfDocumentFromIdCode(String typeOfId){
        String defaultType = "";

        switch (typeOfId){
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