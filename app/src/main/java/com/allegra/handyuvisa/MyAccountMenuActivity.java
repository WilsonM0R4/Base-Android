package com.allegra.handyuvisa;

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

import com.allegra.handyuvisa.utils.Constants;

public class MyAccountMenuActivity extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {

    //AllemUser user;
    int idCuenta;
    private Button btn_logout;
    private ProgressBar pb_cerrarsesion;
    private ActionBar actionBar;
    /*private Context ctx;
    private ArrayList<NameValuePair> postValues;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setView(R.layout.activity_my_account_menu, this);
        //MyBus.getInstance().register(this);
         // ctx = this;
        //Request Soap
        //postValues = new ArrayList<>();
        //checkLogin();
    }


    @Override
    public void initViews(View root) {
        setActionbar();
        initializeListView(root);
    }


  /*  @Subscribe
    public void onAsyncTaskResult(AsyncTaskSoapObjectResultEvent event) {
        if (event.getCodeRequest() == Constants.ACTIVITY_LOGIN) {
            //setWaitinUI(false);
            if (event.getResult() != null) {
                AllemUser user = SoapObjectParsers.toAllemUser(event.getResult());
                ((VisaCheckoutApp) this.getApplication()).setIdSession(user.idSesion);
                ((VisaCheckoutApp) this.getApplication()).setIdCuenta(user.idCuenta);
                //((VisaCheckoutApp) this.getApplication()).setRawPassword(password.getText().toString());
                String name = user.email.substring(0, user.email.indexOf('@'));
                String domain = user.email.substring(user.email.indexOf('@') + 1, user.email.length()).replace(".", "");
                String channel = name + domain + user.idCuenta;
                String password = user.hashpassword;

                idCuenta = user.idCuenta;
                Log.e("Serfar idCuenta MyAcc", String.valueOf(user.idCuenta));
                //Log.e("Serfar", "password MyAcc" + password);
                Log.e("Serfar", "chnel: MyAcc" + channel);
               *//* Constants.saveUser(ctx, user, channel);
                ((VisaCheckoutApp) this.getApplication()).unSetParseChannels();
                ((VisaCheckoutApp) this.getApplication()).parseUser(user.email, channel);*//*

                setResult(RESULT_OK);
                finish();
            } else {
               // Toast.makeText(ctx, event.getFaultString(), Toast.LENGTH_LONG).show();
            }
        }
    }*/

    private void initializeListView(View root) {

        ListView lv = (ListView) root.findViewById(R.id.profileOptionsListView);
        final String[] names= {
                getString(R.string.title_my_profile),
                getString(R.string.benefits),
                getString(R.string.coverage),
                getString(R.string.transactions_history),
                getString(R.string.legal_title),
        };
        final Integer[] images = {R.drawable.menu__profile,R.drawable.my_benefits,R.drawable.coverage,R.drawable.menu__history,
               R.drawable.legal5};
        final Class[] activities = {MyAccountActivity.class, MyBenefits.class,
                ProofOfCoverageActivity.class, OneTransactionsActivity.class, LegalActivity.class};
        lv.setAdapter(new ArrayAdapter<String>(MyAccountMenuActivity.this, R.layout.profile_layout, names) {

            public View getView(final int position,View view,ViewGroup parent) {
                LayoutInflater inflater= MyAccountMenuActivity.this.getLayoutInflater();
                View rowView=inflater.inflate(R.layout.profile_layout, null,true);

                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (position==2){
                            checkLogin();

                        } else {
                            Intent intent = new Intent(MyAccountMenuActivity.this, activities[position]);
                            MyAccountMenuActivity.this.startActivity(intent);
                        }
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
            MyAccountMenuActivity.this.startActivityForResult(i, Constants.ACTIVITY_LOGIN);
        } else {
            Intent i = new Intent(this,ProofOfCoverageActivity.class);
            this.startActivity(i);
            //sendIntentForProofOfCoverage();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Validate login successful
        if (resultCode == RESULT_OK)//Constants.ACTIVITY_LOGIN
        {
            Intent i = new Intent(this,ProofOfCoverageActivity.class);
            this.startActivity(i);
            //sendIntentForProofOfCoverage();
        }

    }


    //Get user's idCuenta and send to ProofOfCoverageActivity
/*    private void sendIntentForProofOfCoverage(){
        //Get user's idCuenta

        //Log.e("SerfarsendIntentForProo",String.valueOf(idCuenta));
        //Log.e("Sergini", "Entra al resultCode == RESULT_OK");
        Intent i = new Intent( this,ProofOfCoverageActivity.class);
        MyAccountMenuActivity.this.startActivity(i);
    }*/

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
