package com.allegra.handyuvisa;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.allegra.handyuvisa.models.AllemUser;
import com.allegra.handyuvisa.models.Notifications;
import com.allegra.handyuvisa.utils.Constants;
import com.allegra.handyuvisa.utils.KeySaver;
import com.allegra.handyuvisa.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by lisachui on 12/1/15.
 */
public class FrontFragment extends Fragment implements
                                    TextureView.SurfaceTextureListener,
                                    MediaPlayer.OnPreparedListener,
                                    MediaPlayer.OnErrorListener {

    private static final String TAG = "FrontFragment";
    private static final String VIDEO_BACKGROUND = "android.resource://com.allegra.handyuvisa/raw/background_dark";
    Surface surf;
    private MediaPlayer mMediaPlayer;
    private TextureView videoView; // TextView for display (similar to VideoView?)
    private ArrayList<Notifications> notifications;


    //***************BroadcastReceivers****************

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
         //   Log.d(TAG,"Notification received");
            loadNotifications(true);
        }
    };

    //************OVERRIDE METHODS*****************

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView;
        if (((VisaCheckoutApp)getActivity().getApplication()).getIdSession()==null) {
            rootView = inflater.inflate(R.layout.activity_intro_screen, container, false);
        } else {
            rootView = inflater.inflate(R.layout.fragment_front, container, false);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        setVideoView();

        if (Util.isAuthenticated(getActivity())) {
            AllemUser user = Constants.getUser(getActivity());
            TextView greeting = (TextView) getActivity().findViewById(R.id.tv_username);
            String format = getActivity().getResources().getString(R.string.txt_user_greeting);
            String value = String.format(format, user.nombre);
            greeting.setText(value);

            TextView current = (TextView) getActivity().findViewById(R.id.tv_current_time);
            current.setText(Util.getFormattedTime());

            loadNotifications(true);
            if (notifications != null) {
                TextView notifMessages = (TextView) getActivity().findViewById(R.id.tv_notifications);
                StringBuilder buf = new StringBuilder();
                for (int i = 0; i < notifications.size(); i++) {
                    buf.append(notifications.get(i).message).append("\n");
                }
                notifMessages.setText(buf.toString());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        resumeMediaPlayer();
        registerFilter();
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            releaseMediaPlayer();
            getActivity().unregisterReceiver(receiver);
          //  Log.d(TAG, "Unregister broadcast filter");
        } catch (Exception ex) {
          //  Log.e(TAG, "Fail to unregister broadcast receiver: " + ex.getMessage());
        }
    }

    private void setVideoView(){
        videoView = (TextureView) getActivity().findViewById(R.id.front_video);
        videoView.setSurfaceTextureListener(this);
    }

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mMediaPlayer.start();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
       // Log.e(TAG, "Error loading video: what: " + what + " extra: " + extra);
        return false;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int i, int i1) {

     //   Log.d("onSurfaceTexture", "ACA RECREA EL VIDEO");
        Surface s = new Surface(surface);
        surf=s;
        resumeMediaPlayer();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }


    private void registerFilter() {
       // Log.d(TAG, "register broadcast filter");
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.BCAST_NOTIFIC_UPDATE);
        getActivity().registerReceiver(receiver, filter);
    }

    private void loadNotifications(boolean frombcast) {
        if (notifications != null) {
            notifications.clear();
        }

        String channel = KeySaver.getStringSavedShare(getActivity(), Constants.KEY_PUSH_CHANNEL);
        try{
            JSONObject notif;
            String notif_shared = KeySaver.getStringSavedShare(getActivity(),Constants.PUSH_GLOBAL);
          //  Log.d(TAG,"notif_global: "+notif_shared);
            if(notif_shared!=null){
                if(notifications==null){
                    notifications = new ArrayList<>();
                }
                notif = new JSONObject(notif_shared);
                for(int i=(notif.getJSONArray("notif").length()-1);i>=0;i--){
                    notifications.add(getNotification(notif.getJSONArray("notif").getJSONObject(i)));
                }
            }
            if ((((VisaCheckoutApp) getActivity().getApplication()).getIdSession() != null)){
                if(KeySaver.isExist(getActivity(), channel)){
                    notif_shared = KeySaver.getStringSavedShare(getActivity(), channel);
                    if (notif_shared!=null){
                     //   Log.d(TAG,"notif_shared: "+notif_shared);
                        if(notifications==null){
                            notifications = new ArrayList<>();
                        }
                        notif = new JSONObject(notif_shared);
                        for(int i=(notif.getJSONArray("notif").length()-1);i>=0;i--){
                            notifications.add(getNotification(notif.getJSONArray("notif").getJSONObject(i)));
                        }
                    }
                }
            }

//            if(notifications!=null){
//
//                Collections.sort(notifications, new Comparator<Notifications>() {
//                    public int compare(Notifications p1, Notifications p2) {
//                        return Long.valueOf(p2.timeinmillis).compareTo(p1.timeinmillis);
//                    }
//                });
//
//                adapter = new NotifAdapter(ctx,notifications);
//
//                if (frombcast) {adapter.notifyDataSetChanged();
//                    lv_notific.setAdapter(adapter);}
//                else{lv_notific.setAdapter(adapter);}
//                if (notifications.size()>0){
//                    tv_no_notif.setVisibility(View.GONE);
//                    lv_notific.setVisibility(View.VISIBLE);
//                }else{
//                    tv_no_notif.setVisibility(View.VISIBLE);
//                    lv_notific.setVisibility(View.GONE);
//                }
//            }

        }catch(Exception e){
           // Log.e(TAG,e.toString());
        }
    }

    private Notifications getNotification(JSONObject notific){
        Notifications result=null;
        try {
            String msg = null;
           // Log.d(TAG,notific.toString());
            msg = notific.getString("msg");
            String url;
            if (notific.has("url")){
                url=notific.getString("url");
            }else{
                url="";
            }
            long timeinmillis = notific.getLong("timeinmillis");
            result =  new Notifications(msg,url,timeinmillis);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;

    }

    private void resumeMediaPlayer(){

        final Uri videoU = Uri.parse(VIDEO_BACKGROUND);
        //if (mMediaPlayer != null) {
            try {
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        // KitKat only: even though MediaPlayer goes to PREPARE state, the video
                        // may not be playing; retrying helps in this case;
                        if (mMediaPlayer!=null) {
                            if (mMediaPlayer.isPlaying()) {
                                mMediaPlayer.start();
                            } else {
                                mediaPlayer.reset();
                                try {
                                    mMediaPlayer.setDataSource(getActivity(), videoU);
                                } catch (IOException e) {
                                   // Log.e(TAG, "Can't initialize media playing for loading video");
                                }
                                mMediaPlayer.prepareAsync();
                            }
                        }
                    }
                });

                mMediaPlayer.setDataSource(getActivity(), videoU);
                mMediaPlayer.setSurface(surf);
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.prepareAsync();
                mMediaPlayer.setOnPreparedListener(this);
                mMediaPlayer.setLooping(true);
            } catch (IllegalArgumentException e) {
               // Log.e(TAG, "Error loading video: ", e);
            } catch (SecurityException e) {
              //  Log.e(TAG, "Error loading video: ", e);
            } catch (IllegalStateException e) {
             //   Log.e(TAG, "Error loading video: ", e);
            } catch (IOException e) {
              //  Log.e(TAG, "Error loading video: ", e);
            }

      //  }
    }

}

