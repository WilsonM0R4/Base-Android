package com.allegra.handyuvisa;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.Button;

public class ActivationActivity extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {

    /*Test Push Dev*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setView(R.layout.activity_activation, this);
    }

    @Override
    public void initViews(View root) {
        setActionbar();

        Button b = (Button) root.findViewById(R.id.activation_call_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivationActivity.this, CallActivity.class);
                ActivationActivity.this.startActivity(intent);
            }
        });
    }

    private ActionBar actionBar;

    private void setActionbar() {
        actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setIcon(R.drawable.ab_icon_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                    | ActionBar.DISPLAY_SHOW_HOME);
        }
    }

    public void onMenu(View view) {
        animate();
    }

    public void back(View view) {
        NavUtils.navigateUpFromSameTask(this);
    }
}
