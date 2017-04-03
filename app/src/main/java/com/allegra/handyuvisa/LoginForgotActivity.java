package com.allegra.handyuvisa;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.allegra.handyuvisa.async.AsyncTaskSoapPrimitiveResultEvent;
import com.allegra.handyuvisa.models.CuentaClienteRecover;
import com.allegra.handyuvisa.utils.Constants;
import com.allegra.handyuvisa.utils.CustomizedTextView;
import com.allegra.handyuvisa.utils.Util;
import com.allegra.handyuvisa.async.AsyncSoapObject;

import org.apache.http.NameValuePair;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jsandoval on 14/06/16.
 */
public class LoginForgotActivity extends Fragment{


    private final String TAG = "LoginForgotActivity";
    private EditText email_txt;
    private Context ctxx;
    private CustomizedTextView send;
    private ArrayList<NameValuePair> postValues;
    private int recover;
    private String recoverstr;
    AsyncTaskSoapPrimitiveResultEvent event;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ctxx = getActivity();
        super.onCreate(savedInstanceState);
        postValues = new ArrayList<>();
        Log.d(TAG, "Enter LoginForgotActivity");
        //super.setView(R.layout.fragment_recover_password, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.fragment_recover_password, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
    }

    public void initViews(final View root) {
        send = (CustomizedTextView)root.findViewById(R.id.button_send);
        email_txt = (EditText)root.findViewById(R.id.email_text);
        recover = Constants.ACTIVITY_LOGIN_RECOVER;
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEmailValid(email_txt.getText().toString())){
                    Toast.makeText(ctxx, getString( R.string.err_novalid_mail), Toast.LENGTH_SHORT).show();
                }else {
                    sendInfo();
                    Boolean b1=true;
                    b1.equals(recover);
                    if(b1){
                        Bundle bundle = new Bundle();
                        bundle.putString("mail", email_txt.getText().toString());

                        LoginForgotConfirmation loginForgotConfirmation = new LoginForgotConfirmation();
                        loginForgotConfirmation.setArguments(bundle);
                        ((FragmentMain) getParentFragment()).replaceLayout(loginForgotConfirmation, false);

                    }else{
                        sendInfo();
                    }
                }
            }
        });

        ((FragmentMain) getParentFragment()).configToolbar(false, Constants.TYPE_BACK, getString(R.string.forgot_password));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    private void sendInfo() {

        CuentaClienteRecover cuentaClienteRecover = new CuentaClienteRecover();
        cuentaClienteRecover.setEmail(email_txt.getText().toString());

        PropertyInfo property = new PropertyInfo();
        property.setName(CuentaClienteRecover.PROPERTY);
        property.setValue(cuentaClienteRecover);
        property.setType(cuentaClienteRecover.getClass());

        if (Util.hasInternetConnectivity(ctxx)) {
            AsyncSoapObject.getInstance(Constants.getWSDL(), Constants.NAMESPACE_ALLEM,
                    Constants.METHOD_RECUPERAR_PASSWORD, property, Constants.ACTIVITY_LOGIN_RECOVER).execute();
        } else {
            Toast.makeText(ctxx, getString(R.string.err_no_internet), Toast.LENGTH_SHORT).show();
        }
    }

}