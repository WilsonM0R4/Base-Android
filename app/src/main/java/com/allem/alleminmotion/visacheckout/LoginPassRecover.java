package com.allem.alleminmotion.visacheckout;

/**
 * Created by victor on 19/02/15.
 * com.allem.allemevent.fragactiv
 */

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.allem.alleminmotion.visacheckout.async.AsyncSoapPrimitive;
import com.allem.alleminmotion.visacheckout.async.AsyncTaskSoapPrimitiveResultEvent;
import com.allem.alleminmotion.visacheckout.async.MyBus;
import com.allem.alleminmotion.visacheckout.utils.Constants;
import com.allem.alleminmotion.visacheckout.utils.Util;
import com.squareup.otto.Subscribe;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class LoginPassRecover extends Activity {
    private final String TAG = "LoginPassRecover";
    private ActionBar actionBar;
    private Context ctx;
    private EditText et_email,et_prodnumber;
    private Button btn_recover;
    private ArrayList<NameValuePair> postValues;
    private ProgressBar pb_recover;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "FASay Activity Creating");
        ctx = this;
        MyBus.getInstance().register(this);
        postValues = new ArrayList<>();
        setView();
        setListeners();
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

    private void setView() {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);  //permits the action bar overlay over the screen
        setContentView(R.layout.activity_login_recover);
        setActionbar();
        et_email = (EditText) findViewById(R.id.et_email);
        et_prodnumber = (EditText) findViewById(R.id.et_prodnumber);
        btn_recover = (Button) findViewById(R.id.btn_recover);
        btn_recover.setEnabled(false);
        pb_recover = (ProgressBar)findViewById(R.id.pb_recover);
        pb_recover.setVisibility(View.GONE);

        /*Activity que se lanza for result, se intenta loguear o se lanza for result
        * la activity para crear nuevo usuario, si retorna RESULT_OK esta a su vez
        * retorna un RESULT_OK a su predecesora, sino queda a la espera que el usuario se loguee
        * o retorna un RESULT_CANCEL
        */

    }

    private void setListeners() {
        et_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkFields();
            }
        });

        et_prodnumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkFields();
            }
        });

        btn_recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.hasInternetConnectivity(ctx)) {
                    setWaitinUI(true);
                    if (!postValues.isEmpty()){
                        postValues.clear();
                    }
                    postValues.add(new BasicNameValuePair(Constants.KEY_EMAIL, et_email.getText().toString()));
                    if (et_prodnumber.getText().toString().isEmpty()) {
                        postValues.add(new BasicNameValuePair(Constants.KEY_PRODUCT_NUMBER, "0000"));
                    } else {
                        postValues.add(new BasicNameValuePair(Constants.KEY_PRODUCT_NUMBER, et_prodnumber.getText().toString()));
                    }
                    AsyncSoapPrimitive.getInstance(Constants.getWSDL(), Constants.NAMESPACE_ALLEM,
                            Constants.METHOD_ENVIAR_PASSWORD, postValues, Constants.ACTIVITY_LOGIN_RECOVER).execute();
                } else {
                    Toast.makeText(ctx, "No tiene conexión a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkFields() {
            btn_recover.setEnabled(android.util.Patterns.EMAIL_ADDRESS.matcher(et_email.getText().toString()).matches());
    }

    private void setWaitinUI(boolean b) {
        if (b) pb_recover.setVisibility(View.VISIBLE);
        else pb_recover.setVisibility(View.GONE);
        btn_recover.setEnabled(!b);
        et_email.setEnabled(!b);
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

    @Subscribe
    public void onAsyncTaskResult(AsyncTaskSoapPrimitiveResultEvent event) {
        if(event.getCodeRequest()==Constants.ACTIVITY_LOGIN_RECOVER){
            setWaitinUI(false);
            if(event.getResult()!=null){
                if(Boolean.valueOf(event.getResult().toString())){
                    Toast.makeText(ctx, "Se envió la nueva contraseña a la cuenta de correo", Toast.LENGTH_LONG).show();
                    LoginPassRecover.this.finish();
                }
                Log.d(TAG, event.getResult().toString());
            }else{
                //Toast.makeText()
                Log.e(TAG, event.getFaultString());
                Toast.makeText(ctx, event.getFaultString(), Toast.LENGTH_LONG).show();
            }
        }
    }

}