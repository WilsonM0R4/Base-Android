package com.allegra.handyuvisa;

import android.app.Activity;
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
import com.allem.onepocket.PurchaseSummaryFragment;
import com.allem.onepocket.RegisterCallback;
import com.allem.onepocket.model.OneTransaction;
import com.allem.onepocket.utils.OPKConstants;


public class OnepocketPurchaseActivity extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {

    private static final String TAG = "OPK_purchaseActivity";
    private PurchaseSummaryFragment summary;

    //**************OVERRIDE METHODS****************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setView(R.layout.activity_onepocket_purchase, this);

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
                Intent result = new Intent();
                result.putExtra(OPKConstants.EXTRA_RESULT, data);
                OnepocketPurchaseActivity.this.setResult(Activity.RESULT_OK, result);

                switch (i) {
                    case 0:
                        //Log.e(TAG, "perform id: 0 - MCARD - Return from onepocket");
                        if (OPKConstants.oneTransaction.getType().equals("MCARD") && data.equals("onepocket_return")) {
                            OnepocketPurchaseActivity.this.finish();
                        }
                        break;

                    case 1:
                        Intent intent = new Intent(OnepocketPurchaseActivity.this, OnepocketContainerActivity.class);
                        Bundle bundle = Constants.createDataBundle(Constants.getUser(OnepocketPurchaseActivity.this), ((VisaCheckoutApp) OnepocketPurchaseActivity.this.getApplication()));
                        intent.putExtras(bundle);
                        startActivity(intent);
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
        if (isDestroyed()) {
            return;
        }

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        OneTransaction oneTransaction = getIntent().getParcelableExtra(OPKConstants.EXTRA_PAYMENT);
        summary = new PurchaseSummaryFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(OPKConstants.EXTRA_PAYMENT, oneTransaction);
        summary.setArguments(bundle);
        transaction.add(R.id.opk_top, summary);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {

    }

    //*******************PROPER METHODS*********************
    public void swap(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        if (fragment != null) {
            Fragment current = getFragmentManager().findFragmentById(R.id.opk_bottom);
            if (current == fragment) {
                transaction.remove(fragment).attach(fragment);
            } else {
                    transaction.replace(R.id.opk_bottom, fragment);
            }
        } else {
            if (summary.isHidden()) {
                transaction.show(summary);
            }else {
                transaction.replace(R.id.opk_bottom, summary);
            }
        }
        transaction.commit();

    }

    private void addFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment currentTop = getFragmentManager().findFragmentById(R.id.opk_top);
        transaction.hide(currentTop);

        transaction.add(R.id.opk_top, fragment, fragment.toString());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
