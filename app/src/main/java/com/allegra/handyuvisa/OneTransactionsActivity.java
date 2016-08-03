package com.allegra.handyuvisa;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.allegra.handyuvisa.models.AllemUser;
import com.allegra.handyuvisa.utils.Constants;
import com.allem.onepocket.BackHandler;
import com.allem.onepocket.MenuHandler;
import com.allem.onepocket.NextHandler;
import com.allem.onepocket.RegisterCallback;
import com.allem.onepocket.TransactionsFragment;
import com.allem.onepocket.model.OneTransaction;
import com.allem.onepocket.utils.OPKConstants;

import java.util.Stack;

public class OneTransactionsActivity extends FrontBackAnimate  implements FrontBackAnimate.InflateReadyListener {

    private TransactionsFragment oneTransctions;
    private Stack<Fragment> stack = new Stack<>();

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
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    while (!stack.isEmpty()) {
                        Fragment top = stack.pop();
                        transaction.remove(top);
                    }
                    transaction.show(oneTransctions);
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
                Fragment confirm = stack.get(size - 2);
                confirm.getArguments().putParcelable(OPKConstants.EXTRA_RESULT, bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.remove(currentTop);
                transaction.detach(confirm).attach(confirm).show(confirm);
                transaction.commit();

                stack.remove(size - 1);
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
                if (!stack.empty()) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    Fragment top = stack.pop();
                    transaction.remove(top);
                    if (!stack.empty()) {
                        transaction.show(stack.peek());
                    } else {
                        transaction.show(oneTransctions);
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
        checkLogin();

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

        stack.push(fragment);

    }


    private void checkLogin() {
        if(((VisaCheckoutApp)this.getApplication()).getIdSession()==null){
            Intent i =new Intent(OneTransactionsActivity.this, LoginActivity.class);
            this.startActivityForResult(i, Constants.ONE_POCKET_NEEDS_LOGIN);
            finish();
        }else {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            AllemUser user = Constants.getUser(this);
            VisaCheckoutApp app = (VisaCheckoutApp) getApplication();
            OneTransaction data = new OneTransaction();
            data.add("sessionId", app.getIdSession());
            data.add("first", user.nombre);
            data.add("last", user.apellido);
            data.add("userName", user.email);
            data.add("rawPassword", app.getRawPassword());
            data.add("idCuenta", Integer.toString(app.getIdCuenta()));
            bundle.putParcelable(OPKConstants.EXTRA_DATA, data);
            oneTransctions = new TransactionsFragment();
            oneTransctions.setArguments(bundle);
            transaction.add(R.id.opk_top, oneTransctions);
            transaction.commit();
        }
    }

}
