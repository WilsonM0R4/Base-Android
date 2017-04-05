package com.allegra.handyuvisa;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.allegra.handyuvisa.utils.Constants;

/**
 * Created by jsandoval on 2/06/16.
 */
public class LegalActivity extends Fragment {

    private final String TAG = "LegalActivity";
    private ImageButton menuButton, backButton;

    private TextView version_legal;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //super.setView(R.layout.fragment_legal, this);

        //checkLogin();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.fragment_legal, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
    }

    public void initViews(View root) {

        ((FragmentMain) getParentFragment()).configToolbar(false, Constants.TYPE_BACK_MENU, getString(R.string.legal_title));
        //setActionbar();

        /*menuButton = (ImageButton) root.findViewById(R.id.menu_image);
        backButton = (ImageButton) root.findViewById(R.id.imageButton6);*/

        //setListeners();

        //this.setContentView(R.layout.activity_my_account_menu);
        initializeListView(root);


    }

    /*private void setListeners(){
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).animate();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceLayout(new MyAccountMenuActivity(), true);
            }
        });
    }*/

    private void initializeListView(View root) {
        ListView lv = (ListView) root.findViewById(R.id.legalOptionsListView);
        final String[] names= {
                getString(R.string.privacy_policy),
                getString(R.string.terms_condition),
        };
        try {
            String versionName = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
            TextView version = (TextView) root.findViewById(R.id.version_legal);
            if (version != null) {
                version.setText("Version: " + versionName);
            }
        } catch (PackageManager.NameNotFoundException e) {
           // Log.e(TAG, "No Version number found");
        }


        Bundle politicalBundle;
        politicalBundle = new Bundle();

        politicalBundle.putBoolean(WebFragment.CAN_RETURN, true);
        politicalBundle.putString(WebFragment.WEB_TITLE, getString(R.string.privacy_policy));
        politicalBundle.putString(WebFragment.STARTER_VIEW, this.getClass().getName());
        politicalBundle.putString(WebFragment.LOADING_URL, Constants.getPolicyUrl());

        WebFragment politicalWeb = new WebFragment();
        politicalWeb.setArguments(politicalBundle);

        Bundle termsBundle;
        termsBundle = new Bundle();

        termsBundle.putBoolean(WebFragment.CAN_RETURN, true);
        termsBundle.putString(WebFragment.WEB_TITLE, getString(R.string.terms_condition));
        termsBundle.putString(WebFragment.STARTER_VIEW, this.getClass().getName());
        termsBundle.putString(WebFragment.LOADING_URL,  Constants.getTermsAndConditionsUrl());

        WebFragment termsWeb = new WebFragment();
        termsWeb.setArguments(termsBundle);

        final Fragment[] fragments = {politicalWeb, termsWeb};
        lv.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.profile_layout_legal, names) {

            public View getView(final int position,View view,ViewGroup parent) {
                LayoutInflater inflater= getActivity().getLayoutInflater();
                View rowView=inflater.inflate(R.layout.profile_layout_legal, null,true);

                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ((FragmentMain) getParentFragment()).replaceLayout(fragments[position], false);

                        /*Intent intent = new Intent(LegalActivity.this,activities[position]);
                        LegalActivity.this.startActivity(intent);*/
                    }
                });
                TextView txtTitle = (TextView) rowView.findViewById(R.id.profileOptionTextLegal);
                txtTitle.setText(names[position]);;
                return rowView;
            };
        });
    }

    /*private ActionBar actionBar;
    private void setActionbar() {
        actionBar = getActivity().getActionBar();
        if(actionBar!=null){
            actionBar.setIcon(R.drawable.ab_icon_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                    | ActionBar.DISPLAY_SHOW_HOME);
        }
    }*/

    public void onMenu(View view) {
        ((MainActivity) getActivity()).animate();
    }




    private void checkLogin() {
        if(((com.allegra.handyuvisa.VisaCheckoutApp) getActivity().getApplication()).getIdSession()==null){

            ((FragmentMain) getParentFragment()).replaceLayout(new LoginActivity(), false);

            /*Intent i =new Intent(this,LoginActivity.class);
            this.startActivityForResult(i, Constants.ACTIVITY_LOGIN);*/
        }
    }


}
