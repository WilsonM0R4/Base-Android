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
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
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
import com.allem.alleminmotion.visacheckout.utils.Util;
import com.squareup.otto.Subscribe;
import org.ksoap2.serialization.PropertyInfo;

public class LoginNewUser extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {


    private final String TAG = "LoginNewUser";
    private final String M_SELECTION_DIVIDER = "mSelectionDivider", HTTP_AGENT = "http.agent";
    private Context ctx;
    private ImageButton ib_showhidepass, ib_showhiderepass;
    private boolean passIsVisible = false, repassIsVisible = false;
    private EditText et_username, et_password, et_names, et_surname, et_mobile;
    private NumberPicker countryPicker;
    private com.allem.alleminmotion.visacheckout.utils.CustomizedTextView btn_sendreg, btn_login;
    private ProgressBar pb_create;
    TextView textCountrySelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ctx = this;
        MyBus.getInstance().register(this);

        super.setView(R.layout.fragment_login_newuser, this);
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
        btn_sendreg = (com.allem.alleminmotion.visacheckout.utils.CustomizedTextView) root.findViewById(R.id.btn_sendreg);
        btn_login = (com.allem.alleminmotion.visacheckout.utils.CustomizedTextView) root.findViewById(R.id.btn_login_new);
        pb_create = (ProgressBar) root.findViewById(R.id.pb_create);
        et_mobile = (EditText) root.findViewById(R.id.et_mobile);
        textCountrySelected = (TextView) root.findViewById(R.id.et_country_mobile);

        setListeners();
    }

    private void setListeners() {

        btn_sendreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkFields()) {
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
                Intent intent = new Intent(ctx,LoginActivity.class);
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
                    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(et_username.getText().toString()).matches()) {
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

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(et_username.getText().toString()).matches()) {
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
                Log.e("Prueba",channel);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(ctx, event.getFaultString(), Toast.LENGTH_LONG).show();
                setResult(RESULT_CANCELED, returnIntent);
            }

        }
    }

    void onAlertSelectCountry() {//View view

        Log.e("Bug 1", "Entro");
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_country_selector_picker_dialog);
        dialog.show();
        countryPicker = new NumberPicker(this);
        countryPicker = (NumberPicker) dialog.findViewById(R.id.selectCountryPicker);
        countryPicker.setMinValue(0);
        countryPicker.setMaxValue(3);
        countryPicker.setDisplayedValues(new String[]{getString(R.string.COLOMBIA), getString(R.string.ECUADOR), getString(R.string.PERU), getString(R.string.UNITED_STATES)});
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

    private void setDividerColor(NumberPicker picker) {

        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
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
}