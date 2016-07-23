package com.allem.alleminmotion.visacheckout;

/**
 * Created by victor on 19/02/15.
 * com.allem.allemevent.fragactiv
 * Modified By: Sergio Farfán 01/Jun/2016
 */

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.allem.alleminmotion.visacheckout.async.AsyncSoapObject;
import com.allem.alleminmotion.visacheckout.async.AsyncTaskSoapObjectResultEvent;
import com.allem.alleminmotion.visacheckout.async.MyBus;
import com.allem.alleminmotion.visacheckout.models.AllemUser;
import com.allem.alleminmotion.visacheckout.models.CuentaCliente;
import com.allem.alleminmotion.visacheckout.models.CuentaClienteInfoAdicional;
import com.allem.alleminmotion.visacheckout.parsers.SoapObjectParsers;
import com.allem.alleminmotion.visacheckout.utils.Constants;
import com.allem.alleminmotion.visacheckout.utils.CustomizedTextView;
import com.allem.alleminmotion.visacheckout.utils.Util;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.otto.Subscribe;
import org.ksoap2.serialization.PropertyInfo;

import java.lang.reflect.Field;

public class LoginNewUser extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {

    //************GLOBAL ATTRIBUTES***************
    private final String TAG = "LoginNewUser", M_SELECTION_DIVIDER = "mSelectionDivider", HTTP_AGENT = "http.agent";
    private Context ctx;
    private ImageButton ib_showhidepass, ib_showhiderepass;
    private boolean passIsVisible = false, repassIsVisible = false;
    private EditText et_username, et_password, et_names, et_surname, et_mobile;
    private NumberPicker countryPicker, typeOfIdPicker;
    private CustomizedTextView btn_sendreg, btn_login;
    private ProgressBar pb_create;
    TextView textCountrySelected, txtTypeOfIdSelected;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    //************ OVERRIDE METHODS**************
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ctx = this;
        System.gc();
        MyBus.getInstance().register(this);
        super.setView(R.layout.fragment_login_newuser, this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "LoginNewUser Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.allem.alleminmotion.visacheckout/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "LoginNewUser Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.allem.alleminmotion.visacheckout/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    protected void onDestroy() {
        MyBus.getInstance().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void initViews(View root) {

        System.gc();
        ib_showhidepass = (ImageButton) root.findViewById(R.id.ib_showhide_pass);
        ib_showhiderepass = (ImageButton) root.findViewById(R.id.ib_showhide_repass);
        et_username = (EditText) root.findViewById(R.id.et_email);
        et_surname = (EditText) root.findViewById(R.id.et_surname);
        et_names = (EditText) root.findViewById(R.id.et_names);
        et_password = (EditText) root.findViewById(R.id.et_password);
        btn_sendreg = (CustomizedTextView) root.findViewById(R.id.btn_sendreg);
        btn_login = (CustomizedTextView) root.findViewById(R.id.btn_login_new);
        pb_create = (ProgressBar) root.findViewById(R.id.pb_create);
        et_mobile = (EditText) root.findViewById(R.id.et_mobile);
        textCountrySelected = (TextView) root.findViewById(R.id.et_country_mobile);
        txtTypeOfIdSelected = (TextView) root.findViewById(R.id.etTypeOfId);
        setListeners();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "Option: " + item.getItemId());
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Subscribe
    public void onAsyncTaskResult(AsyncTaskSoapObjectResultEvent event) {
        if (event.getCodeRequest() == Constants.ACTIVITY_LOGIN_NEW_USER) {
            setWaitinUI(false);
            Intent returnIntent = new Intent();
            if (event.getResult() != null) {
                AllemUser user = SoapObjectParsers.toAllemUser(event.getResult());
                ((VisaCheckoutApp) this.getApplication()).setIdSession(user.idSesion);
                ((VisaCheckoutApp) this.getApplication()).setIdCuenta(user.idCuenta);
                String name = user.email.substring(0, user.email.indexOf('@'));
                String domain = user.email.substring(user.email.indexOf('@') + 1, user.email.length()).replace(".", "");
                String channel = name + domain + user.idCuenta;
                Constants.saveUser(ctx, user, channel);
                ((VisaCheckoutApp) this.getApplication()).unSetParseChannels();
                ((VisaCheckoutApp) this.getApplication()).parseUser(user.email, channel);
                /*if(KeySaver.isExist(ctx,Constants.USER_PUSH)) ((AsobancariaApplication) this.getApplication()).unSetParseChannels();
                ((AsobancariaApplication)this.getApplication()).setParseChannel(channel);*/

                //Remove all views from layout
               /* LinearLayout formLayout = (LinearLayout) findViewById(R.id.rl_body);
                formLayout.removeAllViews();
                /*//******Add all new views*****
                SuccessfulRegister successfulRegister = new SuccessfulRegister(getApplicationContext());
                formLayout.addView(successfulRegister);
                Log.e("Serfar Prueba", channel);*/


            //Temporary
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(ctx, event.getFaultString(), Toast.LENGTH_LONG).show();
                setResult(RESULT_CANCELED, returnIntent);
            }

        }
    }

    //************ PROPER METHODS**************

    private void setListeners() {

        btn_sendreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkFields()) {
                    //Generate dialog for accept
                     //   onAlertAcceptTermsAndConditions();
                    sendInfo();
                } else {
                    Toast.makeText(
                            ctx,
                            getString(R.string.form_errors),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, LoginActivity.class);
                startActivity(intent);
            }
        });

