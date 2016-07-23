package com.allem.alleminmotion.visacheckout;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.allem.alleminmotion.visacheckout.utils.Constants;
import com.allem.onepocket.BackHandler;
import com.allem.onepocket.MenuHandler;
import com.allem.onepocket.NextHandler;
import com.allem.onepocket.RegisterCallback;
import com.allem.onepocket.utils.OPKConstants;

import java.util.ArrayList;

@Deprecated
public class OnepocketAddCardActivity extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {

    private String sessionId;
    //private AddCardFragment addCard;
    private ArrayList<Fragment> stack = new ArrayList<>(4);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkLogin();
        super.setView(R.layout.activity_onepocket_add_card, this);

        RegisterCallback.registerNext(new NextHandler() {
            @Override
            public void handler(Fragment fragment) {

            }

            @Override
            public void handler2(Fragment fragment) {
                addFragment(fragment);
            }

            @Override
            public void returnResult(Bundle bundle) {

            }

            @Override
            public void perform(int i, String data) {

            }
        });

        RegisterCallback.registerMenu(new MenuHandler() {
            @Override
            public void handle() {
                animate();
            }
        });

        RegisterCallback.registerBack(new BackHandler() {
            @Override
            public void handle() {
                onUp(null);
            }
        });
    }

    @Override
    public void initViews(View root) {
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        sessionId = ((VisaCheckoutApp)getApplication()).getIdSession();
//        addCard = new AddCardFragment();
//
//        Bundle arguments = new Bundle();
//        arguments.putString(OPKConstants.EXTRA_SESSION_ID, sessionId);
//        addCard.setArguments(arguments);
//        transaction.add(R.id.opk_top, addCard);
//        transaction.commit();
    }

    private void addFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment currentTop = getFragmentManager().findFragmentById(R.id.opk_top);
        transaction.hide(currentTop);

        for (Fragment f : stack) {
            transaction.hide(f);
            Log.d("OPK", "hide the old one from stack: " + f);
        }
        transaction.add(R.id.opk_top, fragment, fragment.toString());
        transaction.commit();

        stack.add(fragment);

    }

    private void checkLogin() {
        if(((VisaCheckoutApp)this.getApplication()).getIdSession()==null){
            Intent i =new Intent(OnepocketAddCardActivity.this, LoginActivity.class);
            this.startActivityForResult(i, Constants.ACTIVITY_LOGIN);
            finish();
        }
    }


}
