package com.allegra.handyuvisa;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allegra.handyuvisa.utils.Constants;
import com.allem.onepocket.BackHandler;
import com.allem.onepocket.MenuHandler;
import com.allem.onepocket.NextHandler;
import com.allem.onepocket.PurchaseSummaryFragment;
import com.allem.onepocket.RegisterCallback;
import com.allem.onepocket.model.OneTransaction;
import com.allem.onepocket.utils.OPKConstants;

import java.util.Stack;


public class OnepocketPurchaseActivity extends Fragment {

    private static final String TAG = "OPK_purchaseActivity";
    private PurchaseSummaryFragment summary;
    private Stack<Fragment> stack = new Stack<>();

    //**************OVERRIDE METHODS****************
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //super.setView(R.layout.activity_onepocket_purchase, this);




    }

    public void initViews(View root) {
        if (getActivity().isDestroyed()) {
            return;
        }

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        OneTransaction oneTransaction = getArguments().getParcelable(OPKConstants.EXTRA_PAYMENT);
        Log.e("OPK", "extra payment data is -- "+oneTransaction.toString());
        summary = new PurchaseSummaryFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(OPKConstants.EXTRA_PAYMENT, oneTransaction);
        summary.setArguments(bundle);
        transaction.add(R.id.opk_top, summary);
        transaction.commit();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.activity_onepocket_purchase, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        RegisterCallback.registerNext(new NextHandler() {
            @Override
            public void handler(Fragment fragment) {
                if (fragment != null) {
                    addFragment(fragment);
                } else {
                    // clear the stack
                    //Log.d(TAG, "Clear the stack");
                    FragmentManager manager = getChildFragmentManager();
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
                //OnepocketPurchaseActivity.this.setResult(Activity.RESULT_OK, result);

                switch (i) {
                    case 0:
                        //Log.e(TAG, "perform id: 0 - MCARD - Return from onepocket");
                        if (OPKConstants.oneTransaction.getType().equals("MCARD") && data.equals("onepocket_return")) {
                            //OnepocketPurchaseActivity.this.finish();
                            ((MainActivity) getActivity()).replaceLayout(new FrontFragment(), true);
                            Log.e("OPK", "perform if");
                        }
                        break;

                    case 1:
                        //Intent intent = new Intent(OnepocketPurchaseActivity.this, OnepocketContainerActivity.class);
                        Bundle bundle = Constants.createDataBundle(
                                Constants.getUser(getActivity()),
                                ((com.allegra.handyuvisa.VisaCheckoutApp)
                                        getActivity().getApplication()));

                        OnepocketContainerActivity fragmentOPKContainer = new OnepocketContainerActivity();

                        fragmentOPKContainer.setArguments(bundle);

                        ((MainActivity) getActivity()).replaceLayout(fragmentOPKContainer, false);
                        //intent.putExtras(bundle);
                        //startActivity(intent);
                        break;
                }
            }
        });

        RegisterCallback.registerMenu(new MenuHandler() {
            @Override
            public void handle() {
                ((MainActivity) getActivity()).animate();
            }
        });

        RegisterCallback.registerBack(new BackHandler() {
            @Override
            public void handle() {
                if (!stack.empty()) {
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    Fragment top = stack.pop();
                    transaction.remove(top);
                    if (!stack.empty()) {
                        transaction.show(stack.peek());
                    } else {
                        transaction.show(summary);
                    }
                    transaction.commit();
                } else {
                    //onUp(null);
                    ((MainActivity) getActivity()).replaceLayout(new FrontFragment(), true);
                }
            }
        });

    }

    /*public void initViews(View root) {
        if (getActivity().isDestroyed()) {
            return;
        }

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        OneTransaction oneTransaction = getArguments().getParcelable(OPKConstants.EXTRA_PAYMENT);
        summary = new PurchaseSummaryFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(OPKConstants.EXTRA_PAYMENT, oneTransaction);
        summary.setArguments(bundle);
        transaction.add(R.id.opk_top, summary);
        transaction.commit();
    }*/

    /*@Override
    public void onBackPressed() {

    }*/

    //*******************PROPER METHODS*********************
    public void swap(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        if (fragment != null) {
            Fragment current = getChildFragmentManager().findFragmentById(R.id.opk_bottom);
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
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        Fragment currentTop = getChildFragmentManager().findFragmentById(R.id.opk_top);
        transaction.hide(currentTop);

        for (Fragment f : stack) {
            transaction.hide(f);
        }
        transaction.add(R.id.opk_top, fragment, fragment.toString());
        transaction.commit();

        stack.push(fragment);

    }
}
