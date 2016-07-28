package com.allegra.handyuvisa;

import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.allegra.handyuvisa.utils.Constants;

/**
 * Created by jsandoval on 2/06/16.
 */
public class LegalActivity extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener{

    private final String TAG = "LegalActivity";

    private TextView version_legal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setView(R.layout.fragment_legal, this);

        //checkLogin();
    }

    @Override
    public void initViews(View root) {
        setActionbar();

        //  this.setContentView(R.layout.activity_my_account_menu);
        initializeListView(root);


    }


    private void initializeListView(View root) {
        ListView lv = (ListView) root.findViewById(R.id.legalOptionsListView);
        final String[] names= {
                getString(R.string.privacy_policy),
                getString(R.string.terms_condition),
        };
        try {
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            TextView version = (TextView) root.findViewById(R.id.version_legal);
            if (version != null) {
                version.setText("Version: " + versionName);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "No Version number found");
        }

        final Class[] activities = {PoliticalActivity.class, TermsActivity.class};
        lv.setAdapter(new ArrayAdapter<String>(LegalActivity.this, R.layout.profile_layout_legal, names) {

            public View getView(final int position,View view,ViewGroup parent) {
                LayoutInflater inflater= LegalActivity.this.getLayoutInflater();
                View rowView=inflater.inflate(R.layout.profile_layout_legal, null,true);

                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(LegalActivity.this,activities[position]);
                        LegalActivity.this.startActivity(intent);
                    }
                });
                TextView txtTitle = (TextView) rowView.findViewById(R.id.profileOptionTextLegal);
                txtTitle.setText(names[position]);;
                return rowView;
            };
        });
    }

    private ActionBar actionBar;
    private void setActionbar() {
        actionBar = getActionBar();
        if(actionBar!=null){
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




    private void checkLogin() {
        if(((VisaCheckoutApp)this.getApplication()).getIdSession()==null){
            Intent i =new Intent(this,LoginActivity.class);
            this.startActivityForResult(i, Constants.ACTIVITY_LOGIN);
        }
    }

    private static class URLSpanNoUnderline extends URLSpan {
        public URLSpanNoUnderline(String url) {
            super(url);
        }
        @Override public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }
}
