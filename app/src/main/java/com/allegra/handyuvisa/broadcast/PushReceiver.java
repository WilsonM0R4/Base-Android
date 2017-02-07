package com.allegra.handyuvisa.broadcast;


import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.allegra.handyuvisa.utils.Constants;
import com.allegra.handyuvisa.MainActivity;
import com.allegra.handyuvisa.R;
import com.allegra.handyuvisa.models.Notifications;
import com.allegra.handyuvisa.utils.KeySaver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by victor on 09/03/15.
 * com.allem.allemevent.broadcast
 */
public class PushReceiver extends BroadcastReceiver {

    private static final String TAG="PushReceiver";
    private static final int NOTIFICATION_ID = 30001;
    protected static int numMessages = 0;
    private String channelcont,link;
    private JSONObject notifications,notifItem;
    private JSONArray notifArray;
    private ArrayList<Notifications> notif_array;
    private static final long[] pattern = {0,50,100,50,100,100,50,200,20,100,50,50,100,50,100};


    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            String action = intent.getAction();

           // Log.d(TAG, intent.getExtras().getString("com.parse.Data"));
           // Log.d(TAG, action);
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            if (action.equalsIgnoreCase("com.parse.push.intent.RECEIVE")) {
                String alert = json.getString("alert");
                if (json.has("link")){
                    link = json.getString("link");
                } else {
                    link = "";
                }

               // Log.d(TAG, "alert = " + alert);
               // Log.d(TAG, "link = " + link);
                String channel = intent.getExtras().getString("com.parse.Channel");
                if (channel == null) {
                    channel = Constants.PUSH_GLOBAL;
                }
//                channelcont = KeySaver.getStringSavedShare(context, channel);
                notifications = new JSONObject();
                if (channelcont==null){
                    notifItem = new JSONObject();
                    notifItem.put("msg",alert)
                             .put("url",link)
                             .put("timeinmillis", System.currentTimeMillis());
                    notifArray = new JSONArray();
                    notifArray.put(notifItem);
                    notifications.put("notif", notifArray);
                }else{
                    notifications = new JSONObject(channelcont);
                    notifications.getJSONArray("notif").put(new JSONObject().put("msg",alert)
                                                                            .put("url",link)
                                                                            .put("timeinmillis", System.currentTimeMillis()));
                }

               // Log.d(TAG, "Se guarda en channel " + channel + " en shared: " + notifications.toString());
                KeySaver.saveShare(context,channel,notifications.toString());
                Intent bcast = new Intent();
                bcast.setAction(Constants.BCAST_NOTIFIC_UPDATE);
                bcast.putExtra("result", Constants.RESULT_UPDATE);
                context.sendBroadcast(bcast);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    notificateV16(context,intent,alert);
                } else {
                    notificate(context,intent,alert);
                }
            }
        } catch (JSONException e) {
           // Log.d("jsonexc", "JSONException: " + e.getMessage());
        }
    }



    @SuppressLint("NewApi")
    protected void notificateV16(Context context, Intent intent,String alert) {
        Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/raw/allegra_sound");
        Log.d(TAG, sound.getPath());
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_stat_notif)
                        .setContentTitle(context.getResources().getString(R.string.app_name))
                        .setContentText(alert)
                        .setSound(sound)
                        .setVibrate(pattern)
                        .setAutoCancel(true);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.putExtra("launcher","notification");
        resultIntent.putExtra("link",link);
        //resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

    // The stack builder object will contain an artificial back stack for the
    // started Activity.
    // This ensures that navigating backward from the Activity leads out of
    // your application to the Home screen.

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

    // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);


    // Adds the Intent that starts the Activity to the top of the stack


        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    // mId allows you to update the notification later on.
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }


    protected void notificate(Context context, Intent intent,String alert) {
        Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/raw/allegra_sound");
        Log.d(TAG, sound.getPath());
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_notif)
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setContentText(alert)
                .setSound(sound)
                .setVibrate(pattern)
                .setAutoCancel(true);

        // Creates an explicit intent for an Activity in your app
        //Intent resultIntent = new Intent(context, MainActivity.class);
        Intent resultIntent = new Intent(context,MainActivity.class);
        resultIntent.putExtra("launcher","notifications");
        resultIntent.putExtra("link",link);
        //resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

    // The stack builder object will contain an artificial back stack for the
    // started Activity.
    // This ensures that navigating backward from the Activity leads out of
    // your application to the Home screen.

        android.support.v4.app.TaskStackBuilder stackBuilder = android.support.v4.app.TaskStackBuilder.create(context);

    // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);


    // Adds the Intent that starts the Activity to the top of the stack


        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    // mId allows you to update the notification later on.
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

}
