package com.allegra.handyuvisa;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.allegra.handyuvisa.async.AsyncSoapObject;
import com.allegra.handyuvisa.async.AsyncTaskSoapObjectResultEvent;
import com.allegra.handyuvisa.async.MyBus;
import com.allegra.handyuvisa.models.AllemUser;
import com.allegra.handyuvisa.models.CuentaClienteInfo;
import com.allegra.handyuvisa.models.CuentaClienteInfoAdicional;
import com.allegra.handyuvisa.parsers.SoapObjectParsers;
import com.allegra.handyuvisa.utils.Constants;
import com.allegra.handyuvisa.utils.CustomizedEditText;
import com.allegra.handyuvisa.utils.Util;
import com.squareup.otto.Subscribe;

import org.ksoap2.serialization.PropertyInfo;

import java.lang.reflect.Field;


public class EditProfileActivity extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {

    //*********GLOBAL ATTRIBUTES****************
    private final String M_SELECTION_DIVIDER = "mSelectionDivider";
    private String cel_code;
    CustomizedEditText txtName, txtLastName, txtMobile, txtEmail, txtPass, txtNewPass, txtNewPassConfirm, etNumberOfId;
    Button cancel, save;
    AllemUser user;
    private ProgressBar pb_create;
    private NumberPicker countryPicker, typeOfIdPicker;
    private Context ctx;
    TextView txtTypeOfIdSelected, txtSelectCountry;


