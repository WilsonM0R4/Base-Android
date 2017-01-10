package com.allegra.handyuvisa;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.allegra.handyuvisa.utils.Connectivity;
import com.allegra.handyuvisa.utils.Constants;
import com.allem.onepocket.BackHandler;
import com.allem.onepocket.MenuHandler;
import com.allem.onepocket.NextHandler;
import com.allem.onepocket.ProofOfCoverageDinamicoFragment;
import com.allem.onepocket.RegisterCallback;

/**
 * Created by gangchen on 1/5/17.
 */

public class ProofOfCoverageDinamicoActivity extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {
    private static final String TAG = "OPK_POC";

    private ProofOfCoverageDinamicoFragment proofOfCoverage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setView(R.layout.activity_proof_of_coverage_dinamico, this);

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
        if (checkConnectivity()) {
            checkLogin();
        } else {
            showProofOfCoverage();
        }


    }

    private boolean checkConnectivity() {
        if (Connectivity.isConnected(getApplicationContext()) || Connectivity.isConnectedWifi(getApplicationContext()) || Connectivity.isConnectedMobile(getApplicationContext())) {
            return true;
        } else {
            Toast.makeText(getApplicationContext(), R.string.err_no_internet, Toast.LENGTH_SHORT).show();
            return false;
        }
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


    private boolean checkLogin() {
        if(((VisaCheckoutApp)this.getApplication()).getIdSession()==null){
            Intent i =new Intent(ProofOfCoverageDinamicoActivity.this, LoginActivity.class);
            this.startActivityForResult(i, Constants.ONE_POCKET_NEEDS_LOGIN);
            finish();
            return false;
        } else {
            showProofOfCoverage();
            return true;
        }
    }

    public void showProofOfCoverage() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Bundle bundle = Constants.createDataBundle(Constants.getUser(this), (VisaCheckoutApp) getApplication());
        proofOfCoverage = new ProofOfCoverageDinamicoFragment();
        proofOfCoverage.setArguments(bundle);
        transaction.add(R.id.opk_top, proofOfCoverage);
        transaction.commit();
    }

}
