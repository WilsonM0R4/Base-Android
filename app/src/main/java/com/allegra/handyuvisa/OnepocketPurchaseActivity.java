package com.allegra.handyuvisa;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.allegra.handyuvisa.models.AllemUser;
import com.allegra.handyuvisa.utils.Constants;
import com.allem.onepocket.BackHandler;
import com.allem.onepocket.MenuHandler;
import com.allem.onepocket.NextHandler;
import com.allem.onepocket.PurchaseSummaryFragment;
import com.allem.onepocket.RegisterCallback;
import com.allem.onepocket.model.OneTransaction;
import com.allem.onepocket.utils.OPKConstants;

import java.util.Stack;

public class OnepocketPurchaseActivity extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {

    private static final String TAG = "OPK_purchaseActivity";
    private String sessionId;
    private PurchaseSummaryFragment summary;
    private Stack<Fragment> stack = new Stack<>();

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
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    while (!stack.isEmpty()) {
                        Fragment top = stack.pop();
                        transaction.remove(top);
                    }
                    transaction.show(summary);
                    transaction.commit();
                }

            }

            @Override
            public void handler2(Fragment fragment) {
                addFragment(fragment);
            }

            @Override
            public void returnResult(Bundle bundle) {

                int size = stack.size();
                Fragment currentTop = stack.get(size - 1);
                Fragment currentNext = stack.get(size - 2);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.remove(currentTop);
                transaction.detach(currentNext).attach(currentNext).show(currentNext);
                transaction.commit();
                stack.remove(size - 1);
            }

            @Override
            public void perform(int i, String data) {
                Intent result = new Intent();
                result.putExtra(OPKConstants.EXTRA_RESULT, data);
                OnepocketPurchaseActivity.this.setResult(Activity.RESULT_OK, result);
                OnepocketPurchaseActivity.this.finish();

                switch (i) {
                    case 0:
                        Log.e(TAG, "Invalid perform id: 0");
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
                if (!stack.empty()) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    Fragment top = stack.pop();
                    transaction.remove(top);
                    if (!stack.empty()) {
                        transaction.show(stack.peek());
                    } else {
                        transaction.show(summary);
                    }
                    transaction.commit();
                } else {
                    onUp(null);
                }
            }
        });
    }

    @Override
    public void initViews(View root) {

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

        for (Fragment f : stack) {
            transaction.hide(f);
        }
        transaction.add(R.id.opk_top, fragment, fragment.toString());
        transaction.commit();

        stack.add(fragment);

    }
}
