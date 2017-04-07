package com.allegra.handyuvisa;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.allegra.handyuvisa.async.AsyncSoapPrimitive;
import com.allegra.handyuvisa.async.AsyncTaskSoapPrimitiveResultEvent;
import com.allegra.handyuvisa.async.MyBus;
import com.allegra.handyuvisa.utils.Constants;
import com.allegra.handyuvisa.utils.KeySaver;
import com.allegra.handyuvisa.utils.OnBackCallback;
import com.allegra.handyuvisa.utils.Util;
import com.squareup.otto.Subscribe;
import com.urbanairship.UAirship;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class MyAccountActivity extends Fragment {

    private final String TAG="FAPerfilInformation";
    private ActionBar actionBar;
    private Context ctx;
    private ArrayList<NameValuePair> postValues;
    private TextView tv_fullname,tv_email, tv_phone;
    private Button btn_logout;
    private ProgressBar pb_cerrarsesion;
    private ImageButton editButton, backButton, menuButton;
    OnBackCallback onBackCallback;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ctx = getActivity();
        MyBus.getInstance().register(this);

        //super.setView(R.layout.fragment_my_information, this);

        ((MainActivity) getActivity()).statusBarVisibility(false);

        checkLogin();
        postValues = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.fragment_my_information, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
    }

    @Override
    public void onDestroy() {
        MyBus.getInstance().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == MainActivity.RESULT_OK) {
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
            setResult(resultCode);
            finish();
        } else {
            finish();
        }
    }*/

    //@Override
    public void initViews(View root) {
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);  //permits the action bar overlay over the screen
        //setActionbar();

        onBackCallback = ((FragmentMain) getParentFragment());

        /*menuButton = (ImageButton)root.findViewById(R.id.menu_image);
        backButton = (ImageButton)root.findViewById(R.id.imageButton2);*/

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

        ((FragmentMain) getParentFragment()).configToolbar(false, Constants.TYPE_BACK_MENU, getString(R.string.title_my_profile));
    }

    private void setListeners() {

        /*menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).animate();
            }
        });*/

        /*backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceLayout(new MyAccountMenuActivity(), true);
            }
        });*/

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((FragmentMain) getParentFragment()).replaceLayout(new EditProfileActivity(), false);
                /*Intent intent = new Intent(MyAccountActivity.this,EditProfileActivity.class);
                startActivity(intent);*/
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.hasInternetConnectivity(ctx)){
                    pb_cerrarsesion.setVisibility(View.VISIBLE);
                    postValues.add(new BasicNameValuePair("idSesion",
                            (((VisaCheckoutApp)MyAccountActivity.this.getActivity().
                                    getApplication()).getIdSession())));
                    String wsdl;
                    if (BuildConfig.DEBUG || Constants.TESTING) {
                        wsdl = Constants.URL_ALLEM_WSDL_TEST;
                    }else{
                        wsdl = Constants.URL_ALLEM_WSDL_PROD;
                    }
                    AsyncSoapPrimitive.getInstance(wsdl, Constants.NAMESPACE_ALLEM,
                            Constants.METHOD_CERRAR_SESION, postValues,
                            Constants.ACTIVITY_PROFILE_INFORMATION).execute();
                    //Remove UrbanAirship Notifications
                    UAirship.shared().getPushManager()
                            .setUserNotificationsEnabled(false);

                    //*******Clear SharedPReferences
/*
                    SharedPreferences prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.clear();
*/

                }else{
                    Toast.makeText(ctx, R.string.err_no_internet, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*private void setActionbar() {

        actionBar = getActivity().getActionBar();
        if(actionBar!=null){
            actionBar.setIcon(R.drawable.ab_icon_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                    | ActionBar.DISPLAY_SHOW_HOME);
        }

    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
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
                    ((VisaCheckoutApp)getActivity().getApplication()).deleteSesion();
                    //Remove Notifications
                    UAirship.shared().getPushManager().editTags()
                            //.addTag("some_tag")
                            .removeTag("some_other_tag")
                            .apply();

                    //UAirship.shared().getNamedUser().setId(null);
                    //((VisaCheckoutApp)this.getApplication()).unSetParseChannels();
                }else{
                    Log.d(TAG, "No se pudo desloguear o ya se encuentra deslogueado");
                    ((VisaCheckoutApp)getActivity().getApplication()).deleteSesion();
                }
            }else{
                Log.d(TAG, event.getFaultString());
                Constants.deleteUser(ctx);
                ((VisaCheckoutApp)getActivity().getApplication()).deleteSesion();
            }
            getActivity().setResult(MainActivity.RESULT_OK);
            //finish();
            //overridePendingTransition(R.animator.back_slide_in, R.animator.front_slide_out);
        }

        //onBackCallback.onBack();
        ((FragmentMain) getParentFragment()).restartStack();
        ((FragmentMain) getParentFragment()).replaceLayout(new FrontFragment(), false);
    }

    public void onMenu(View view) {
        ((MainActivity) getActivity()).animate();
    }

    /*private void sendLogoutIntent(boolean cleanupParse) {
        if (cleanupParse) {
           // ((VisaCheckoutApp)this.getApplication()).unSetParseChannels();
        }
        Intent resultIntent = new Intent(this, MainActivity.class);
        startActivity(resultIntent);

    }*/

    private void checkLogin() {
        if(((VisaCheckoutApp) getActivity().getApplication()).getIdSession()==null){
            ((FragmentMain) getParentFragment()).replaceLayout(new LoginActivity(), false);
            /*Intent i =new Intent(ctx,LoginActivity.class);
            this.startActivityForResult(i, Constants.ACTIVITY_LOGIN);*/
        }
    }

}
