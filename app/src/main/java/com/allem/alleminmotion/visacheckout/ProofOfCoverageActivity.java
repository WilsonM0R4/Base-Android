package com.allem.alleminmotion.visacheckout;

import android.os.Bundle;
import android.view.View;

public class ProofOfCoverageActivity extends FrontBackAnimate  implements FrontBackAnimate.InflateReadyListener {


    //********************OVERRIDE METHODS*******************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_proof_of_coverage);
        super.setView(R.layout.activity_proof_of_coverage, this);
    }

    @Override
    public void initViews(View root) {


    }

    //********************PROPER METHODS*******************

    public void onMenu(View view) {
        animate();
    }

}