    //*********OVERRIDE METHODS****************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setView(R.layout.activity_edit_profile, this);
        ctx = this;
        MyBus.getInstance().register(this);
    }

    @Override
    public void initViews(View root) {
        setViewElements(root);
        setListeners();
    }

    @Override
    protected void onDestroy() {
        MyBus.getInstance().unregister(this);
        super.onDestroy();
    }

    //*********PROPER METHODS****************

    private void setTextWatchers() {

        //********************DOCUMENT NUMBER*******************
        etNumberOfId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etNumberOfId.getText().toString().length() < 4) {
                    //Toast.makeText(getApplicationContext(),"PIlas con el tipo" ,Toast.LENGTH_LONG).show();
                    etNumberOfId.setHintTextColor(Color.RED);
                    etNumberOfId.setTextColor(Color.RED);
                } else{
                    etNumberOfId.setHintTextColor(Color.BLACK);
                    etNumberOfId.setTextColor(Color.BLACK);
                }

            }
        });

        //********************NAME*******************
        txtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() < 2) {
                    txtName.setTextColor(Color.RED);
                    txtName.setHintTextColor(Color.RED);
                } else {
                    txtName.setTextColor(Color.BLACK);
                    txtName.setHintTextColor(Color.BLACK);
                }
            }
        });

        //********************LAST NAME*******************
        txtLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.length() < 2) {
                    txtLastName.setTextColor(Color.RED);
                    txtLastName.setHintTextColor(Color.RED);
                } else {
                    txtLastName.setTextColor(Color.BLACK);
                    txtLastName.setHintTextColor(Color.BLACK);
                }
                //btn_sendreg.setEnabled(checkFields());
            }
        });

        //********************PHONE NUMBER*******************
        txtMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() < 3) {
                    txtMobile.setTextColor(Color.RED);
                    txtMobile.setHintTextColor(Color.RED);
                } else {
                    txtMobile.setTextColor(Color.BLACK);
                    txtMobile.setHintTextColor(Color.BLACK);
                }
            }
        });

        //******************** New PASSWORD*******************  txtNewPass, txtNewPassConfirm
        txtNewPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() < 6) {
                    txtNewPass.setTextColor(Color.RED);
                    txtNewPass.setHintTextColor(Color.RED);
                } else {
                    txtNewPass.setTextColor(Color.BLACK);
                    txtNewPass.setHintTextColor(Color.BLACK);
                }
                //btn_sendreg.setEnabled(checkFields());
            }
        });

        //********************Confirm PASSWORD*******************
        txtNewPassConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() < 6) {
                    txtNewPassConfirm.setTextColor(Color.RED);
                    txtNewPassConfirm.setHintTextColor(Color.RED);
                } else {
                    txtNewPassConfirm.setTextColor(Color.BLACK);
                    txtNewPassConfirm.setHintTextColor(Color.BLACK);
                }
                //btn_sendreg.setEnabled(checkFields());
            }
        });


    }

    //CUMPLE LA FUNCIÓN DEL MÉTODO initViews
    private void setViewElements(View root){

        etNumberOfId = (CustomizedEditText)root.findViewById(R.id.etIdNumber);
        txtTypeOfIdSelected = (TextView) root.findViewById(R.id.etTypeOfId);
        txtName= (CustomizedEditText) root.findViewById(R.id.et_names);
        txtLastName= (CustomizedEditText) root.findViewById(R.id.et_surname);
        txtMobile= (CustomizedEditText) root.findViewById(R.id.et_mobile);
        txtEmail= (CustomizedEditText)root.findViewById(R.id.et_email);
        cancel= (Button) root.findViewById(R.id.btn_ed_cancel);
        save= (Button)root.findViewById(R.id.btn_ed_save);
        txtPass =(CustomizedEditText) root.findViewById(R.id.et_current_password);
        txtNewPass=(CustomizedEditText) root.findViewById(R.id.et_new_password);
        pb_create = (ProgressBar) root.findViewById(R.id.pb_create);
        txtNewPassConfirm=(CustomizedEditText) root.findViewById(R.id.et_reppassword);
        txtSelectCountry = (TextView)root.findViewById(R.id.et_country_mobile);
        loadUserProfile();
        setTextWatchers();
    }

    //Carga y setea los datos del usuario
    private void loadUserProfile(){

        user = Constants.getUser(this);
        txtName.setText(user.nombre);
        txtLastName.setText(user.apellido);
        if(user.celular.length()>0) txtMobile.setText(user.celular);
        txtEmail.setText(user.email);
        txtTypeOfIdSelected.setText(getTypeOfIdForDisplay(user.idType));
        //Get celular_codigo from SharedPreferences
     /*   SharedPreferences prefs =
                getSharedPreferences("MisPreferencias", MODE_PRIVATE);

        cel_code = prefs.getString("celular_codigo", "+57");*/
        txtSelectCountry.setText(user.celular_codigo);
        etNumberOfId.setText(user.idNumber);

        /*CuentaClienteInfoAdicional ccia = new CuentaClienteInfoAdicional();
        ccia = user.*/

    }

    private String getTypeOfIdForDisplay(String idType){

        String strType = "";
        switch(idType) {

            case "1":
                return getResources().getString(R.string.txt_citizenship_card);
                //break;
            case "2":
                return getResources().getString(R.string.txt_Foreigner_ID);
            case "3":
                return getResources().getString(R.string.txt_nit);
            //break;
            case "4":
                return getResources().getString(R.string.txt_identity_card);
            case "5":
                return getResources().getString(R.string.txt_passport);
            //break;
            case "10":
                return getResources().getString(R.string.txt_nuip);
            case "9":
                return getResources().getString(R.string.txt_otro);
                //break;

        }


        return strType;

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
            case 4:
                txtTypeOfIdSelected.setText(getString(R.string.txt_nit));
                break;
            case 5:
                txtTypeOfIdSelected.setText(getString(R.string.txt_nuip));
                break;
            case 6:
                txtTypeOfIdSelected.setText(getString(R.string.txt_otro));
                break;

        }

    }

    private void onAlertSelectTypeOfId() {//View view

        Log.e("Bug 1", "Entro");
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_select_type_of_id);
        dialog.show();
        typeOfIdPicker = new NumberPicker(this);
        typeOfIdPicker = (NumberPicker) dialog.findViewById(R.id.selectTypeOfIdPicker);
        typeOfIdPicker.setMinValue(0);
        typeOfIdPicker.setMaxValue(6);
        typeOfIdPicker.setWrapSelectorWheel(false);
        typeOfIdPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        typeOfIdPicker.setDisplayedValues(new String[]{getString(R.string.txt_citizenship_card),
                getString(R.string.txt_Foreigner_ID), getString(R.string.txt_passport), getString(R.string.txt_identity_card), getString(R.string.txt_nit),
                getString(R.string.txt_nuip), getString(R.string.txt_otro)});
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

    private String getTypeOfIdToSend(String optionSelected){

        String typeOfIdToSend = "";
        if (optionSelected.equals(getString(R.string.txt_citizenship_card))){
            return "1";
        }else {
            if (optionSelected.equals(getString(R.string.txt_Foreigner_ID)))return "2";
            if (optionSelected.equals(getString(R.string.txt_nit)))return "3";
            if (optionSelected.equals(getString(R.string.txt_identity_card)))return "4";
            if (optionSelected.equals(getString(R.string.txt_passport)))return "5";
            if (optionSelected.equals(getString(R.string.txt_nuip)))return "10";
            if (optionSelected.equals(getString(R.string.txt_otro)))return "9";
        }
        return typeOfIdToSend;
    }

    //Validate and send new user information for ActualizarCuenta web service
    private void updateUser(){

        if(validateData()) {
            //Model class for basic Info
            CuentaClienteInfo client = new CuentaClienteInfo();
            client.setSaludo("1");
            client.setIdSesion(((VisaCheckoutApp) this.getApplication()).getIdSession());
            client.setNombre(txtName.getText().toString());
            client.setApellido(txtLastName.getText().toString());
            client.setTipoDocumento(getTypeOfIdToSend(txtTypeOfIdSelected.getText().toString()));
            client.setNumeroDocumento(etNumberOfId.getText().toString());
            client.setCurrentPassword(txtPass.getText().toString());
            if(txtNewPass.getText().toString().length()>0) {
                client.setPassword(txtNewPass.getText().toString());
            }
            //Model class for additional Info
            CuentaClienteInfoAdicional additionalInfo = new CuentaClienteInfoAdicional();
            additionalInfo.setCelular(txtMobile.getText().toString());
            additionalInfo.setEmpresa("");
            additionalInfo.setCargo("");
            additionalInfo.setCiudad("");
            additionalInfo.setClase("");
            additionalInfo.setCelularCodigo(txtSelectCountry.getText().toString());

            client.setCuentaClienteInformacionAdicional(additionalInfo);

            PropertyInfo property = new PropertyInfo();
            property.setName(CuentaClienteInfo.PROPERTY);
            property.setValue(client);
            property.setType(client.getClass());
            Log.d("IDSession",((VisaCheckoutApp)this.getApplication()).getIdSession());
            if (Util.hasInternetConnectivity(this)) {
                setWaitinUI(true);
                AsyncSoapObject.getInstance(Constants.getWSDL(), Constants.NAMESPACE_ALLEM,
                        Constants.METHOD_ACTUALIZAR_CUENTA, property, Constants.ACTIVITY_UPDATE_USER).execute();
            } else {
                Toast.makeText(this, R.string.err_no_internet, Toast.LENGTH_SHORT).show();
            }


        }else{
            Toast.makeText(
                    ctx,
                    getString(R.string.form_errors),
                    Toast.LENGTH_LONG).show();
        }

    }


    //Validate fields
    private boolean validateData(){

        boolean result=true;

        //****************NAME*************
        if(txtName.getText().toString().length()<2){
            txtName.setTextColor(Color.RED);
            txtName.setHintTextColor(Color.RED);
            result=false;
        }
        //****************LAST NAME*************
        if(txtLastName.getText().toString().length()<2){
            txtLastName.setTextColor(Color.RED);
            txtLastName.setHintTextColor(Color.RED);
            result=false;
        }

        //***************Type of ID***************
        if (txtTypeOfIdSelected.getText().toString().equals("")){
            //Toast.makeText(getApplicationContext(),"PIlas con el tipo" ,Toast.LENGTH_LONG).show();
            txtTypeOfIdSelected.setHintTextColor(Color.RED);
            result = false;
        }
        //***************Number of ID***************
        if (etNumberOfId.getText().toString().length() < 4){
            etNumberOfId.setHintTextColor(Color.RED);
            etNumberOfId.setTextColor(Color.RED);
            result = false;
        }
        //*************** Country CelularCode***************
        if (txtSelectCountry.getText().toString().equals("")){
            txtSelectCountry.setHintTextColor(Color.RED);
            result = false;
        }




        //****************PASSWORD *************
        if(txtPass.getText().toString().length()>0 || txtNewPass.getText().toString().length()>0 || txtNewPassConfirm.getText().toString().length()>0) {

            if (txtPass.getText().toString().length() < 6) {
                txtPass.setTextColor(Color.RED);
                txtPass.setHintTextColor(Color.RED);
                result = false;
            }

            if (txtNewPass.getText().toString().length() < 6) {
                txtNewPass.setTextColor(Color.RED);
                txtNewPass.setHintTextColor(Color.RED);
                result = false;
            }


            if (!txtNewPass.getText().toString().equals(txtNewPassConfirm.getText().toString())) {
                txtNewPass.setTextColor(Color.RED);
                txtNewPassConfirm.setTextColor(Color.RED);
                txtNewPass.setHintTextColor(Color.RED);
                txtNewPassConfirm.setHintTextColor(Color.RED);
                result = false;
            }

        }
        return result;
    }

    public void onMenu(View view) {
        animate();
    }

    private void setListeners(){
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //animate();
                onBackPressed();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });

        txtTypeOfIdSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {onAlertSelectTypeOfId();
            }
        });

        txtSelectCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAlertSelectCountry();
            }
        });

    }

    private void onAlertSelectCountry() {

        Log.e("Bug 1", "Entro");
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_country_selector_picker_dialog);
        dialog.show();
        countryPicker = new NumberPicker(this);
        countryPicker = (NumberPicker) dialog.findViewById(R.id.selectCountryPicker);
        countryPicker.setMinValue(0);
        countryPicker.setMaxValue(4);
        countryPicker.setWrapSelectorWheel(false);
        countryPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        countryPicker.setDisplayedValues(new String[]{getString(R.string.COLOMBIA),
                getString(R.string.ECUADOR), getString(R.string.PERU), getString(R.string.UNITED_STATES),getString(R.string.ARGENTINA)});
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

        //txtSelectCountry = (TextView) findViewById(R.id.et_country_mobile);
        int country = countryPicker.getValue();

        switch (country) {
            case 0:
                txtSelectCountry.setText(getString(R.string.PhoneCode_COLOMBIA));
                break;
            case 1:
                txtSelectCountry.setText(getString(R.string.PhoneCode_ECUADOR));
                break;
            case 2:
                txtSelectCountry.setText(getString(R.string.PhoneCode_PERU));
                break;
            case 3:
                txtSelectCountry.setText(getString(R.string.PhoneCode_USA));
                break;
            case 4:
                txtSelectCountry.setText(getString(R.string.PhoneCode_ARGENTINA));
        }

    }

    private void setWaitinUI(boolean b) {
        if (b) pb_create.setVisibility(View.VISIBLE);
        else pb_create.setVisibility(View.GONE);
        txtName.setEnabled(!b);
        txtLastName.setEnabled(!b);
        txtMobile.setEnabled(!b);
        txtPass.setEnabled(!b);
        txtNewPass.setEnabled(!b);
        txtNewPassConfirm.setEnabled(!b);
        save.setEnabled(!b);
        cancel.setEnabled(!b);
    }


    //Called when execute AsyncSoapObject request
    @Subscribe
    public void onAsyncTaskResult(AsyncTaskSoapObjectResultEvent event) {

        if (event.getCodeRequest()== Constants.ACTIVITY_UPDATE_USER){
            setWaitinUI(false);
            Intent returnIntent = new Intent();
            if (event.getResult()!=null){
                Log.e("Response",event.getResult().toString());
                AllemUser user = SoapObjectParsers.toAllemUser(event.getResult());
                AllemUser currentUser= Constants.getUser(EditProfileActivity.this);
                user.saludo= currentUser.saludo;
                user.idSesion= currentUser.idSesion;
                /*user.idType = currentUser.idType;
                user.idNumber = currentUser.idNumber;
                user.nombre = currentUser.nombre;
                user.apellido = currentUser.apellido;*/
                //Save in SharedPReferences TypeOfID and Number OfID

               Constants.saveUser(EditProfileActivity.this,user,currentUser.channel);

                new AlertDialog.Builder(ctx).setTitle(getString(R.string.txt_lbl_notification)).setMessage(getString(R.string.edit_successful)).setPositiveButton("ok", null).show();
            }else{
                Toast.makeText(EditProfileActivity.this, event.getFaultString(), Toast.LENGTH_LONG).show();
                setResult(RESULT_CANCELED, returnIntent);
            }

        }
    }

}
