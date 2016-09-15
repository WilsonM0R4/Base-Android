package com.allegra.handyuvisa;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.VideoView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class IntroScreenActivity extends Activity {

    private static final String TAG = "IntroActivity";
    private static final String VIDEO_INTRO = "";//android.resource://com.allem.alleminmotion.visacheckout/raw/intro
    private static final String VIDEO_BACKGROUND = "";//android.resource://com.allem.alleminmotion.visacheckout/raw/background_visa

    private VideoView videoView;
    private Button login;
    private Button register;
    private Button skipIntro;
    private String videoName = VIDEO_INTRO;
    private LinearLayout header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intro_screen);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroScreenActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroScreenActivity.this, LoginNewUser.class);
                startActivity(intent);
            }
        });

        skipIntro = (Button) findViewById(R.id.skip_intro);
        header = (LinearLayout) findViewById(R.id.ll_header);

        android.app.ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        initFromLocal();
    }

    private void initFromLocal() {
        Uri video = Uri.parse(videoName);

        videoView = (VideoView) findViewById(R.id.fullscreen_content);
        videoView.setVideoURI(video);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (videoName.equals(VIDEO_INTRO)) {
                    videoName = VIDEO_BACKGROUND;
                    Uri video = Uri.parse(videoName);
                    mediaPlayer.reset();
                    try {
                        mediaPlayer.setDataSource(IntroScreenActivity.this, video);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (Exception ex) {

                    }
                    setButtonStates(true);
                } else {
                    mediaPlayer.start();
                    Log.d(TAG, "Loop ...");
                }
            }
        });
        videoView.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoName = VIDEO_INTRO;
        videoView.start();
        setButtonStates(false);
    }

    public void onSkipIntro(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setButtonStates(boolean enable) {
        login.setEnabled(enable);
        login.setVisibility((enable) ? View.VISIBLE : View.INVISIBLE);
        register.setEnabled(enable);
        register.setVisibility((enable) ? View.VISIBLE : View.INVISIBLE);
        skipIntro.setEnabled(enable);
        skipIntro.setVisibility((enable) ? View.VISIBLE : View.INVISIBLE);
        header.setVisibility((enable) ? View.VISIBLE : View.INVISIBLE);
    }
}
