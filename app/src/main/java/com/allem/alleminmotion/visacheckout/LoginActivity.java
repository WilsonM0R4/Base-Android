package com.allem.alleminmotion.visacheckout;

/**
 * Created by victor on 19/02/15.
 * com.allem.allemevent.fragactiv
 */

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.allem.alleminmotion.visacheckout.async.AsyncSoapObject;
import com.allem.alleminmotion.visacheckout.async.AsyncTaskSoapObjectResultEvent;
import com.allem.alleminmotion.visacheckout.async.MyBus;
import com.allem.alleminmotion.visacheckout.models.AllemUser;
import com.allem.alleminmotion.visacheckout.models.CuentaCliente;
import com.allem.alleminmotion.visacheckout.models.McardCliente;
import com.allem.alleminmotion.visacheckout.parsers.SoapObjectParsers;
import com.allem.alleminmotion.visacheckout.utils.Constants;
import com.allem.alleminmotion.visacheckout.utils.CustomizedTextView;
import com.allem.alleminmotion.visacheckout.utils.KeySaver;
import com.allem.alleminmotion.visacheckout.utils.Util;
import com.squareup.otto.Subscribe;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;

import java.util.ArrayList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d(TAG, "Enter LoginActivity");
        ctx = this;
        MyBus.getInstance().register(this);
        super.setView(R.layout.fragment_login, this);
        postValues = new ArrayList<>();
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
            Log.e(TAG, "No Version number found");
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
                    final InputMethodManager inputMethodManager = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(password, InputMethodManager.SHOW_IMPLICIT);
                }
            }, 300);
        }

        setListeners();
        new TareaWSConsulta().execute();
        //getHigherMcard();
    }



   /* private void getHigherMcard(){
        if (Util.hasInternetConnectivity(ctx)) {
/*//************Get higher mCard********
            //Crear el request
           *//* SoapObject request = new SoapObject(Constants.MCARD_NAMESPACE, Constants.MCARD_METHOD);
            request.addProperty("idCuenta", idCuenta);
            //Crear el envelope
            SoapSerializationEnvelope envelope =
                    new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.setOutputSoapObject(request);

            McardCliente cuentaCliente = new McardCliente();
            cuentaCliente.setIdCuenta(idCuenta);
            PropertyInfo property = new PropertyInfo();
            property.setName(McardCliente.PROPERTY);
            property.setValue(cuentaCliente);
            property.setType(cuentaCliente.getClass());
            Log.e("SerIdCuentaHigherMcard",String.valueOf(idCuenta));*//*
            //new TareaWSConsulta().execute();
           *//* if (postValues.size() > 0) postValues.clear();

            postValues.add(new BasicNameValuePair("idCuenta", String.valueOf(idCuenta)));
            AsyncSoapObject.getInstance2(Constants.SOAP_URL_MCARD, Constants.MCARD_NAMESPACE,
                    Constants.MCARD_METHOD, property, Constants.ACTIVITY_PROOF_OF_COVERAGE).execute();*//*
        } else {
            Toast.makeText(ctx, getString(R.string.err_no_internet), Toast.LENGTH_SHORT).show();
        }
    }*/

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
                    AsyncSoapObject.getInstance(Constants.getWSDL(), Constants.NAMESPACE_ALLEM,
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

    private void setActionbar() {
        actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setIcon(R.drawable.ab_icon_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                    | ActionBar.DISPLAY_SHOW_HOME);
        }
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
    }



    @Subscribe
    public void onAsyncTaskResult(AsyncTaskSoapObjectResultEvent event) {

        if (event.getCodeRequest() == Constants.ACTIVITY_LOGIN) {
            setWaitinUI(false);
            if (event.getResult() != null) {
                Log.e("Entro una vez","Al onAsyncTaskResult");
                AllemUser user = SoapObjectParsers.toAllemUser(event.getResult());
                ((VisaCheckoutApp) this.getApplication()).setIdSession(user.idSesion);
                ((VisaCheckoutApp) this.getApplication()).setIdCuenta(user.idCuenta);
                ((VisaCheckoutApp) this.getApplication()).setRawPassword(password.getText().toString());
                String name = user.email.substring(0, user.email.indexOf('@'));
                String domain = user.email.substring(user.email.indexOf('@') + 1, user.email.length()).replace(".", "");
                String channel = name + domain + user.idCuenta;
                String password = user.hashpassword;

                String nombre = user.nombre;
                String apellido = user.apellido;
                //Save in SharedPreferences
                SharedPreferences prefs =
                        getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("apellido", apellido);
                editor.putString("nombre", nombre);
                editor.commit();


                Log.e("Serfar", "nombre " + nombre);
                Log.e("Serfar", " apellido" + apellido);
                idCuenta = user.idCuenta;
                Log.e("Serfar idCuenta onAsync",String.valueOf(idCuenta));

                Constants.saveUser(ctx, user, channel);
                ((VisaCheckoutApp) this.getApplication()).unSetParseChannels();
                ((VisaCheckoutApp) this.getApplication()).parseUser(user.email, channel);

                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(ctx, event.getFaultString(), Toast.LENGTH_LONG).show();
            }
            //getHigherMcard();
        }

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

    private Element[] buildAuthHeader(SoapSerializationEnvelope envelope) {

        Element headers[] = new Element[1];
        headers[0]= new Element().createElement("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Security");
        headers[0].setAttribute(envelope.env, "mustUnderstand", "1");
        Element security=headers[0];

        Element to = new Element().createElement(security.getNamespace(), "UsernameToken");
        to.setAttribute("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "Id", "UsernameToken-2");

        Element action1 = new Element().createElement(security.getNamespace(), "Username");
        action1.addChild(Node.TEXT, Constants.ACTIVITY_UPDATE_USER);
        to.addChild(Node.ELEMENT,action1);

        Element action2 = new Element().createElement(security.getNamespace(), "Password");
        action2.setAttribute(null, "Type", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");
        action2.addChild(Node.TEXT, Constants.SOAP_AUTH_PASS);
        to.addChild(Node.ELEMENT,action2);
        headers[0].addChild(Node.ELEMENT, to);
        return headers;
    }

    //**********************INNER CLASSES**************

    //Llamar al servicio web a una AsyncTask que realice las operaciones en segundo
    // plano utilizando un hilo secundario

    private class TareaWSConsulta extends AsyncTask<String,Integer,Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {

            boolean resul = true;
            //McardCliente[] listaClientes;

            SoapObject request = new SoapObject(Constants.MCARD_NAMESPACE, Constants.MCARD_METHOD);
            request.addProperty("idCuenta", idCuenta);
            SoapSerializationEnvelope envelope =
                    new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.setOutputSoapObject(request);
            /*McardCliente cuentaCliente = new McardCliente();
            cuentaCliente.setIdCuenta(idCuenta);
            PropertyInfo property = new PropertyInfo();
            property.setName(McardCliente.PROPERTY);
            property.setValue(cuentaCliente);
            property.setType(cuentaCliente.getClass());*/

            HttpTransportSE transporte = new HttpTransportSE(Constants.SOAP_URL_MCARD);

            try
            {
                transporte.call(Constants.MCARD_NAMESPACE+Constants.MCARD_METHOD, envelope);

                SoapObject resSoap =(SoapObject)envelope.getResponse();
                envelope.headerOut = buildAuthHeader(envelope);

                SoapObject[] list = new SoapObject [resSoap.getPropertyCount()];
                Log.e("Sergio","Es "+String.valueOf(list.length));
                arrayListMemberships = new ArrayList<>();
                for (int i = 0; i < list.length; i++){
                    arrayListMemberships.add(list[i].getProperty(1).toString());
                    Log.e("Test",arrayListMemberships.get(i));//.toString()
                }


                /*McardCliente cli = new McardCliente();
                cli.idProducto = Integer.parseInt(ic.getProperty(1).toString());*/

                //Log.e("idProdcuto",String.valueOf(cli.idProducto));
                SoapPrimitive resultado_xml =(SoapPrimitive)envelope.getResponse();
                String res = resultado_xml.toString();
                Log.e("Resposn",res);
                //listaClientes = new McardCliente[resSoap.getPropertyCount()];

               /*/*//* for (int i = 0; i < 1; i++)
                {
                    SoapObject ic = (SoapObject)resSoap.getProperty(i);

                    McardCliente cli = new McardCliente();
                    cli.idCuenta = Integer.parseInt(ic.getProperty(0).toString());

                    //listaClientes[i] = cli;
                }*/
            }
            catch (Exception e)
            {
                resul = false;
            }

            return resul;
        }
    }

    }