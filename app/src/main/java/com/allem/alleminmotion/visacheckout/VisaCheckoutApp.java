package com.allem.alleminmotion.visacheckout;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.allem.alleminmotion.visacheckout.utils.Constants;
import com.allem.alleminmotion.visacheckout.utils.KeySaver;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

/**
 * Created by lisachui on 12/1/15.
 */
public class VisaCheckoutApp extends MultiDexApplication {

    private static final String TAG = "VisaCheckoutApp";

    private String idSession=null,channel,urlResto;
    private String rawPassword;
    private int idCuenta;
    private ParseUser currentUser;

    private String urlHotel;

    @Override
    public void onCreate() {
        super.onCreate();
        //SystemClock.sleep(0);   // For running splash screen

        super.onCreate();
        Parse.enableLocalDatastore(getApplicationContext());
        Parse.initialize(this, "YLafvQVsiUgpHPhTBZFuEIEfdnCtzHNV6fwiOWnY", "PpAKINbIw42zMgFuzstKl6IOrrQqRFAxupHqkGdn");
        ParseInstallation.getCurrentInstallation().saveEventually(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) Log.e(TAG, e.toString());
                else {
                    ParseUser.enableAutomaticUser();
                    ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            currentUser = ParseUser.getCurrentUser();
                            if (currentUser != null) {
                                Log.d(TAG, "currentuser: " + currentUser.getObjectId() + " platform:" + Build.VERSION.RELEASE);
                                currentUser.put("platform", "Android");
                                currentUser.put("systemVer", Build.VERSION.RELEASE);
                                currentUser.saveInBackground();
                                ParseInstallation currentInstall = ParseInstallation.getCurrentInstallation();
                                currentInstall.put("user", currentUser);
                                currentInstall.saveInBackground();
                            }
                        }
                    });
                    subscribeGlobal();
                }
            }
        });

        // Setup handler for uncaught exceptions.
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable e) {
                handleUncaughtException(thread, e);
            }
        });
    }

    private void subscribeGlobal() {

        ParsePush.subscribeInBackground(Constants.PUSH_GLOBAL, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d(TAG, "successfully subscribed to the global " + Constants.PUSH_GLOBAL + " channel.");
                } else {
                    Log.e(TAG, "failed to subscribe for push", e);
                }
            }
        });
    }

    public void setParseChannel(String channel) {
        Log.d(TAG, "Suscribiendo al canal " + channel);
        this.channel = channel;
        ParsePush.subscribeInBackground(channel, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d(TAG, "successfully subscribed to " + VisaCheckoutApp.this.channel + " broadcast channel.");
                    KeySaver.saveShare(getApplicationContext(), Constants.USER_PUSH, VisaCheckoutApp.this.channel);
                } else {
                    Log.e(TAG, "failed to subscribe for push", e);
                }
            }
        });
    }



    public void unSetParseChannels(){
        final List<String> subscribedChannels = ParseInstallation.getCurrentInstallation().getList("channels");
        final int channelsNbr=subscribedChannels.size();
        for(int i=0;i<subscribedChannels.size();i++){
            final String channel = subscribedChannels.get(i);
            if (!channel.equals(Constants.PUSH_GLOBAL)){
                ParsePush.unsubscribeInBackground(subscribedChannels.get(i), new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.d(TAG, "successfully unsubscribed to " + channel + " broadcast channel.");
                        } else {
                            Log.e(TAG, "failed to subscribe for push", e);
                        }
                    }
                });
            }
        }


    }
    public String getIdSession() {
        return idSession;
    }

    public void setIdSession(String idSession) {
        this.idSession = idSession;
    }

    public String getRawPassword() {
        return rawPassword;
    }

    public void setRawPassword(String rawPassword) {
        this.rawPassword = rawPassword;
    }

    public int getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(int idCuenta) {
        this.idCuenta = idCuenta;
    }

    public void deleteSesion(){
        this.idCuenta=-1;
        this.idSession=null;
    }

    public void parseUser(String username,String channel){
        final String userchannel=channel;
        setParseChannel(userchannel);
       /* currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            Log.d(TAG,currentUser.toString());
            Log.d(TAG, "Setting user: " + username);
            currentUser.put("email",username);
//            if(mLastLocation!=null) {
//                Log.d(TAG,"ubicación lat: "+mLastLocation.getLatitude()+" - lon: "+mLastLocation.getLongitude());
//                currentUser.put("location", new ParseGeoPoint(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
//            }


            currentUser.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {

                    if(e!=null){
                        Log.e(TAG,"Error saving currrent user ");
                        e.printStackTrace();
                    }
                    Log.d(TAG, "Set Instalation User: " + currentUser.toString());

                }
            });
        }*/
    }

    public String getUrlHotel() {
        return urlHotel;
    }

    public void setUrlHotel(String urlHotel) {
        this.urlHotel = urlHotel;
    }

    public void handleUncaughtException (Thread thread, Throwable e) {
        e.printStackTrace(); // not all Android versions will print the stack trace automatically

        Intent intent = new Intent ();
        intent.setAction("com.allem.alleminmotion.visacheckout.SEND_LOG"); // see step 5.
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // required when starting from Application
        startActivity(intent);

        System.exit(1); // kill off the crashed app
    }

}
