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
import android.util.Log;
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
import com.allegra.handyuvisa.utils.LoginCallback;
import com.allegra.handyuvisa.utils.NavigationCallback;
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
                                    MediaPlayer.OnErrorListener{

    private static final String TAG = "FrontFragment";
    private static final String VIDEO_BACKGROUND = "android.resource://com.allegra.handyuvisa/raw/background_dark";
    Surface surf;
    private MediaPlayer mMediaPlayer;
    private TextureView videoView; // TextView for display (similar to VideoView?)

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
        ((MainActivity) getActivity()).statusBarVisibility(true);



        TextureView textureView = (TextureView)rootView.findViewById(R.id.front_video);
        textureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentMain) getParentFragment()).animate();
            }
        });

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

        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        View login, register;

        login = view.findViewById(R.id.login);
        register = view.findViewById(R.id.register);

        if(login!=null && register!=null){
            view.findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.e("Front", "login pressed");

                    LoginActivity login = new LoginActivity();
                    login.setSuccessLoginListener(new LoginCallback() {
                        @Override
                        public void onLoginFinished() {
                            Log.e("Front", "login clicked");
                        }
                    });
                    ((FragmentMain) getParentFragment()).replaceLayout(
                            login, false);
                }
            });

            view.findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((FragmentMain) getParentFragment()).replaceLayout(new LoginNewUser(), false);
                }
            });
        }

        ((FragmentMain) getParentFragment()).configToolbar(true, 0, "");

    }

    @Override
    public void onResume() {
        super.onResume();
        resumeMediaPlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseMediaPlayer();
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

