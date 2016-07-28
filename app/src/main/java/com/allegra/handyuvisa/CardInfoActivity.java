package com.allegra.handyuvisa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class CardInfoActivity extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {

    public static final String EXTRA_CALL_ID = "callId";
    public static final String EXTRA_ENC_KEY = "encKey";
    public static final String EXTRA_ENC_DATA = "encData";

    private SeekBar seekBar;
    private TextView payments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setView(R.layout.fragment_card_info, this);

    }

    @Override
    public void initViews(View root) {
        payments = (TextView) root.findViewById((R.id.et_number_payments));
        seekBar = (SeekBar) root.findViewById(R.id.sb_number_payments);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                payments.setText(Integer.toString(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void onClearCVV2Text(View view) {
        ((TextView) findViewById(R.id.et_cvv2)).setText("");
    }

    public void onClearIDText(View view) {
        ((TextView) findViewById(R.id.et_card)).setText("");
    }

    public void onPayNow(View view) {
        String paramCardId = ((TextView) findViewById(R.id.et_card)).getText().toString();
        String paramCVV2 = ((TextView) findViewById(R.id.et_cvv2)).getText().toString();
        String paramPayments = payments.getText().toString();

        Intent intent = getIntent();
        String paramCallId = intent.getStringExtra(EXTRA_CALL_ID);
        String paramEncKey = intent.getStringExtra(EXTRA_ENC_KEY);
        String paramEncData = intent.getStringExtra(EXTRA_ENC_DATA);

        // all payment data to backend now

        // TODO Let's go back to Main Menu for now; resume work when the backend API is ready
        Intent next = new Intent(this, MainActivity.class);
        startActivity(next);
    }
}
