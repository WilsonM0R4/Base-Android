package com.allem.alleminmotion.visacheckout;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.allem.alleminmotion.visacheckout.models.AllemUser;
import com.allem.alleminmotion.visacheckout.utils.Constants;
import com.allem.onepocket.BackHandler;
import com.allem.onepocket.MenuHandler;
import com.allem.onepocket.NextHandler;
import com.allem.onepocket.OPKSummaryFragment;
import com.allem.onepocket.RegisterCallback;
import com.allem.onepocket.model.OneTransaction;
import com.allem.onepocket.utils.OPKConstants;

import java.util.Stack;


public class OnepocketContainerActivity extends FrontBackAnimate  implements FrontBackAnimate.InflateReadyListener {

    private static final int REQUEST_CALL = 1;
    private static final int REQUEST_MCARD = 2;

    private OPKSummaryFragment onePocket;
    private Stack<Fragment> stack = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkLogin();

        super.setView(R.layout.activity_onepocket_container, this);

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
                    transaction.show(onePocket);
                    transaction.commit();
                }
            }

            @Override
            public void handler2(Fragment fragment) {
                addFragment(fragment);
            }

            @Override
            public void returnResult (Bundle bundle) {
                int size = stack.size();
                Fragment currentTop = stack.get(size - 1);
                Fragment confirm = stack.get(size - 2);
                confirm.getArguments().putParcelable(OPKConstants.EXTRA_RESULT, bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.remove(currentTop);
                transaction.detach(confirm).attach(confirm).show(confirm);
                transaction.commit();

                stack.remove(size - 1);
            }          @Override
            public void perform(int i, String onePocketmessage) {
                switch (i) {
                    case 0:
                        Intent i1 = new Intent(OnepocketContainerActivity.this, OnepocketPurchaseActivity.class);

                        Bundle bundle = new Bundle();
                        AllemUser user = Constants.getUser(OnepocketContainerActivity.this);
                        VisaCheckoutApp app = (VisaCheckoutApp) getApplication();
                        OneTransaction transaction = new OneTransaction();
                        transaction.add("jsonPayment", onePocketmessage);
                        transaction.add("type", OPKConstants.TYPE_MCARD);
                        transaction.add("sessionId", app.getIdSession());
                        transaction.add("first", user.nombre);
                        transaction.add("last", user.apellido);
                        transaction.add("userName", user.email);
                        transaction.add("rawPassword", app.getRawPassword());
                        transaction.add("idCuenta", Integer.toString(app.getIdCuenta()));

                        bundle.putParcelable(OPKConstants.EXTRA_PAYMENT, transaction);
                        i1.putExtras(bundle);
                        startActivityForResult(i1, 2);
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
                if (!stack.empty()) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    Fragment top = stack.pop();
                    transaction.remove(top);
                    if (!stack.empty()) {
                        transaction.show(stack.peek());
                    } else {
                        transaction.show(onePocket);
                    }
                    transaction.commit();
                } else {
                    onUp(null);
                }

            }
        });
    }


//    public void swap(Fragment fragment) {
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//
//        if (fragment != null) {
//            Fragment current = getFragmentManager().findFragmentById(R.id.opk_bottom);
//            if (current == fragment) {
//                transaction.detach(fragment).attach(fragment);
//            } else {
//                transaction.replace(R.id.opk_bottom, fragment);
//            }
//        } else {
//            transaction.replace(R.id.opk_bottom, summary);
//        }
//        transaction.commit();
//
//    }

    private void addFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment currentTop = getFragmentManager().findFragmentById(R.id.opk_top);
        transaction.hide(currentTop);

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
        onePocket = new OPKSummaryFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(OPKConstants.EXTRA_DATA, oneTransaction);
        onePocket.setArguments(bundle);
        transaction.add(R.id.opk_top, onePocket);

//        // initialize with summary
//        summary = new SummaryFragment();
//        transaction.add(R.id.opk_bottom, summary);
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
