package com.allem.alleminmotion.visacheckout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.allem.alleminmotion.visacheckout.utils.Util;

public class SuccessfulRegisterActivity extends Activity implements BackFragment.MenuSelectListener{

    private static final String TAG = "MainActivity";
    private FrontFragment frontFragment;
    private BackFragment backFragment;
    private int state = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.successful_register);
        getWindow().setBackgroundDrawable(null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            //int uiOptions = (toShow) ? View.SYSTEM_UI_FLAG_VISIBLE : View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        frontFragment = (FrontFragment) getFragmentManager().findFragmentById(R.id.fragment_top);
        backFragment = (BackFragment) getFragmentManager().findFragmentById( R.id.fragment_bottom );
        backFragment.menulistener = this;
        state = 0;
    }

    @Override
    public void getStartActivity(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.animator.front_slide_in, R.animator.back_slide_out);
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

    //************PROPER METHODS************
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

        Log.d("Width",width+"");
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
}
