package com.allem.alleminmotion.visacheckout;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.allem.alleminmotion.visacheckout.utils.Constants;

public class MyAccountMenuActivity extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setView(R.layout.activity_my_account_menu, this);

        //checkLogin();
    }

    @Override
    public void initViews(View root) {
        setActionbar();

      //  this.setContentView(R.layout.activity_my_account_menu);
        initializeListView(root);


    }


    private void initializeListView(View root) {
        ListView lv = (ListView) root.findViewById(R.id.profileOptionsListView);
        final String[] names= {
                getString(R.string.title_my_profile),
                getString(R.string.benefits),
                getString(R.string.transactions_history),
                getString(R.string.legal_title),
        };
        final Integer[] images = {R.drawable.menu__profile,R.drawable.my_benefits,R.drawable.coverage,R.drawable.menu__history,
               R.drawable.legal5};
        final Class[] activities = {MyAccountActivity.class, MyBenefits.class, OneTransactionsActivity.class, LegalActivity.class};
        lv.setAdapter(new ArrayAdapter<String>(MyAccountMenuActivity.this, R.layout.profile_layout, names) {

            public View getView(final int position,View view,ViewGroup parent) {
                LayoutInflater inflater= MyAccountMenuActivity.this.getLayoutInflater();
                View rowView=inflater.inflate(R.layout.profile_layout, null,true);

                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MyAccountMenuActivity.this,activities[position]);
                        MyAccountMenuActivity.this.startActivity(intent);
                    }
                });
                TextView txtTitle = (TextView) rowView.findViewById(R.id.profileOptionText);
                ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView);

                txtTitle.setText(names[position]);
                imageView.setImageResource(images[position]);
                return rowView;
            };
        });
    }

    private Button btn_logout;
    private ProgressBar pb_cerrarsesion;


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
    private void stripUnderlines(TextView textView) {
        /*
        SpannedString s = (SpannedString)textView.getText();
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span: spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);

            s.
            s.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            s.setSpan(span, start, end, 0);
        }
        textView.setText(s);
        */
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
