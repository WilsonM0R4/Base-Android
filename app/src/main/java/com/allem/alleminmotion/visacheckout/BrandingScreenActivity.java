package com.allem.alleminmotion.visacheckout;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class BrandingScreenActivity extends Activity {

    private static final String TAG = "BrandingScreenActivity";

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intro_screen);
        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BrandingScreenActivity.this, "login now", Toast.LENGTH_SHORT).show();
            }
        });

        Button register = (Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BrandingScreenActivity.this, "register now", Toast.LENGTH_SHORT).show();
            }
        });


        android.app.ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        initFromLocal();
    }

    private void initFromLocal() {
        Uri video = Uri.parse("android.resource://com.allem.alleminmotion.visacheckout/raw/intro");

        videoView = (VideoView) findViewById(R.id.fullscreen_content);
        videoView.setVideoURI(video);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                Log.d(TAG, "Loop ...");
            }
        });
        videoView.start();
    }

    public void onSkipIntro(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}
