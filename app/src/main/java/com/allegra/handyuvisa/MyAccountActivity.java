package com.allegra.handyuvisa;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.allegra.handyuvisa.async.AsyncTaskSoapPrimitiveResultEvent;
import com.allegra.handyuvisa.async.MyBus;
import com.allegra.handyuvisa.utils.Constants;
import com.allegra.handyuvisa.utils.Util;
import com.allegra.handyuvisa.async.AsyncSoapPrimitive;
import com.allegra.handyuvisa.utils.KeySaver;
import com.squareup.otto.Subscribe;
import com.urbanairship.UAirship;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class MyAccountActivity extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {

    private final String TAG="FAPerfilInformation";
    private ActionBar actionBar;
    private Context ctx;
    private ArrayList<NameValuePair> postValues;
    private TextView tv_fullname,tv_email, tv_phone;
    private Button btn_logout;
    private ProgressBar pb_cerrarsesion;
    private ImageButton editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ctx=this;
        MyBus.getInstance().register(this);
        super.setView(R.layout.fragment_my_information, this);
        checkLogin();
        postValues = new ArrayList<>();
    }

    @Override protected void onDestroy() {
        MyBus.getInstance().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
            setResult(resultCode);
            finish();
        } else {
            finish();
        }
    }

    @Override
    public void initViews(View root) {
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);  //permits the action bar overlay over the screen
        setActionbar();
        tv_fullname = (TextView) root.findViewById(R.id.tv_fullname);
        tv_email = (TextView) root.findViewById(R.id.tv_email);
        tv_phone = (TextView)root.findViewById(R.id.tv_phone);
        btn_logout = (Button) root.findViewById(R.id.btn_logout);
        tv_email.setText(KeySaver.getStringSavedShare(ctx, Constants.KEY_EMAIL));
        tv_phone.setText(KeySaver.getStringSavedShare(ctx, Constants.KEY_MOBILE_NUMBER));
        tv_fullname.setText(KeySaver.getStringSavedShare(ctx,Constants.KEY_GREET)+" "+
                KeySaver.getStringSavedShare(ctx,Constants.KEY_NAME)+" "+
                KeySaver.getStringSavedShare(ctx, Constants.KEY_SURNAME));
        pb_cerrarsesion = (ProgressBar)root.findViewById(R.id.pb_cerrarsesion);
        editButton = (ImageButton) root.findViewById(R.id.ib_edit_profile);
        setListeners();
    }

    private void setListeners() {
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAccountActivity.this,EditProfileActivity.class);
                startActivity(intent);
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.hasInternetConnectivity(ctx)){
                    pb_cerrarsesion.setVisibility(View.VISIBLE);
                    postValues.add(new BasicNameValuePair("idSesion", (((VisaCheckoutApp)MyAccountActivity.this.getApplication()).getIdSession())));
                    String wsdl;
                    if (BuildConfig.DEBUG || Constants.TESTING) {
                        wsdl = Constants.URL_ALLEM_WSDL_TEST;
                    }else{
                        wsdl = Constants.URL_ALLEM_WSDL_PROD;
                    }
                    AsyncSoapPrimitive.getInstance(wsdl, Constants.NAMESPACE_ALLEM,
                            Constants.METHOD_CERRAR_SESION, postValues, Constants.ACTIVITY_PROFILE_INFORMATION).execute();
                //Remove UrbanAirship Notifications
                    UAirship.shared().getPushManager().editTags()
                            .addTag("")
                            //.removeTag("some_other_tag")
                            .apply();

                }else{
                    Toast.makeText(ctx, R.string.err_no_internet, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setActionbar() {

        actionBar = getActionBar();
        if(actionBar!=null){
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

        if(event.getCodeRequest()==Constants.ACTIVITY_PROFILE_INFORMATION){
            pb_cerrarsesion.setVisibility(View.GONE);
            if (event.getResult()!=null){
                if(Boolean.valueOf(event.getResult().toString())){
                    Constants.deleteUser(ctx);
                    ((VisaCheckoutApp)this.getApplication()).deleteSesion();
                    //Remove Notifications
                    UAirship.shared().getPushManager().editTags()
                            //.addTag("some_tag")
                            .removeTag("some_other_tag")
                            .apply();

                    //UAirship.shared().getNamedUser().setId(null);
                    //((VisaCheckoutApp)this.getApplication()).unSetParseChannels();
                }else{
                    Log.d(TAG, "No se pudo desloguear o ya se encuentra deslogueado");
                    ((VisaCheckoutApp)this.getApplication()).deleteSesion();
                }
            }else{
                Log.d(TAG, event.getFaultString());
                Constants.deleteUser(ctx);
                ((VisaCheckoutApp)this.getApplication()).deleteSesion();
            }
            setResult(RESULT_OK);
            finish();
            overridePendingTransition(R.animator.back_slide_in, R.animator.front_slide_out);
        }
    }

    public void onMenu(View view) {
        animate();
    }

    private void sendLogoutIntent(boolean cleanupParse) {
        if (cleanupParse) {
           // ((VisaCheckoutApp)this.getApplication()).unSetParseChannels();
        }
        Intent resultIntent = new Intent(this, MainActivity.class);
        startActivity(resultIntent);

    }

    private void checkLogin() {
        if(((VisaCheckoutApp)this.getApplication()).getIdSession()==null){
            Intent i =new Intent(ctx,LoginActivity.class);
            this.startActivityForResult(i, Constants.ACTIVITY_LOGIN);
        }
    }

}
