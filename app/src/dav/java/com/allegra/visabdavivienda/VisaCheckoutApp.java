package com.allegra.handyuvisa;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.allegra.handyuvisa.utils.Constants;
import com.allem.onepocket.utils.OPKLibraryConfig;
import com.urbanairship.AirshipConfigOptions;
import com.urbanairship.UAirship;
import com.urbanairship.push.notifications.DefaultNotificationFactory;

/**
 * Created by lisachui on 12/1/15.
 */
public class VisaCheckoutApp extends MultiDexApplication {

    private static final String TAG = "VisaCheckoutApp";
    public Context context = this;
    private String idSession=null,channel,urlResto;
    private String rawPassword;
    private int idCuenta;
    private String urlHotel;
    private String path = "android.resource://com.allegra.visabdavivienda/raw/allegra_sound";


    @Override
    public void onCreate() {
        super.onCreate();

        AirshipConfigOptions options = new AirshipConfigOptions.Builder()
                .setDevelopmentAppKey("KYQyaYDmTniGid2KzD4ItA")
                .setDevelopmentAppSecret("kxsqJmKtT7G_rg62BfdNtA")
                .setProductionAppKey("jTWvMHv6QdSvYvZNPRTb0Q")
                .setProductionAppSecret("AeE9Ic7mS8WwI-LlbIkr-A")
                .setInProduction(!BuildConfig.DEBUG)
                .setGcmSender("624140577621")// FCM/GCM sender ID
                .build();

        UAirship.takeOff(this,options, new UAirship.OnReadyCallback() {

            @Override
            public void onAirshipReady(UAirship airship) {

                // Log.d("ESTOY LISTO", "RECIBIDO");
                DefaultNotificationFactory factory = (DefaultNotificationFactory)
                        UAirship.shared().getPushManager().getNotificationFactory();
                factory.setSound(Uri.parse(path));
                airship.getPushManager().setUserNotificationsEnabled(true);
                UAirship.shared().getNamedUser().setId(null);
            }


            public AirshipConfigOptions createAirshipConfigOptions(@NonNull Context context) {

                AirshipConfigOptions options = new AirshipConfigOptions.Builder()
                        .setDevelopmentAppKey("KYQyaYDmTniGid2KzD4ItA")
                        .setDevelopmentAppSecret("kxsqJmKtT7G_rg62BfdNtA")
                        .setProductionAppKey("jTWvMHv6QdSvYvZNPRTb0Q")
                        .setProductionAppSecret("AeE9Ic7mS8WwI-LlbIkr-A")
                        .setInProduction(!BuildConfig.DEBUG)
                        .setGcmSender("624140577621")// FCM/GCM sender ID
                        .build();

                return options;
            }

        });
        initOnepocket();
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

    private void initOnepocket() {
        new OPKLibraryConfig.Builder()
                .testMode(Constants.TESTING)
                .idPortal(Constants.ID_PORTAL)
                .enableLogging(Constants.LOGGING)
                .build();
    }

}
