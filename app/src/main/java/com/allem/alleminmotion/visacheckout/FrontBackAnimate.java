package com.allem.alleminmotion.visacheckout;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by lchui on 1/10/16.
 */
//This class is one that consume a lot of memory, because the way is inflated the menu.
//Example:   setContentView(R.layout.activity_front_back);
public class FrontBackAnimate extends FragmentActivity implements BackFragment.MenuSelectListener {

    public interface InflateReadyListener {
        void initViews(View root);
    }

    private static final String TAG = "FrontBackAnimate";
    private static final String FRAGMENT_FRONT = "FRAGMENT_FRONT";

    private FrontFragment frontFragment;
    private BackFragment backFragment;
    private static FrontBackAnimate.InflateReadyListener inflateListener;
    protected static int frontLayoutResId = R.layout.fragment_front;
    private int state = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_back);

        frontFragment = new FrontFragment();
        getFragmentManager().beginTransaction()
                            .add(R.id.container_front_back, frontFragment, FRAGMENT_FRONT)
                            .commit();
        Log.d(TAG,FRAGMENT_FRONT);

        backFragment = (BackFragment) getFragmentManager().findFragmentById( R.id.fragment_bottom );
        backFragment.menulistener = this;
        //Log.d(TAG,FRAGMENT_FRONT);
    }

    public static class FrontFragment extends Fragment {

        public FrontFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View root = inflater.inflate(frontLayoutResId, container, false);
            if (inflateListener != null) {
                inflateListener.initViews(root);
            }
            return root;
        }
    }

    protected void setView(int resId, FrontBackAnimate.InflateReadyListener listener) {
        frontLayoutResId = resId;
        inflateListener = listener;
    }

    protected int getFrontLayoutResId() {
        return R.layout.fragment_front;
    }

    protected void animate() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int resId = R.animator.front_open;
        if (width > 800) {
            resId = R.animator.front_open_xlarge;
        }

        getFragmentManager().beginTransaction().hide(frontFragment).hide(backFragment).commit();
        if (state == 0) {//Button menu pressed, BackFragment is hidden
            state = 1;
            showStatusBar(false);
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(resId, 0)
                    .show(frontFragment)
                    .setCustomAnimations(R.animator.back_exposed, 0)
                    .show(backFragment)
                    .commit();
            Log.d("Sergio", "0");
        } else {
            state = 0;
            showStatusBar(true);
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.animator.front_close, 0)
                    .show(frontFragment)
                    .setCustomAnimations(R.animator.back_hidden, 0)
                    .show(backFragment)
                    .commit();
            Log.d("Sergio", "1");
        }

    }

    public void animateBetter(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int resId = R.animator.front_open;
        if (width > 800) {
            resId = R.animator.front_open_xlarge;
        }
        //

        if (state == 1) {
            getFragmentManager().beginTransaction().hide(frontFragment).hide(backFragment).commit();
            //state = 1;
            Log.d("Sergio", "Entra al state == 1");
            state = 0;
            showStatusBar(true);
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.animator.front_close, 0)
                    .show(frontFragment)
                    .setCustomAnimations(R.animator.back_hidden, 0)
                    .show(backFragment)
                    .commit();
        }

    }

    public void onHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.animator.front_slide_in, R.animator.back_slide_out);
        finish(); // call this to finish the current activity
    }

    protected void setCustomActionBar(View view,boolean backOption, String activityTitle){
        TextView title = (TextView) view.findViewById(R.id.tv_title_secc);
        ImageButton backButton = (ImageButton) view.findViewById(R.id.btn_back);
        if (backOption) backButton.setVisibility(View.VISIBLE); else backButton.setVisibility(View.INVISIBLE);
        title.setText(activityTitle);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void getStartActivity(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.animator.front_slide_in, R.animator.back_slide_out);
        finish();
    }

    public void onCloseMenu(View view) {
        //animate();
        animateBetter();
    }

    public void showStatusBar(boolean toShow) {
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = (toShow) ? View.SYSTEM_UI_FLAG_VISIBLE : View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

    }

    public void onUp(View view) {
        super.onBackPressed();
    }

}
