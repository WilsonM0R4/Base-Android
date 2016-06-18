package com.allem.alleminmotion.visacheckout;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.allem.alleminmotion.visacheckout.twilio.BasicPhone;

import java.util.HashMap;
import java.util.Map;
/**
 * Created by jsandoval on 1/06/16.
 */
public class CallActivityMcard extends CallActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OTC_NUMBER = "+5714890939";
    }
}
