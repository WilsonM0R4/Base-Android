package com.allem.alleminmotion.visacheckout;

import  android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.allem.alleminmotion.visacheckout.async.AsyncSoapPrimitive;
import com.allem.alleminmotion.visacheckout.utils.Constants;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by jsandoval on 14/06/16.
 */
public class LoginForgotConfirmation extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {

    private final String TAG = "LoginForgotConfirmation";
    private ImageView close;
    private Button login, email;
    private TextView emailtext;
    private Button send_again;
    private int recover;
    private ArrayList<NameValuePair> postValues;
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Enter LoginForgotActivity");
        postValues = new ArrayList<>();
        ctx = this;
        super.setView(R.layout.fragment_recoverpass_conf, this);
    }

    @Override
    public void initViews(final View root) {
        recover = Constants.ACTIVITY_LOGIN_RECOVER;
        close = (ImageView)root.findViewById(R.id.close_btn);
        emailtext = (TextView)root.findViewById(R.id.email_recovery);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx,LoginActivity.class);
                startActivity(intent);
            }
        });
        login = (Button)root.findViewById(R.id.to_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx,LoginActivity.class);
                startActivity(intent);
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

        Intent i = getIntent();
        String str = i.getStringExtra("mail");
        emailtext.setText(str);

        send_again = (Button)root.findViewById(R.id.send_again);
        send_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postValues.add(new BasicNameValuePair(Constants.KEY_EMAIL, emailtext.getText().toString()));
                AsyncSoapPrimitive.getInstance(Constants.getWSDL(), Constants.NAMESPACE_ALLEM,
                        Constants.METHOD_RECUPERAR_PASSWORD, postValues, recover).execute();
                Toast.makeText(ctx, getString( R.string.resend_mail), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ctx,LoginActivity.class);
        startActivity(intent);
    }

}
