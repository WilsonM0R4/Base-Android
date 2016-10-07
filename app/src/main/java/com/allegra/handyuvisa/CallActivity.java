package com.allegra.handyuvisa;

import android.*;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.allegra.handyuvisa.twilio.BasicPhone;
import java.util.HashMap;
import java.util.Map;

public class CallActivity extends LoadAnimate implements BasicPhone.LoginListener,
        BasicPhone.BasicConnectionListener,BasicPhone.BasicDeviceListener,
        SensorEventListener, LoadAnimate.InflateReadyListener,
        BackFragment.MenuSelectListener {

    //*************GLOBAL ATTRIBUTES**************
    protected String OTC_NUMBER = "+13055605384";
    private static final int CALLING = 1, MY_PERMISSIONS_REQUEST_AUDIO = 4563;
    private static final int CALLIP = 2;
    private static final int HANGIP = 3;
    private static final int STANDBY = 4;
    private static final Handler handler = new Handler();
    private final String TAG="FA_OTC";
    private BasicPhone phone;
    private ActionBar actionBar;
    private Context ctx;
    private boolean near=false;
    private int statusCall=-1;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private float mSensorRange;
    private boolean speakerOff = true;
    private boolean muteOff = true;
    private TextView txtTitle;
    String[] PERMISSIONS = { android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.MODIFY_AUDIO_SETTINGS,
            android.Manifest.permission.READ_PHONE_STATE};

    //*************OVERRIDE METHODS**************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setView(R.layout.fragment_call_in_progress, R.drawable.load__call, R.string.txt_lbl_callwait, this);
        addStatusMessage("One Touch Call");
        ctx = this;
        phone = BasicPhone.getInstance(ctx);

        /*mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if (mSensor!=null) mSensorRange=mSensor.getMaximumRange();
        phone.login("OneTouchCall", true, true);
        //setButton(-1);
        setListeners();*/
        //******Check for Audio permission
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, MY_PERMISSIONS_REQUEST_AUDIO);
        } else {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) setCallSettings();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_AUDIO: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted, yay! Do the camera-related task you need to do.
                    setCallSettings();
                } else {
                    // Permission denied, boo! Disable the functionality that depends on this permission.
                    finish();
                }
                return;
            }
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(mSensor!=null) mSensorManager.registerListener(this, mSensor,SensorManager.SENSOR_DELAY_NORMAL);
        if (phone.handleIncomingIntent(getIntent())) {
            //showIncomingAlert();
            addStatusMessage("Received incoming connection");
            //syncMainButton();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: shutdown phone");
        if (phone != null) {
            if (!speakerOff) {
                // Speaker state seems to be retained in the Twilio library; clear its state before
                // bringing down the phone.
                phone.setSpeakerEnabled();
            }
            if (phone.isConnected()) {
                phone.disconnect();
            } else {
                Log.d(TAG, "phone is not connected");
            }
            phone.setListeners(null, null, null);
            phone = null;
        } else {
            Log.d(TAG, "phone is not initialized");
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void initViews(View root) {
        setActionbar();
    }

    @Override
    public void onCancelLoading() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //if (statusCall!=CALLING&&!near) this.finish();
                if (statusCall!=CALLING) this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onIncomingConnectionDisconnected() {
        addStatusMessage("Pending incoming connection disconnected");
    }


    @Override
    public void onConnectionConnecting() {
        addStatusMessage("trying to connect");
        if (statusCall==STANDBY){
            statusCall=CALLIP;
            setButton(statusCall);
        }
    }

    @Override
    public void onConnectionConnected() {
        addStatusMessage("Connected: current status = " + statusCall);
        if(statusCall==STANDBY||statusCall==CALLIP){
            statusCall=CALLING;
            setButton(statusCall);
        }
    }

    @Override
    public void onConnectionFailedConnecting(Exception error,int errorCode) {
        if (error != null)
            //Connection error: %s
            addStatusMessage(String.format("Connection error: %s:%s", error.getLocalizedMessage(),String.valueOf(errorCode)));
        else
            //Connection error
            addStatusMessage("Connection error");
    }

    @Override
    public void onConnectionDisconnecting() {
        addStatusMessage("Tratando de deconectar");
        if(statusCall==CALLIP||statusCall==CALLING){
            statusCall=HANGIP;
            setButton(statusCall);
        }
    }

    @Override
    public void onConnectionDisconnected() {
        addStatusMessage("Disconnected");
        finish();
    }

    @Override
    public void onConnectionFailed(Exception error, int errorCode) {
        if (error != null)
            //Connection error: %s
            addStatusMessage(String.format("Error de conexión: %s:%s", error.getLocalizedMessage(),String.valueOf(errorCode)));
        else
            //Connection error
            addStatusMessage("Error de conexión");
    }

    @Override
    public void onDeviceStartedListening() {

        addStatusMessage("Device is listening for incoming connections");
        showProgress(false);
        statusCall=STANDBY;

        setButton(statusCall);
    }

    @Override
    public void onDeviceStoppedListening(Exception error,int errorCode) {
        if (error!=null){
            addStatusMessage("Device is no longer listening for incoming connections due to error: " + error.getLocalizedMessage() + ":" + errorCode);
        }else{
            addStatusMessage("Device is no longer listening for incoming connections");
        }
        setButton(-1);
        setStatus(R.string.txt_lbl_disconnect);
        finish();
    }

    @Override
    public void onLoginStarted() {
        addStatusMessage("Waiting for token");

    }

    @Override
    public void onLoginFinished() {
        addStatusMessage("Token Obtained");

        if (!phone.isConnected()) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("To", OTC_NUMBER);
            phone.connect(params);
            statusCall = CALLIP;
            setButton(statusCall);
        } else {
            phone.disconnect();
        }

    }

    @Override
    public void onLoginError(Exception error) {
        if(error!=null) addStatusMessage("Error Obtaining token " + error.getLocalizedMessage() + ":" + error.hashCode());
        else addStatusMessage("Login error");
        showProgress(false);
        setStatus(R.string.txt_lbl_disconnect);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        addStatusMessage("event.values[0]=" + event.values[0] + " of " + String.valueOf(mSensorRange));
        if (event.values[0] < mSensorRange) {
            near=true;
            addStatusMessage("near");
        } else {
            near=false;
            addStatusMessage("far");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void getStartActivity(Intent intent) {
        onEndCall(null);
        super.getStartActivity(intent);
    }

    //*******************PROPER METHODS**************

    private void setCallSettings(){

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if (mSensor!=null) mSensorRange=mSensor.getMaximumRange();
        phone.login("OneTouchCall", true, true);
        //setButton(-1);
        setListeners();
    }

    private void setActionbar() {
        actionBar = getActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

    }

    private void setListeners() {
        phone.setListeners(this, this, this);
    }

    private void setButton(final int callstatus) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (callstatus) {
                    case STANDBY:
                        showProgress(true);
                        break;
                    case CALLIP:
                        showProgress(true);
                        break;
                    case CALLING:
                        showProgress(true);
                        animate();
                        break;
                    default:
                        showProgress(false);
                }
            }
        });

    }

    protected void onPause() {
        super.onPause();
        if(mSensor!=null) mSensorManager.unregisterListener(this);
    }

    /* The BasicPhone Listeners*/

    private void addStatusMessage(final String message)
    {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, message);
            }
        });
    }

    public void onEndCall(View view) {
        finish();
        overridePendingTransition(R.animator.back_slide_in, R.animator.front_slide_out);
    }

    public void onToggleSpeaker(View view) {

        if (speakerOff) {
            speakerOff = false;
            ((ImageButton)view).setImageResource(R.drawable.icon_speaker2);
        } else {
            speakerOff = true;
            ((ImageButton)view).setImageResource(R.drawable.icon_speaker);
        }

        phone.setSpeakerEnabled();
    }

    public void onToggleMute(View view) {
        if (muteOff) {
            muteOff = false;
            ((ImageButton)view).setImageResource(R.drawable.icon_mute2);
            phone.setCallMuted(true);
        } else {
            muteOff = true;
            ((ImageButton)view).setImageResource(R.drawable.icon_mute);
            phone.setCallMuted(false);
        }

    }

    public void onHome(View view) {
        onEndCall(null);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if ( context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
