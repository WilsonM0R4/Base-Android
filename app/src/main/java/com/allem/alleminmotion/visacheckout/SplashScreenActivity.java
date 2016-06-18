package com.allem.alleminmotion.visacheckout;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.VideoView;

public class SplashScreenActivity extends Activity {

    private static final String VIDEO_INTRO = "android.resource://com.allem.alleminmotion.visacheckout/raw/intro";

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);
/*        Intent intent = new Intent(this, IntroScreenActivity.class);
        startActivity(intent);
        finish();*/

        initFromLocal();
    }

    private void initFromLocal() {
        Uri video = Uri.parse(VIDEO_INTRO);

        videoView = (VideoView) findViewById(R.id.fullscreen_content);
        videoView.setVideoURI(video);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
        videoView.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.start();
    }

}
