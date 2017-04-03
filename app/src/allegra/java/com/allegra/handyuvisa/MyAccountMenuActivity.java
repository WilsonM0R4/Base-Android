package com.allegra.handyuvisa;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.allegra.handyuvisa.utils.Constants;
import com.allegra.handyuvisa.utils.CustomizedTextView;

public class MyAccountMenuActivity extends Fragment {

    public static final String VIEW_TYPE_MY_BENEFITS = "my_benefits";

    private ActionBar actionBar;
    private String nombre, apellido, tipoid, numid, nummcard, value1, value2, value3;
    Context ctx;
    CustomizedTextView txtGetYourCertificate;
    ImageButton menuButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //super.setView(R.layout.activity_my_account_menu, this);
        ctx = this.getActivity();
        ((MainActivity) getActivity()).statusBarVisibility(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.activity_my_account_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        initViews(view);
    }

    public void initViews(View root) {

        ((FragmentMain) getParentFragment()).configToolbar(false, Constants.TYPE_MENU, getString(R.string.title_my_account));

        /*menuButton = (ImageButton)root.findViewById(R.id.menu_image);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).animate();
            }
        });*/

        setActionbar();
        initializeListView(root);
    }

    private void initializeListView(View root) {


        ListView lv = (ListView) root.findViewById(R.id.profileOptionsListView);
        final String[] names = {
                getString(R.string.title_my_profile),
                getString(R.string.benefits),
             //   getString(R.string.mytips),
                getString(R.string.coverage),
                getString(R.string.transactions_history),
                getString(R.string.legal_title),
        };
        final Integer[] images = {R.drawable.menu__profile, R.drawable.my_benefits,/*R.drawable.menu_tips*/R.drawable.coverage, R.drawable.menu__history,
                R.drawable.legal5};
        /*final Class[] activities = {MyAccountActivity.class, com.allegra.handyuvisa.MyBenefits.class, //MyTips.class,
                ProofOfCoverageDinamicoActivity.class, OneTransactionsActivity.class, LegalActivity.class};*/

        Bundle benefitsBundle;

        benefitsBundle = new Bundle();

        benefitsBundle.putString(WebFragment.WEB_TITLE, getString(R.string.benefits));
        benefitsBundle.putString(WebFragment.STARTER_VIEW, VIEW_TYPE_MY_BENEFITS);
        benefitsBundle.putString(WebFragment.LOADING_URL, Constants.getMyBenefitsUrl());
        benefitsBundle.putBoolean(WebFragment.CAN_RETURN, true);

        WebFragment benefits = new WebFragment();
        benefits.setArguments(benefitsBundle);

        final Fragment[] fragments = {new MyAccountActivity(), benefits,
                new ProofOfCoverageDinamicoActivity(), new OneTransactionsActivity(),
                new LegalActivity()};

        lv.setAdapter(new ArrayAdapter<String>(MyAccountMenuActivity.this.getActivity(),
                R.layout.profile_layout, names) {

            public View getView(final int position, View view, ViewGroup parent) {
                final LayoutInflater inflater = getActivity().getLayoutInflater();
                View rowView = inflater.inflate(R.layout.profile_layout, null, true);

                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ((FragmentMain) getParentFragment()).replaceLayout(fragments[position], false);

                        /*Intent intent = new Intent(MyAccountMenuActivity.this, activities[position]);
                        MyAccountMenuActivity.this.startActivity(intent);*/
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
        actionBar = getActivity().getActionBar();
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
        /*setContentView(R.layout.get_your_certificate_dynamic);
        txtGetYourCertificate = (CustomizedTextView) findViewById(R.id.txtGetYourCertificatedynamic);
        txtGetYourCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchloginActivity();
            }
        });*/


    }

    public void onUpProof(View view) {
        //onBackPressed();
    }

    public void onUpProofDinamico(View view) {
        //onBackPressed();
    }

    void launchloginActivity() {

        ((FragmentMain) getParentFragment()).replaceLayout(new LoginActivity(), false);

        /*Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);*/
        //finish();
    }

    public void onMenu(View view) {
        ((MainActivity) getActivity()).animate();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Validate login successful
        if (resultCode == MainActivity.RESULT_OK)//Constants.ACTIVITY_LOGIN
        {
            Intent i = new Intent(ctx, ProofOfCoverageDinamicoActivity.class);
            this.startActivity(i);
            //sendIntentForProofOfCoverage();
        }

    }
}
