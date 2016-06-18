package com.allem.alleminmotion.visacheckout;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.allem.alleminmotion.visacheckout.utils.Constants;
import com.allem.onepocket.BackHandler;
import com.allem.onepocket.MenuHandler;
import com.allem.onepocket.NextHandler;
import com.allem.onepocket.OPKSummaryFragment;
import com.allem.onepocket.RegisterCallback;
import com.allem.onepocket.SummaryFragment;
import com.allem.onepocket.model.OneTransaction;
import com.allem.onepocket.utils.OPKConstants;

import java.util.ArrayList;
import java.util.Stack;


public class OnepocketContainerActivity extends FrontBackAnimate  implements FrontBackAnimate.InflateReadyListener {

    private SummaryFragment summary;
    private OPKSummaryFragment onePocket;
    private String sessionId;
    private Stack<Fragment> stack = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkLogin();

        super.setView(R.layout.activity_onepocket_container, this);

        RegisterCallback.registerNext(new NextHandler() {
            @Override
            public void handler(Fragment fragment) {
                swap(fragment);
            }

            @Override
            public void handler2(Fragment fragment) {
                addFragment(fragment);
            }

            @Override
            public void returnResult(Bundle bundle) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Fragment fragment;
                while (!stack.empty()) {
                    fragment = stack.pop();
                    transaction.remove(fragment);
                }

                transaction.remove(onePocket);
                transaction.remove(summary);
                transaction.commit();
                initViews(null);
            }

            @Override
            public void perform(int i) {
                switch (i) {
                    case 0:
                        Intent i1 = new Intent(OnepocketContainerActivity.this, CallActivityMcard.class);
                        startActivity(i1);
                        break;

                    case 1:
                        Intent i2 = new Intent(OnepocketContainerActivity.this, CallActivity.class);
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
                if (!onePocket.isHidden()) {
                    onUp(null);
                } else {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    if (!stack.empty()) {
                        Fragment top = stack.pop();
                        transaction.remove(top);
                        if (!stack.empty()) {
                            transaction.show(stack.peek());
                        } else {
                            transaction.show(onePocket);
                            transaction.show(summary);
                        }
                    }

                    transaction.commit();
                }
            }
        });
    }


    public void swap(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        if (fragment != null) {
            Fragment current = getFragmentManager().findFragmentById(R.id.opk_bottom);
            if (current == fragment) {
                transaction.detach(fragment).attach(fragment);
            } else {
                transaction.replace(R.id.opk_bottom, fragment);
            }
        } else {
            transaction.replace(R.id.opk_bottom, summary);
        }
        transaction.commit();

    }

    private void addFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment currentTop = getFragmentManager().findFragmentById(R.id.opk_top);
        Fragment currentBottom = getFragmentManager().findFragmentById(R.id.opk_bottom);
        transaction.hide(currentBottom).hide(currentTop);

        for (Fragment f : stack) {
            transaction.hide(f);
        }
        transaction.add(R.id.opk_top, fragment, fragment.toString());
        transaction.commit();

        stack.push(fragment);

    }

    @Override
    public void initViews(View root) {
        OneTransaction oneTransaction = getIntent().getParcelableExtra(OPKConstants.EXTRA_DATA);
        if (oneTransaction == null) {
            return;
        }

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        sessionId = ((VisaCheckoutApp)getApplication()).getIdSession();
        onePocket = new OPKSummaryFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(OPKConstants.EXTRA_DATA, oneTransaction);
        onePocket.setArguments(bundle);
        transaction.add(R.id.opk_top, onePocket);

        // initialize with summary
        summary = new SummaryFragment();
        transaction.add(R.id.opk_bottom, summary);
        transaction.commit();

    }


    private void checkLogin() {
        if(((VisaCheckoutApp)this.getApplication()).getIdSession()==null){
            Intent i =new Intent(OnepocketContainerActivity.this, LoginActivity.class);
            this.startActivityForResult(i, Constants.ONE_POCKET_NEEDS_LOGIN);
            finish();
        }
    }

}
