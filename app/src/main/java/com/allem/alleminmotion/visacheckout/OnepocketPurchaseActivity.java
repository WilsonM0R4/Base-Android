package com.allem.alleminmotion.visacheckout;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.allem.onepocket.BackHandler;
import com.allem.onepocket.MenuHandler;
import com.allem.onepocket.NextHandler;
import com.allem.onepocket.OneTransactionDescFragment;
import com.allem.onepocket.PurchaseSummaryFragment;
import com.allem.onepocket.RegisterCallback;
import com.allem.onepocket.model.OneTransaction;
import com.allem.onepocket.utils.OPKConstants;

import java.util.Stack;

public class OnepocketPurchaseActivity extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {

    private String sessionId;
    private PurchaseSummaryFragment summary;
    private Stack<Fragment> stack = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setView(R.layout.activity_onepocket_purchase, this);

        RegisterCallback.registerNext(new NextHandler() {
            @Override
            public void handler(Fragment fragment) {
                //swap(fragment);
                addFragment(fragment);

            }

            @Override
            public void handler2(Fragment fragment) {
                addFragment(fragment);
            }

            @Override
            public void returnResult(Bundle bundle) {
                int size = stack.size();
                Fragment currentTop = stack.get(size - 1);
                Fragment confirm = stack.get(size - 2);
                confirm.getArguments().putParcelable(OPKConstants.EXTRA_RESULT, bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.remove(currentTop);
                transaction.detach(confirm).attach(confirm).show(confirm);
                transaction.commit();

                stack.remove(size - 1);
            }

            @Override
            public void perform(int i) {
                switch (i) {
                    case 0:
                        OnepocketPurchaseActivity.this.finish();
                        Intent intent = new Intent(OnepocketPurchaseActivity.this, CallActivityMcard.class);
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
                //onUp(null);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                if (!stack.empty()) {
                    Fragment top = stack.pop();
                    transaction.remove(top);
                    if (!stack.empty()) {
                        transaction.show(stack.peek());
//                    } else {
//                        transaction.show(onePocket);
//                        transaction.show(summary);
                    }
                }

                transaction.commit();

            }
        });
    }

    @Override
    public void initViews(View root) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        OneTransaction oneTransaction = getIntent().getParcelableExtra(OPKConstants.EXTRA_PAYMENT);
        Fragment current = new OneTransactionDescFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(OPKConstants.EXTRA_PAYMENT, oneTransaction);
        current.setArguments(bundle);
        transaction.add(R.id.opk_top, current);

//        // initialize with summary
//        summary = new PurchaseSummaryFragment();
//        transaction.add(R.id.opk_bottom, summary);
//        transaction.hide(summary);
        transaction.commit();

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
