package com.allegra.handyuvisa;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.allegra.handyuvisa.Intellilink;
import com.allegra.handyuvisa.Myvisaenterprise;
import com.allegra.handyuvisa.utils.Connectivity;
import com.allegra.handyuvisa.utils.CustomizedTextView;

public class MyAccountMenuActivity extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {


    private ActionBar actionBar;
    private String nombre, apellido, tipoid, numid, nummcard, value1, value2, value3;
    Context ctx;
    CustomizedTextView txtGetYourCertificate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setView(R.layout.activity_my_account_menu, this);
        ctx = this;

    }


    @Override
    public void initViews(View root) {
        setActionbar();
        initializeListView(root);
    }

    private void initializeListView(View root) {


        ListView lv = (ListView) root.findViewById(R.id.profileOptionsListView);
        final String[] names = {
                getString(R.string.title_my_profile),
                getString(R.string.benefits),
                getString(R.string.coverage),
                getString(R.string.myvisaenterprise),
                getString(R.string.transactions_history),
                getString(R.string.legal_title),
        };
        final Integer[] images = {
                R.drawable.menu__profile,
                R.drawable.my_benefits,
                R.drawable.coverage,
                R.drawable.visa_business,
                R.drawable.menu__history,
                R.drawable.legal5};
        final Class[] activities = {
                MyAccountActivity.class,
                com.allegra.handyuvisa.MyBenefits.class,
                ProofOfCoverageDinamicoActivity.class,
                Myvisaenterprise.class,
                OneTransactionsActivity.class,
                LegalActivity.class};
        lv.setAdapter(new ArrayAdapter<String>(MyAccountMenuActivity.this, R.layout.profile_layout, names) {

            public View getView(final int position, View view, ViewGroup parent) {
                final LayoutInflater inflater = MyAccountMenuActivity.this.getLayoutInflater();
                View rowView = inflater.inflate(R.layout.profile_layout, null, true);

                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            Intent intent = new Intent(MyAccountMenuActivity.this, activities[position]);
                            MyAccountMenuActivity.this.startActivity(intent);
                    }

                });
                TextView txtTitle = (TextView) rowView.findViewById(R.id.profileOptionText);
                ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView);

                txtTitle.setText(names[position]);
                imageView.setImageResource(images[position]);
                return rowView;
            }

            ;
        });
    }

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

    void setGetYourCertificateLayout() {
        //Change layout
        setContentView(R.layout.get_your_certificate_dynamic);
        txtGetYourCertificate = (CustomizedTextView) findViewById(R.id.txtGetYourCertificatedynamic);
        txtGetYourCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchloginActivity();
            }
        });


    }

    public void onUpProof(View view) {
        onBackPressed();
    }

    public void onUpProofDinamico(View view) {
        onBackPressed();
    }

    void launchloginActivity() {

        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    public void onMenu(View view) {
        animate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Validate login successful
        if (resultCode == RESULT_OK)//Constants.ACTIVITY_LOGIN
        {
            Intent i = new Intent(this, ProofOfCoverageActivity.class);
            this.startActivity(i);
            //sendIntentForProofOfCoverage();
        }

    }
}
