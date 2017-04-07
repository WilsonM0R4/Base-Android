package com.allegra.handyuvisa;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allegra.handyuvisa.utils.Constants;
import com.allegra.handyuvisa.utils.MenuCallback;
import com.allegra.handyuvisa.utils.NavigationCallback;
import com.allegra.handyuvisa.utils.OnBackCallback;
import com.allegra.handyuvisa.utils.WebNavigationCallBack;
import com.allegra.handyuvisa.utils.WebNavigationEnablingCallback;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Este fragmento contiene el toolbar y el content de la aplicacion
 */
public class FragmentMain extends Fragment implements NavigationCallback,
        WebNavigationEnablingCallback, MenuCallback, OnBackCallback{

    private static final String TAG = "FragmentMain";
    private static final int BUTTON_BACK = 1;
    private static final int ANY_BUTTON = 2;
    private Fragment currentFrontFragment; //will manage all fragments in the app


    RelativeLayout toolbar;
    ImageButton leftButton, leftButton2, rightButton, rightButton2;
    TextView tvTitle;
    ArrayList <Fragment> arrayFragments;
    Fragment stackFragment;
    WebNavigationCallBack callBack;
    FrameLayout content;
    ImageView divider;
    int buttonAction = ANY_BUTTON;

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        callBack = (WebFragment) stackFragment;

        toolbar = (RelativeLayout)view.findViewById(R.id.toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((MainActivity) getActivity()).state == MainActivity.menu_exposed){
                    ((MainActivity) getActivity()).animate();
                }

            }
        });

        content = (FrameLayout)view.findViewById(R.id.content_fragment);
        divider = (ImageView)view.findViewById(R.id.iv_header);
        leftButton = (ImageButton)view.findViewById(R.id.button_left);
        leftButton2 = (ImageButton)view.findViewById(R.id.button_left2);
        rightButton = (ImageButton)view.findViewById(R.id.button_right);
        rightButton2 = (ImageButton)view.findViewById(R.id.button_right2);
        tvTitle = (TextView)view.findViewById(R.id.title_toolbar);
        arrayFragments = new ArrayList<>();

        FrontFragment front = new FrontFragment();
        currentFrontFragment = front;

        getChildFragmentManager().beginTransaction()
                .add(R.id.content_fragment, front, MainActivity.TAG_FRONT)
                .commit();

        arrayFragments.add(currentFrontFragment);



    }

    //The menu button action
    public void menuAction(int imageResource){
        rightButton.setVisibility(View.VISIBLE);
        rightButton.setImageResource(imageResource);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).animate();
            }
        });
    }

    //The back button and cancel button action
    public void backCancelAction(int imageResource){
        leftButton.setVisibility(View.VISIBLE);
        leftButton.setImageResource(imageResource);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (arrayFragments.size() > 0) {
                    Log.e("backAction", "fragment is "+arrayFragments.get(arrayFragments.size()-1).getClass().getName());
                    buttonAction = BUTTON_BACK;
                    replaceLayout(arrayFragments
                            .get(arrayFragments.size()-1), true);
                    arrayFragments.remove(arrayFragments.size()-1);

                    buttonAction = ANY_BUTTON;

                } else {
                    Log.e("backAction","No fragments");
                }
            }
        });
    }

    //Action buttons web left side
    public void webActionLeft(int weBackAction, int webForwardAction ){
        leftButton.setVisibility(View.VISIBLE);
        leftButton.setImageResource(weBackAction);
        leftButton2.setVisibility(View.VISIBLE);
        leftButton2.setImageResource(webForwardAction);

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onWebBackPressed();
            }
        });

        leftButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onWebForwardPressed();
            }
        });
    }

    //Action buttons web right side
    public void webActionRight(int weBackAction, int webForwardAction ){
        rightButton.setVisibility(View.VISIBLE);
        rightButton.setImageResource(webForwardAction);
        rightButton2.setVisibility(View.VISIBLE);
        rightButton2.setImageResource(weBackAction);

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onWebForwardPressed();

            }
        });

        rightButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onWebBackPressed();
            }
        });
    }

    public void configToolbar(boolean isHomeView, int type, String title){

        if (isHomeView){
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) content.getLayoutParams();
            params.width = FrameLayout.LayoutParams.MATCH_PARENT;
            params.height = FrameLayout.LayoutParams.MATCH_PARENT;
            content.setLayoutParams(params);
            toolbar.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);
            return;
        }else{
            toolbar.setVisibility(View.VISIBLE);
            divider.setVisibility(View.VISIBLE);
            Log.e("Toolbar","Toolbar is " + tvTitle.getText());
        }

        switch (type){
            case Constants.TYPE_MENU:
                //Hide the other buttons
                rightButton2.setVisibility(View.GONE);
                leftButton.setVisibility(View.GONE);
                leftButton2.setVisibility(View.GONE);

                //rightButton
                menuAction(R.drawable.icon_menu);
                break;

            case Constants.TYPE_BACK:
                //Hide the other buttons
                rightButton.setVisibility(View.GONE);
                rightButton2.setVisibility(View.GONE);
                leftButton2.setVisibility(View.GONE);

                //leftButton
                backCancelAction(R.drawable.navigation__back);
                break;

            case Constants.TYPE_BACK_MENU:
                //Hide the other buttons
                leftButton2.setVisibility(View.GONE);
                rightButton2.setVisibility(View.GONE);

                //leftButton
                backCancelAction(R.drawable.navigation__back);
                //rightButton
                menuAction(R.drawable.icon_menu);
                break;

            case Constants.TYPE_WEB_MENU:
                //Hide the other buttons
                rightButton2.setVisibility(View.GONE);

                //leftButton and leftButton2
                webActionLeft(R.drawable.navigation__backurl_2, R.drawable.navigation__fwdurl);
                //rightButton
                menuAction(R.drawable.icon_menu);
                break;

            case Constants.TYPE_BACK_WEB:
                //Hide the other buttons
                leftButton2.setVisibility(View.GONE);

                //leftButton
                backCancelAction(R.drawable.navigation__back);
                //RightButton and rightButton2
                webActionRight(R.drawable.navigation__backurl_2, R.drawable.navigation__fwdurl);
                break;

            case Constants.TYPE_ICON_CANCEL:
                //Hide the other buttons
                leftButton2.setVisibility(View.GONE);
                rightButton.setVisibility(View.GONE);
                rightButton2.setVisibility(View.GONE);

                //leftButton
                backCancelAction(R.drawable.cancel_button);
                break;

            case Constants.TYPE_ICONCANCEL_MENU:
                //Hide the other buttons
                leftButton2.setVisibility(View.GONE);
                rightButton2.setVisibility(View.GONE);

                //leftButton
                backCancelAction(R.drawable.cancel_button);
                //rightButton
                menuAction(R.drawable.icon_menu);
                break;

            case Constants.TYPE_NO_BUTTON:
                //Hide the other buttons
                leftButton.setVisibility(View.GONE);
                leftButton2.setVisibility(View.GONE);
                rightButton.setVisibility(View.GONE);
                rightButton2.setVisibility(View.GONE);
                break;
        }

        tvTitle.setText(title);
        Log.e("Toolbar","Toolbar is " + tvTitle.getText());
    }

    public void replaceLayout(final Fragment fragment, boolean goBack){

        /*Log.e(TAG, "replaceLayout: currentFront: " + currentFrontFragment.toString()
                + " fragment: " + fragment.toString() + " isBack: " + goBack);*/

        Log.e(TAG,"before if");

        if(arrayFragments == null){
            arrayFragments = new ArrayList<>();
        }

        if(((MainActivity) getActivity()).state == MainActivity.menu_exposed){
            ((MainActivity) getActivity()).animate();
            Log.e(TAG, "menu is exposed");
            if(!isSameFragment(this.currentFrontFragment, fragment)){

                currentFrontFragment = fragment;

                arrayFragments.add(currentFrontFragment);
                Log.e("ArrayList"," El stack es " + arrayFragments);

                Log.e(TAG, "is same fragment");

                Log.e(TAG, "currentFront:"+currentFrontFragment.toString()
                        +" theOtherFragment: "+fragment);

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {

                        /*getChildFragmentManager().beginTransaction()
                                .setCustomAnimations(R.animator.slide_in_left,
                                        R.animator.slide_out_right)
                                .remove(getChildFragmentManager().findFragmentByTag(MainActivity.TAG_FRONT))
                                .add(R.id.content_fragment, fragment, MainActivity.TAG_FRONT)
                                .commit();*/

                        getChildFragmentManager().beginTransaction()
                                .setCustomAnimations(R.animator.slide_in_left,
                                        R.animator.slide_out_right)
                                .replace(R.id.content_fragment, fragment, MainActivity.TAG_FRONT)
                                .commit();

                        //
                    }
                }, 100);


            }

        } else {
            Log.e(TAG, "menu is hidden");
            Log.e(TAG, "is not same fragment");

            Log.e(TAG, "currentFront:"+currentFrontFragment.toString()
                    +" theOtherFragment: "+fragment);

            Log.e("ArrayList", " El stack es " + arrayFragments);

            int first_animation = R.animator.slide_in_left,
                    second_animation = R.animator.slide_out_right;

            if(goBack){
                first_animation = R.animator.slide_in_right;
                second_animation = R.animator.slide_out_left;
            }

            /*getChildFragmentManager().beginTransaction()
                    .setCustomAnimations(first_animation, second_animation)
                    .remove(getChildFragmentManager().findFragmentByTag(MainActivity.TAG_FRONT))
                    .add(R.id.content_fragment, fragment, MainActivity.TAG_FRONT)
                    .commit();*/

            getChildFragmentManager().beginTransaction()
                    .setCustomAnimations(first_animation, second_animation)
                    .replace(R.id.content_fragment, fragment, MainActivity.TAG_FRONT)
                    .commit();


            currentFrontFragment = fragment;

            if(buttonAction != BUTTON_BACK) {
                //if (arrayFragments.get(arrayFragments.size()-1) != currentFrontFragment)
                arrayFragments.add(currentFrontFragment);
            }

            Log.e(TAG, "currentFront:"+currentFrontFragment.toString()
                    +" theOtherFragment: "+fragment);

        }

        buttonAction = ANY_BUTTON;

    }

    public void animate(){
        ((MainActivity) getActivity()).animate();
    }

    private boolean isSameFragment(Fragment current, Fragment next) {
        String currentName = "", nextName = "";
        String[] groups = current.toString().split("\\{");
        String currentIndicator = "", secondIndicator = "";

        //work with tags

        if (groups.length > 0) {
            currentName = groups[0];
        }

        groups = next.toString().split("\\{");
        if (groups.length > 0) {
            nextName = groups[0];
        }

        /*if (currentName.equals(nextName)) {
            return true;
        } else {
            return false;
        }*/

        if(currentName.equals("WebFragment")){
            currentIndicator = current.getArguments().getString(WebFragment.WEB_TITLE);
        }

        if(nextName.equals("WebFragment")){
            secondIndicator = next.getArguments().getString(WebFragment.WEB_TITLE);
        }

        return (currentName.equals(nextName) && currentIndicator.equals(secondIndicator)) ;
    }

    @Override
    public void onBack(){
        if (arrayFragments.size() > 0) {
            Log.e("backAction", "fragment is "+arrayFragments.get(arrayFragments.size()-1).getClass().getName());
            Log.e("backAction", "stack is "+arrayFragments);
            buttonAction = BUTTON_BACK;
            arrayFragments.remove(arrayFragments.size()-1);
            replaceLayout(arrayFragments
                    .get(arrayFragments.size()-1), true);

            buttonAction = ANY_BUTTON;

        } else {
            Log.e("backAction","No fragments");
        }
    }

    public void restartStack(){
        arrayFragments = null;
    }

    //INTERFACE NavigationCallback
    @Override
    public void onForwardPressed(Fragment fragment) {
        arrayFragments.add(fragment);
    }

    //INTERFACE WebNavigationEnablingCallback
    @Override
    public void canGoBack(boolean canGoBack) {
        //set the enabled image to web back button
    }

    //INTERFACE WebNavigationEnablingCallback
    @Override
    public void canGoForward(boolean canGoForward) {
        //set the enabled image to web forward button
    }

    @Override
    public void goToView(Fragment fragment) {
        restartStack();
        replaceLayout(fragment, false);
    }

}
