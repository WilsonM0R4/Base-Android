package com.allem.alleminmotion.visacheckout;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allem.alleminmotion.visacheckout.models.AllemUser;
import com.allem.alleminmotion.visacheckout.utils.Constants;
import com.allem.alleminmotion.visacheckout.utils.Util;
import com.allem.onepocket.model.OneTransaction;
import com.allem.onepocket.utils.OPKConstants;

/**
 * Created by lisachui on 12/1/15.
 */

//This class is one that consume a lot of memory, because the way is inflated the menu.
//Example: new MenuActivity (R.string.title_call, R.drawable.menu__onetouch__call, CallActivity.class)
public class BackFragment extends Fragment  {


    public interface MenuSelectListener {
        public void getStartActivity(Intent intent);
    }
    public MenuSelectListener menulistener;

    public class MenuActivity {

        private CharSequence title;
        private int iconResource;
        private Class<? extends Activity> activityClass;

        public MenuActivity(int titleResId, int iconResource, Class<? extends Activity> activityClass) {
            this.activityClass = activityClass;
            this.title = getResources().getString(titleResId);
            this.iconResource = iconResource;
        }

        @Override
        public String toString() {
            return title.toString();
        }

        public int getIconResource() { return iconResource; }

        public Class<? extends Activity> getActivityClass()  {
            return activityClass;
        }
    }

