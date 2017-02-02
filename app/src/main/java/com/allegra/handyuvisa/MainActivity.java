package com.allegra.handyuvisa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.allegra.handyuvisa.utils.Constants;
import com.allegra.handyuvisa.utils.CustomizedTextView;
import com.allegra.handyuvisa.utils.Util;
import com.appsflyer.AppsFlyerLib;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


public class MainActivity extends FragmentActivity implements com.allegra.handyuvisa.BackFragment.MenuSelectListener {

    private static final String TAG = "MainActivity";
    private FrontFragment frontFragment;
    private com.allegra.handyuvisa.BackFragment backFragment;
    private boolean isLogin;
    private boolean tutorial=true;
    private CustomizedTextView login, register;
    private int state = 0;   // 0 - front open + back exposed;
    // 1 - front close + back hidden;

    //OVERRIDE METHODS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setBackgroundDrawable(null);

        /* APPSFLYER */
        AppsFlyerLib.getInstance().startTracking(this.getApplication(),"55GLA2WaeHkqjapkFYTmZY");//AppFlyer Dev Key jsandoval@iatai.com
        AppsFlyerLib.getInstance().setCollectIMEI(true);//Get de Imei of the Phone
        AppsFlyerLib.getInstance().setCollectAndroidID(true);//Get the Android ID of the OS Phone.
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        /* APPSFLYER */

        //int  what  = Build.VERSION.SDK_INT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            //int uiOptions = (toShow) ? View.SYSTEM_UI_FLAG_VISIBLE : View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
            frontFragment = (FrontFragment) getFragmentManager().findFragmentById(R.id.fragment_top);
            backFragment = (com.allegra.handyuvisa.BackFragment) getFragmentManager().findFragmentById(R.id.fragment_bottom);
            backFragment.menulistener = this;
            state = 0;

            if (!isAuthenticated()) {
                login = (CustomizedTextView) findViewById(R.id.login);
                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivityForResult(intent, Constants.ACTIVITY_LOGIN);
                    }
                });

                register = (CustomizedTextView) findViewById(R.id.register);
                register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, com.allegra.handyuvisa.LoginNewUser.class);
                        startActivity(intent);
                    }
                });
            }

    }

    @Override
    protected void onResume() {
        super.onResume();
        View guest = findViewById(R.id.guest_controls_group);
        View user = findViewById(R.id.login_controls_group);
        if (Util.isAuthenticated(this) && guest != null ||
                !Util.isAuthenticated(this) && user != null) {
            finish();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            state = 1;
            animate();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            finish();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }


    //Interface MenuSelectListener's OVERRIDE METHODS
    @Override
    public void getStartActivity(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.animator.front_slide_in, R.animator.back_slide_out);
        state = 1;
    }

    //PROPER METHODS

    public void ClickMe(View view) {
        animate();
    }

    public void onSkipIntro(View view) {
        animate();
    }

    private boolean isAuthenticated() {
        return (((com.allegra.handyuvisa.VisaCheckoutApp)getApplication()).getIdSession() != null);
    }

    private void animate() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        int dens=dm.densityDpi;
        double wi=(double)width/(double)dens;
        double hi=(double)height/(double)dens;
        double x = Math.pow(wi,2);
        double y = Math.pow(hi, 2);
        //double screenInches = Math.sqrt(x + y);
        //Toast.makeText(this,  "w: " + width + " h: " + height, Toast.LENGTH_SHORT).show();

        //Log.d("Width",width+"");
        int resId = R.animator.front_open;
        if (width > 800) {
            resId = R.animator.front_open_xlarge;
        }

        getFragmentManager().beginTransaction().hide(frontFragment).hide(backFragment).commit();
        if (state == 0) {//Open
            state = 1;
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(resId, 0)
                    .show(frontFragment)
                    .setCustomAnimations(R.animator.back_exposed, 0)
                    .show(backFragment)
                    .commit();
        } else {//Close
            state = 0;
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.animator.front_close, 0)
                    .show(frontFragment)
                    .setCustomAnimations(R.animator.back_hidden, 0)
                    .show(backFragment)
                    .commit();
        }

    }

    public void onHome(View view) {
        animate();
    }


}