        et_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (et_username.getText().toString().length() < 7) {
                        showToast(getString(R.string.txt_email_should_have_7_characters));
                        et_username.setTextColor(Color.RED);
                        et_username.setHintTextColor(Color.RED);
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(et_username.getText().toString()).matches()) {
                        showToast(getString(R.string.txt_invalid_email));
                        et_username.setTextColor(Color.RED);
                        //et_username.setHintTextColor(Color.RED);
                    } else {
                        et_username.setTextColor(getResources().getColor(R.color.register_input_active));
                        //et_username.setHintTextColor(Color.BLACK);
                    }
                }
            }
        });

        et_names.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (et_names.getText().toString().length() < 2) {
                        showToast(getString(R.string.txt_name_incorrect));
                        et_names.setTextColor(Color.RED);
                        et_names.setHintTextColor(Color.RED);
                    } else {
                        et_names.setTextColor(getResources().getColor(R.color.register_input_active));
                        //et_names.setHintTextColor(Color.BLACK);
                    }
                }
            }
        });

        et_surname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (et_surname.getText().toString().length() < 2) {
                        showToast(getString(R.string.txt_last_name_incorrect));
                        et_surname.setTextColor(Color.RED);
                        et_surname.setHintTextColor(Color.RED);
                    } else {
                        et_surname.setTextColor(getResources().getColor(R.color.register_input_active));
                        //et_surname.setHintTextColor(Color.BLACK);
                    }
                }
            }
        });


        et_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (et_password.getText().toString().length() < 6) {
                        showToast(getString(R.string.txt_password_incorrectly_entered));
                        et_password.setTextColor(Color.RED);
                        et_password.setHintTextColor(Color.RED);
                    } else {
                        et_password.setTextColor(getResources().getColor(R.color.register_input_active));
                        //et_password.setHintTextColor(Color.BLACK);
                    }
                }
            }
        });

        textCountrySelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAlertSelectCountry();
            }
        });

        txtTypeOfIdSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAlertSelectTypeOfId();
            }
        });

    }

    private void onAlertAcceptTermsAndConditions() {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_alert_dialog_accept_terms_conditions);
        dialog.show();
        //Agree button
        CustomizedTextView btnOk = (CustomizedTextView) dialog.findViewById(R.id.btnAccept);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendInfo();
                dialog.dismiss();
            }
        });

        //Decline button
        CustomizedTextView btnDecline = (CustomizedTextView) dialog.findViewById(R.id.btnDecline);
        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Return to filled Fields
                dialog.dismiss();

            }
        });

        //Terms and conditions
        CustomizedTextView txtTermsAndConditions = (CustomizedTextView) dialog.findViewById(R.id.txtTermsAndConditions);
        txtTermsAndConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),TermsActivity.class);
                startActivity(i);
            }
        });

        //Privacy policy
        CustomizedTextView txtPrivacyPolicy = (CustomizedTextView) dialog.findViewById(R.id.txtPrivacyPolicy);
        txtPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),PoliticalActivity.class);
                startActivity(i);
            }
        });


    }

    //Called when the user information is valid
    private void sendInfo() {

        int greet = 1, rb_id;
        String celular = et_mobile.getText().toString();
        if (celular.length() < 1) celular = "-";//If goes empty
        CuentaClienteInfoAdicional ccia = new CuentaClienteInfoAdicional();
        ccia.setEmpresa("");
        ccia.setCargo("");
        ccia.setCelular(celular);
        ccia.setCiudad("");
        ccia.setClase("");
        //ccia.setCodigoPais("");

        CuentaCliente cuentaCliente = new CuentaCliente();
        cuentaCliente.setSegmento(String.valueOf(1));
        cuentaCliente.setSaludo(String.valueOf(greet));
        cuentaCliente.setNombre(et_names.getText().toString());
        cuentaCliente.setApellido(et_surname.getText().toString());
        cuentaCliente.setEmail(et_username.getText().toString());
        cuentaCliente.setPassword(et_password.getText().toString());
        cuentaCliente.setUserAgent(System.getProperty(HTTP_AGENT));
        cuentaCliente.setCuentaClienteInformacionAdicional(ccia);

        PropertyInfo property = new PropertyInfo();
        property.setName(CuentaCliente.PROPERTY);
        property.setValue(cuentaCliente);
        property.setType(cuentaCliente.getClass());

        if (Util.hasInternetConnectivity(ctx)) {
            setWaitinUI(true);
            AsyncSoapObject.getInstance(Constants.getWSDL(), Constants.NAMESPACE_ALLEM,
                    Constants.METHOD_CREAR_CUENTA, property, Constants.ACTIVITY_LOGIN_NEW_USER).execute();
        } else {
            Toast.makeText(ctx, getString(R.string.err_no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    private void setTextWatchers() {
        et_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() < 7) {
                    et_username.setTextColor(Color.RED);
                    et_username.setHintTextColor(Color.RED);
                } else {
                    et_username.setTextColor(Color.BLACK);
                    et_username.setHintTextColor(Color.BLACK);
                }
            }
        });
        et_names.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() < 2) {
                    et_names.setTextColor(Color.RED);
                    et_names.setHintTextColor(Color.RED);
                } else {
                    et_names.setTextColor(Color.BLACK);
                    et_names.setHintTextColor(Color.BLACK);
                }
            }
        });
        et_surname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                btn_sendreg.setEnabled(checkFields());
            }
        });
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                btn_sendreg.setEnabled(checkFields());
            }
        });

    }

    private void showToast(String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    private boolean checkFields() {
        boolean result = true;
        if (et_username.getText().toString().length() < 7) {
            et_username.setTextColor(Color.RED);
            et_username.setHintTextColor(Color.RED);
            result = false;
        }
        if (et_names.getText().toString().length() < 2) {
            et_names.setTextColor(Color.RED);
            et_names.setHintTextColor(Color.RED);
            result = false;
        }
        if (et_surname.getText().toString().length() < 2) {
            et_surname.setTextColor(Color.RED);
            et_surname.setHintTextColor(Color.RED);
            result = false;
        }
        if (et_password.getText().toString().length() < 6) {
            et_password.setTextColor(Color.RED);
            et_password.setHintTextColor(Color.RED);
            result = false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(et_username.getText().toString()).matches()) {
            et_username.setTextColor(Color.RED);
            et_username.setHintTextColor(Color.RED);
            result = false;
        }
        return result;
    }

    private void setWaitinUI(boolean b) {

        if (b) pb_create.setVisibility(View.VISIBLE);
        else pb_create.setVisibility(View.GONE);
        et_username.setEnabled(!b);
        et_names.setEnabled(!b);
        et_surname.setEnabled(!b);
        et_password.setEnabled(!b);
        btn_sendreg.setEnabled(!b);
        et_mobile.setEnabled(!b);
    }

    private void onAlertSelectCountry() {

        Log.e("Bug 1", "Entro");
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_country_selector_picker_dialog);
        dialog.show();
        countryPicker = new NumberPicker(this);
        countryPicker = (NumberPicker) dialog.findViewById(R.id.selectCountryPicker);
        countryPicker.setMinValue(0);
        countryPicker.setMaxValue(3);
        countryPicker.setDisplayedValues(new String[]{getString(R.string.COLOMBIA),
                getString(R.string.ECUADOR), getString(R.string.PERU), getString(R.string.UNITED_STATES)});
        setDividerColor(countryPicker);
        //TextViews (Cancel and Ok)
        TextView textCancel = (TextView) dialog.findViewById(R.id.textCancelDialogCountry);
        textCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        TextView textOk = (TextView) dialog.findViewById(R.id.textOkDialogCountry);
        textOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                updateTextOfCountry();
            }
        });
    }

    private void onAlertSelectTypeOfId() {//View view

        Log.e("Bug 1", "Entro");
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_select_type_of_id);
        dialog.show();
        typeOfIdPicker = new NumberPicker(this);
        typeOfIdPicker = (NumberPicker) dialog.findViewById(R.id.selectTypeOfIdPicker);
        typeOfIdPicker.setMinValue(0);
        typeOfIdPicker.setMaxValue(3);
        typeOfIdPicker.setDisplayedValues(new String[]{getString(R.string.txt_citizenship_card),
                getString(R.string.txt_Foreigner_ID), getString(R.string.txt_passport), getString(R.string.txt_identity_card)});
        setDividerColor(typeOfIdPicker);
        //TextViews (Cancel and Ok)
        TextView textCancel = (TextView) dialog.findViewById(R.id.textCancelDialogTypeOfId);
        textCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        TextView textOk = (TextView) dialog.findViewById(R.id.textOkDialogTypeOfId);
        textOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                updateTextOfTypeOfId();
            }
        });
    }

    private void updateTextOfCountry() {

        textCountrySelected = (TextView) findViewById(R.id.et_country_mobile);
        int country = countryPicker.getValue();

        switch (country) {
            case 0:
                textCountrySelected.setText(getString(R.string.COLOMBIA));
                break;
            case 1:
                textCountrySelected.setText(getString(R.string.ECUADOR));
                break;
            case 2:
                textCountrySelected.setText(getString(R.string.PERU));
                break;
            case 3:
                textCountrySelected.setText(getString(R.string.UNITED_STATES));
                break;
        }

    }

    private void updateTextOfTypeOfId() {

        txtTypeOfIdSelected = (TextView) findViewById(R.id.etTypeOfId);
        int typeOfID = typeOfIdPicker.getValue();

        switch (typeOfID) {
            case 0:
                txtTypeOfIdSelected.setText(getString(R.string.txt_citizenship_card));
                break;
            case 1:
                txtTypeOfIdSelected.setText(getString(R.string.txt_Foreigner_ID));
                break;
            case 2:
                txtTypeOfIdSelected.setText(getString(R.string.txt_passport));
                break;
            case 3:
                txtTypeOfIdSelected.setText(getString(R.string.txt_identity_card));
                break;
        }

    }

    private void setDividerColor(NumberPicker picker) {

        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals(M_SELECTION_DIVIDER)) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.dark_gray));
                    pf.set(picker, colorDrawable);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }

    }

    public void onMenu(View view) {
        animate();
    }

    public void onUp(View view) {
        onBackPressed();
    }

    //**************INNER CLASSES*****************

    private class SuccessfulRegister extends LinearLayout{

        public SuccessfulRegister(Context context) {

            super(context);
            //****************COMPLETE LAYOUT****************************
            setOrientation(LinearLayout.VERTICAL);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            setLayoutParams(layoutParams);
            setBackgroundColor(getResources().getColor(R.color.white));
            //TEXT REGISTER TITLE
            CustomizedTextView txtTitle = new CustomizedTextView(context);
            txtTitle.setText(R.string.title_register);
            addView(txtTitle);
            txtTitle.setTextColor(getResources().getColor(R.color.magenta));
            txtTitle.setGravity(Gravity.CENTER_HORIZONTAL);
            txtTitle.setTextSize(18);
            LinearLayout.LayoutParams layoutParams3 = (LinearLayout.LayoutParams)txtTitle.getLayoutParams();
            //int left, int top, int right, int bottom
            layoutParams3.setMargins(0, 40, 0, 40);
            txtTitle.setLayoutParams(layoutParams3);

            //IMAGE VIEW SEPARATOR
            ImageView imvSeparator = new ImageView(context);
            imvSeparator.setBackground(getResources().getDrawable(R.drawable.separator_medium_mag));
            imvSeparator.setImageResource(R.drawable.separator);
            addView(imvSeparator);
            LinearLayout.LayoutParams params6 = (LinearLayout.LayoutParams)imvSeparator.getLayoutParams();
            //int left, int top, int right, int bottom
            params6.setMargins(-6, 0, -6, -4);
            imvSeparator.setLayoutParams(params6);

            //****************CONTENT SUB-LAYOUT****************************
            LinearLayout layConfirmation = new LinearLayout(context);
            layConfirmation.setOrientation(LinearLayout.VERTICAL);
            LayoutParams layoutParams2 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            layoutParams2.gravity= Gravity.CENTER_HORIZONTAL;
            layConfirmation.setLayoutParams(layoutParams2);
            layConfirmation.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_successfull_account));
            addView(layConfirmation);

            //TEXT CONGRATULATIONS
            CustomizedTextView txtCongrat = new CustomizedTextView(context);
            txtCongrat.setTextColor(getResources().getColor(R.color.white));
            txtCongrat.setText(R.string.congratulations);
            txtCongrat.setTextSize(32);
            txtCongrat.setGravity(Gravity.CENTER_HORIZONTAL);
            layConfirmation.addView(txtCongrat);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)txtCongrat.getLayoutParams();
            //int left, int top, int right, int bottom
            params.setMargins(0, 100, 0, 0);
            txtCongrat.setLayoutParams(params);


            //IMAGE VIEW CHECK
           ImageView imvCheck = new ImageView(context);
           imvCheck.setBackground(getResources().getDrawable(R.drawable.approved));
           LayoutParams layoutParams1 = new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            layoutParams1.gravity= Gravity.CENTER_HORIZONTAL;
            layoutParams1.setMargins(0, 100, 0, 0);
            imvCheck.setLayoutParams(layoutParams1);
            layConfirmation.addView(imvCheck);

            //TEXT USER NAME
            CustomizedTextView txtUserName = new CustomizedTextView(context);
            txtUserName.setTextColor(getResources().getColor(R.color.white));
            AllemUser user = Constants.getUser(getApplicationContext());
            String value = user.nombre;
            txtUserName.setText(value);
            txtUserName.setGravity(Gravity.CENTER_HORIZONTAL);
            layConfirmation.addView(txtUserName);
            txtUserName.setTextSize(32);
            LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams)txtUserName.getLayoutParams();
            //int left, int top, int right, int bottom
            params1.setMargins(0, 100, 0, 0);
            txtUserName.setLayoutParams(params1);


            //TEXT ENJOY
            CustomizedTextView txtEnjoy = new CustomizedTextView(context);
            txtEnjoy.setTextColor(getResources().getColor(R.color.white));
            txtEnjoy.setText(R.string.you_can_enjoy_benefits);
            txtEnjoy.setGravity(Gravity.CENTER_HORIZONTAL);
            txtEnjoy.setTextSize(20);
            layConfirmation.addView(txtEnjoy);
            LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams)txtEnjoy.getLayoutParams();
            //int left, int top, int right, int bottom
            params2.setMargins(0, 100, 0, 0);
            txtUserName.setLayoutParams(params2);

            //TEXT ACTIVATE
            CustomizedTextView txtActivateNow = new CustomizedTextView(context);
            txtActivateNow.setTextColor(getResources().getColor(R.color.white));
            txtActivateNow.setText(R.string.TXT_ACTIVATE_NOW);
            txtActivateNow.setGravity(Gravity.CENTER_HORIZONTAL);
            txtActivateNow.setTextSize(18);
            layConfirmation.addView(txtActivateNow);
            LinearLayout.LayoutParams params3 = (LinearLayout.LayoutParams)txtActivateNow.getLayoutParams();
            //int left, int top, int right, int bottom
            params3.setMargins(0, 120, 0, 0);
            txtUserName.setLayoutParams(params3);

            //TEXT BUTTON LETS_TALK
            CustomizedTextView txtLetsTalk = new CustomizedTextView(context);
            txtLetsTalk.setTextColor(getResources().getColor(R.color.white));
            txtLetsTalk.setText(R.string.TXT_LETS_TALK);
            txtLetsTalk.setBackground(getResources().getDrawable(R.drawable.round_corner_transparent));
            txtLetsTalk.setGravity(Gravity.CENTER);
            layConfirmation.addView(txtLetsTalk);
            LinearLayout.LayoutParams params4 = (LinearLayout.LayoutParams)txtLetsTalk.getLayoutParams();
            //int left, int top, int right, int bottom
            params4.setMargins(32, 50, 32, 0);
            txtUserName.setLayoutParams(params4);
            txtLetsTalk.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(),CallActivity.class);
                    startActivity(intent);
                }
            });

            //TEXT LATER
            CustomizedTextView txtLater = new CustomizedTextView(context);
            txtLater.setTextColor(getResources().getColor(R.color.white));
            txtLater.setText(R.string.TXT_LATER);
            txtLater.setGravity(Gravity.CENTER_HORIZONTAL);
            layConfirmation.addView(txtLater);
            LinearLayout.LayoutParams params5 = (LinearLayout.LayoutParams)txtLater.getLayoutParams();
            //int left, int top, int right, int bottom
            params5.setMargins(0, 90, 0, 0);
            txtUserName.setLayoutParams(params5);
            txtLater.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

        }
    }

}