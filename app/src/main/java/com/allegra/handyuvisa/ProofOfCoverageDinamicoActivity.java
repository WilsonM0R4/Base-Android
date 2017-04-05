package com.allegra.handyuvisa;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

public class ProofOfCoverageDinamicoActivity extends Fragment {
    private static final String TAG = "OPK_POC";

    private ProofOfCoverageDinamicoFragment proofOfCoverage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** pending for some changes **/
        //super.setView(R.layout.activity_proof_of_coverage_dinamico, this);

        //Fragment callbacks were moved to override method onViewCreated()
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.activity_proof_of_coverage_dinamico, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);



        initViews(view);

        //Fragment Callbacks
        RegisterCallback.registerNext(new NextHandler() {
            @Override
            public void handler(Fragment fragment) {
                if (fragment != null) {
                    addFragment(fragment);
                } else {
                    // clear the stack
                    // Log.d(TAG, "Clear the stack");
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
            }
        });

        RegisterCallback.registerMenu(new MenuHandler() {
            @Override
            public void handle() {
                ((MainActivity) getActivity()).animate();
            }
        });

        Log.e("PROOF","EntryCount es " +getChildFragmentManager().getBackStackEntryCount());

        RegisterCallback.registerBack(new BackHandler() {
            @Override
            public void handle() {
                //onUp(null);
                Log.e("Proof", "back pressed");
                //((MainActivity) getActivity()).replaceLayout(new MyAccountMenuActivity(), true);
                if(getChildFragmentManager().getBackStackEntryCount() > 0){
                    Log.e("THistory", "es " + getChildFragmentManager().getBackStackEntryCount());
                    getChildFragmentManager().popBackStack();
                    getChildFragmentManager().beginTransaction().commit();
                }else{
                    Log.e("THistory", "es " + getChildFragmentManager().getBackStackEntryCount());
                    ((FragmentMain) getParentFragment()).onBack();//replaceLayout(new MyAccountMenuActivity(), true);
                }


            }
        });

    }

    public void initViews(View root) {

        ((FragmentMain) getParentFragment()).configToolbar(true, 0, "");

        if (checkConnectivity()) {
            checkLogin();
        } else {

            showProofOfCoverage();
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == MainActivity.RESULT_OK) {
            showProofOfCoverage();
        }
    }

    private boolean checkConnectivity() {
        if (Connectivity.isConnected(getActivity().getApplicationContext())
                || Connectivity.isConnectedWifi(getActivity().getApplicationContext())
                || Connectivity.isConnectedMobile(getActivity().getApplicationContext())) {
            return true;
        } else {
            Toast.makeText(getActivity().getApplicationContext(),
                    R.string.err_no_internet,
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /*@Override
    public void onBackPressed() {

    }*/

    private void addFragment(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        Fragment currentTop = getChildFragmentManager().findFragmentById(R.id.opk_top);
        transaction.hide(currentTop);

        transaction.add(R.id.opk_top, fragment, fragment.toString());
        transaction.addToBackStack(fragment.getTag());
        transaction.commit();

    }


    private boolean checkLogin() {

        if(((com.allegra.handyuvisa.VisaCheckoutApp) getActivity().getApplication()).getIdSession()==null){
            ((FragmentMain) getParentFragment()).replaceLayout(new LoginActivity(), false);
            //((MainActivity) getActivity())
            /*Intent i =new Intent(getActivity(), LoginActivity.class);
            this.startActivityForResult(i, Constants.ONE_POCKET_NEEDS_LOGIN);*/
            return false;
        } else {
            showProofOfCoverage();
            return true;
        }
    }

    public void showProofOfCoverage() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        Bundle bundle = Constants.createDataBundle(Constants.getUser(getActivity()), (com.allegra.handyuvisa.VisaCheckoutApp) getActivity().getApplication());
        proofOfCoverage = new ProofOfCoverageDinamicoFragment();
        proofOfCoverage.setArguments(bundle);
        transaction.add(R.id.opk_top, proofOfCoverage);
        transaction.commit();


    }

}