    public static MenuActivity[] activities;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        activities = new MenuActivity[] {
                new MenuActivity(R.string.title_my_account, R.drawable.menu__account,  MyAccountActivity.class),
                new MenuActivity(R.string.title_market_place, R.drawable.menu__mktplace, MarketPlaceActivity.class),
                new MenuActivity(R.string.title_flights, R.drawable.menu__flights, FlightsActivity.class),
                new MenuActivity(R.string.onepocket,R.drawable.menu__onepocket,McardActivity.class),
                new MenuActivity(R.string.title_hotels, R.drawable.menu__hotels, HotelsActivity.class),
                new MenuActivity(R.string.title_concierge, R.drawable.concierge5,ConciergeActivity.class),
                new MenuActivity(R.string.title_services, R.drawable.menu__services, HomeActivity_Handy.class),
                new MenuActivity(R.string.title_qr_scan, R.drawable.menu__qr__code, QRScanActivity.class),
                new MenuActivity(R.string.title_chat, R.drawable.menu__onetouch__chat, ChatActivity.class),
                new MenuActivity(R.string.title_call, R.drawable.menu__onetouch__call, CallActivity.class),
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_back, container, false);

    }

   /* public void sendToServices (){
        Intent intent = new Intent(getActivity(), ServiceActivity.class);
        getActivity().startActivity(intent);
    }*/

    public void sendToAccount(){
        Intent intent = new Intent(getActivity(),MyAccountMenuActivity.class);
        getActivity().startActivity(intent);
    }

    public void sendToQrScan(){
        Intent intent = new Intent(getActivity(),QRScanActivity.class);
        getActivity().startActivity(intent);
    }

    public void sendToMarket(){
        Intent intent = new Intent(getActivity(),MarketPlaceActivity.class);
        getActivity().startActivity(intent);
    }

    public void sendToChat(){
        Intent intent = new Intent(getActivity(),ChatActivity.class);
        getActivity().startActivity(intent);
    }

    public void sendToCall(){
        Intent intent = new Intent(getActivity(),CallActivity.class);
        getActivity().startActivity(intent);
    }

    public void sendToPocket(){
        Intent intent = new Intent(getActivity(), OnepocketContainerActivity.class);
        Bundle bundle = new Bundle();
        AllemUser user = Constants.getUser(getActivity());
        VisaCheckoutApp app = ((VisaCheckoutApp) getActivity().getApplication());

        if (user != null) {
            OneTransaction transaction = new OneTransaction(null,
                    null,
                    app.getIdSession(),
                    user.nombre,
                    user.apellido,
                    user.email,
                    app.getRawPassword(),
                    app.getIdCuenta()
            );
            bundle.putParcelable(OPKConstants.EXTRA_DATA, transaction);
            intent.putExtras(bundle);
        }
        getActivity().startActivity(intent);
    }

    public void sendToMcard(){
        Intent intent = new Intent(getActivity(),Mcardhtml.class);
        getActivity().startActivity(intent);
        /*
        *         Intent intent = new Intent(getActivity(),McardActivity.class);
        getActivity().startActivity(intent);*///MCARD NATIVE
    }

    public void sendToFlights(){
        Intent intent = new Intent(getActivity(),FlightsActivity.class);
        getActivity().startActivity(intent);
    }

    public void sendToHotels(){
        Intent intent = new Intent(getActivity(),HotelsActivity.class);
        getActivity().startActivity(intent);
    }

    public void sendToConcierge(){
        Intent intent = new Intent(getActivity(),ConciergeActivity.class);
        getActivity().startActivity(intent);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayout account_option = (LinearLayout) getView().findViewById(R.id.account_option);
        account_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToAccount();
            }
        });

        LinearLayout pocket_option = (LinearLayout) getView().findViewById(R.id.onepocket_option);
        pocket_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToPocket();
            }
        });

        LinearLayout mcard_option = (LinearLayout) getView().findViewById(R.id.mCard_option);//getActivity()
        mcard_option.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                sendToMcard();
            }
        });

        LinearLayout qr_option = (LinearLayout) getView().findViewById(R.id.scanqr_option);
        qr_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToQrScan();
            }
        });

        LinearLayout market_option = (LinearLayout) getView().findViewById(R.id.market_option);
        market_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToMarket();
            }
        });

        LinearLayout flights_option = (LinearLayout) getView().findViewById(R.id.flights_option);
        flights_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToFlights();
            }
        });

        LinearLayout hotels_option = (LinearLayout) getView().findViewById(R.id.hotels_option);
        hotels_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToHotels();
            }
        });

        LinearLayout concierge_option = (LinearLayout) getView().findViewById(R.id.concierge_option);
        concierge_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToConcierge();
            }
        });

        LinearLayout call_option = (LinearLayout) getView().findViewById(R.id.call_option);
        call_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToCall();
            }
        });
        LinearLayout chat_option = (LinearLayout) getView().findViewById(R.id.chat_option);
        chat_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToChat();
            }
        });

        LinearLayout restaurants_option = (LinearLayout) getView().findViewById(R.id.restaurants_option);
        restaurants_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView txthandy = new TextView(getActivity());
                txthandy.findViewById(R.id.restaurants_option);
                Intent intent = new Intent(getActivity(), HomeActivity_Handy.class);
                intent.putExtra("restaurants", txthandy.getText().toString());
                getActivity().startActivity(intent);
            }
        });

        LinearLayout taxi_option = (LinearLayout) getView().findViewById(R.id.taxi_option);
        taxi_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txthandy = new TextView(getActivity());
                txthandy.findViewById(R.id.taxi_option);
                Intent intent = new Intent(getActivity(), HomeActivity_Handy.class);
                intent.putExtra("taxi", txthandy.getText().toString());
                getActivity().startActivity(intent);
            }
        });


        LinearLayout service_option = (LinearLayout) getView().findViewById(R.id.services_option);
        service_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txthandy = new TextView(getActivity());
                txthandy.findViewById(R.id.services_option);
                Intent intent = new Intent(getActivity(), HomeActivity_Handy.class);
                intent.putExtra("services", txthandy.getText().toString());
                getActivity().startActivity(intent);
            }
        });


      /*  ListView mainMenu = (ListView)(getActivity().findViewById(R.id.main_menu));
        mainMenu.setScrollbarFadingEnabled(false);
        mainMenu.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_LEFT);
        mainMenu.setAdapter(new MainMenuAdapter(getActivity(), activities));
        mainMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (menulistener != null) {
                    menulistener.getStartActivity(new Intent(getActivity(), activities[position].activityClass));
                }
            }
        });*/

        if (Util.isAuthenticated(getActivity())) {
            AllemUser user = Constants.getUser(getActivity());
            TextView greeting = (TextView) getActivity().findViewById(R.id.tv_username_2);
            greeting.setVisibility(View.GONE);
            String format = getActivity().getResources().getString(R.string.txt_user_greeting);
            String value = String.format(format, user.nombre);
            greeting.setText(value);
        }

    }

}
