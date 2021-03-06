package com.allegra.handyuvisa;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.allegra.handyuvisa.async.AsyncSoapObject;
import com.allegra.handyuvisa.models.CuentaClienteRecover;
import com.allegra.handyuvisa.utils.Constants;
import com.allegra.handyuvisa.utils.CustomizedTextView;
import com.allegra.handyuvisa.utils.Util;

import org.apache.http.NameValuePair;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;

/**
 * Created by jsandoval on 14/06/16.
 */
public class LoginForgotConfirmation extends Fragment {

    private final String TAG = "LoginForgotConfirmation";
    private ImageView close;
    //private Button login, email;
    private TextView emailtext;
    private CustomizedTextView send_again, login;
    private LayoutInflater inflater;
    private View rootview;
    private ViewGroup container;
    private int recover;
    private ArrayList<NameValuePair> postValues;
    private Context ctx;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.d(TAG, "Enter LoginForgotActivity");
        postValues = new ArrayList<>();
        ctx = getActivity();
        //super.setView(R.layout.fragment_recoverpass_conf, this);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.fragment_recoverpass_conf, container, false);
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
    }


    public void initViews(final View root) {
        recover = Constants.ACTIVITY_LOGIN_RECOVER;
        emailtext = (TextView)root.findViewById(R.id.email_recovery);

        /*close = (ImageView)root.findViewById(R.id.close_btn);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHome(v);
            }
        });*/
        login = (CustomizedTextView)root.findViewById(R.id.to_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentMain) getParentFragment()).restartStack();
                ((FragmentMain) getParentFragment()).replaceLayout(new LoginActivity(), true);
            }
        });
     /*   email = (Button)root.findViewById(R.id.go_to_email);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:"));
                Intent chooser = Intent.createChooser(email,"Abrir");
                startActivity(chooser);
            }
        });*/


        String str = getArguments().getString("mail");
        emailtext.setText(str);

        send_again = (CustomizedTextView) root.findViewById(R.id.send_again);
        send_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInfo();
                Toast.makeText(ctx, getString( R.string.resend_mail), Toast.LENGTH_SHORT).show();
            }
        });

        ((FragmentMain) getParentFragment()).configToolbar(false, Constants.TYPE_ICON_CANCEL, getString(R.string.forgot_password));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /*@Override
    public void onBackPressed() {
        Intent intent = new Intent(ctx,LoginActivity.class);
        startActivity(intent);
    }*/

    private void sendInfo() {

        CuentaClienteRecover cuentaClienteRecover = new CuentaClienteRecover();
        cuentaClienteRecover.setEmail(emailtext.getText().toString());

        PropertyInfo property = new PropertyInfo();
        property.setName(CuentaClienteRecover.PROPERTY);
        property.setValue(cuentaClienteRecover);
        property.setType(cuentaClienteRecover.getClass());

        if (Util.hasInternetConnectivity(ctx)) {
            AsyncSoapObject.getInstance(Constants.getWSDL(), Constants.NAMESPACE_ALLEM,
                    Constants.METHOD_RECUPERAR_PASSWORD, property, Constants.ACTIVITY_LOGIN_RECOVER).execute();
        } else {
            Toast.makeText(ctx, getString(R.string.err_no_internet), Toast.LENGTH_SHORT).show();
        }
    }

}
