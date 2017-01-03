package com.allegra.handyuvisa;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.allegra.handyuvisa.utils.Constants;
import com.allem.onepocket.BackHandler;
import com.allem.onepocket.MenuHandler;
import com.allem.onepocket.NextHandler;
import com.allem.onepocket.RegisterCallback;
import com.allem.onepocket.TransactionsFragment;

public class OneTransactionsActivity extends FrontBackAnimate  implements FrontBackAnimate.InflateReadyListener {

    private static final String TAG = "OPK_Transaction";

    private TransactionsFragment oneTransctions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setView(R.layout.activity_one_transactions, this);

        RegisterCallback.registerNext(new NextHandler() {
            @Override
            public void handler(Fragment fragment) {
                if (fragment != null) {
                    addFragment(fragment);
                } else {
                    // clear the stack
                    Log.d(TAG, "Clear the stack");
                    FragmentManager manager = getFragmentManager();
                    manager.popBackStackImmediate();
                }
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
                switch (i) {
                    case 0:
                        Intent i1 = new Intent(OneTransactionsActivity.this, CallActivityMcard.class);
                        startActivity(i1);
                        break;

                    case 1:
                        Intent i2 = new Intent(OneTransactionsActivity.this, CallActivity.class);
                        startActivityForResult(i2, 1);
                        break;

                }

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
        checkLogin();

    }

    @Override
    public void onBackPressed() {

    }

    private void addFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment currentTop = getFragmentManager().findFragmentById(R.id.opk_top);
        transaction.hide(currentTop);

        transaction.add(R.id.opk_top, fragment, fragment.toString());
        transaction.addToBackStack(null);
        transaction.commit();

    }


    private void checkLogin() {
        if(((VisaCheckoutApp)this.getApplication()).getIdSession()==null){
            Intent i =new Intent(OneTransactionsActivity.this, LoginActivity.class);
            this.startActivityForResult(i, Constants.ONE_POCKET_NEEDS_LOGIN);
            finish();
        }else {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            Bundle bundle = Constants.createDataBundle(Constants.getUser(this), (VisaCheckoutApp) getApplication());
            oneTransctions = new TransactionsFragment();
            oneTransctions.setArguments(bundle);
            transaction.add(R.id.opk_top, oneTransctions);
            transaction.commit();
        }
    }


}
