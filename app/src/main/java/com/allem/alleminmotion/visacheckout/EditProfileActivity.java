package com.allem.alleminmotion.visacheckout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.allem.alleminmotion.visacheckout.async.AsyncSoapObject;
import com.allem.alleminmotion.visacheckout.async.AsyncTaskSoapObjectResultEvent;
import com.allem.alleminmotion.visacheckout.async.MyBus;
import com.allem.alleminmotion.visacheckout.models.AllemUser;
import com.allem.alleminmotion.visacheckout.models.CuentaClienteInfo;
import com.allem.alleminmotion.visacheckout.models.CuentaClienteInfoAdicional;
import com.allem.alleminmotion.visacheckout.parsers.SoapObjectParsers;
import com.allem.alleminmotion.visacheckout.utils.Constants;
import com.allem.alleminmotion.visacheckout.utils.Util;
import com.squareup.otto.Subscribe;

import org.ksoap2.serialization.PropertyInfo;

public class EditProfileActivity extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {

    EditText txtName, txtLastName, txtMobile, txtEmail, txtPass, txtNewPass, txtNewPassConfirm;
    Button cancel, save;
    AllemUser user;
    private ProgressBar pb_create;
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setView(R.layout.activity_edit_profile, this);
        ctx = this;
        MyBus.getInstance().register(this);

    }

    private void setViewElements(View root){
        txtName= (EditText) root.findViewById(R.id.et_names);
        txtLastName= (EditText) root.findViewById(R.id.et_surname);
        txtMobile= (EditText) root.findViewById(R.id.et_mobile);
        txtEmail= (EditText)root.findViewById(R.id.et_email);
        cancel= (Button) root.findViewById(R.id.btn_ed_cancel);
        save= (Button)root.findViewById(R.id.btn_ed_save);
        txtPass =(EditText) root.findViewById(R.id.et_current_password);
        txtNewPass=(EditText) root.findViewById(R.id.et_new_password);
        pb_create = (ProgressBar) root.findViewById(R.id.pb_create);
        txtNewPassConfirm=(EditText) root.findViewById(R.id.et_reppassword);
        loadUserProfile();
    }

    @Override
    protected void onDestroy() {
        MyBus.getInstance().unregister(this);
        super.onDestroy();
    }

    private void loadUserProfile(){
        user = Constants.getUser(this);
        txtName.setText(user.nombre);
        txtLastName.setText(user.apellido);
        if(user.celular.length()>0) txtMobile.setText(user.celular);
        txtEmail.setText(user.email);
    }

    private void updateUser(){

        if(validateData()) {
            CuentaClienteInfo client = new CuentaClienteInfo();
            CuentaClienteInfoAdicional additionalInfo = new CuentaClienteInfoAdicional();
            client.setSaludo("1");
            client.setCurrentPassword(txtPass.getText().toString());
            client.setIdSesion(((VisaCheckoutApp) this.getApplication()).getIdSession());
            client.setNombre(txtName.getText().toString());
            client.setApellido(txtLastName.getText().toString());
            if(txtNewPass.getText().toString().length()>0) {
                client.setPassword(txtNewPass.getText().toString());
            }
            additionalInfo.setCelular(txtMobile.getText().toString());
            additionalInfo.setEmpresa("");
            additionalInfo.setCargo("");
            additionalInfo.setCiudad("");
            additionalInfo.setClase("");

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

    private boolean validateData(){

        boolean result=true;

        if(txtName.getText().toString().length()<2){
            txtName.setTextColor(Color.RED);
            txtName.setHintTextColor(Color.RED);
            result=false;
        }
        if(txtLastName.getText().toString().length()<2){
            txtLastName.setTextColor(Color.RED);
            txtLastName.setHintTextColor(Color.RED);
            result=false;
        }

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
                animate();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });
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

    @Override
    public void initViews(View root) {
        setViewElements(root);
        setListeners();
    }

    @Subscribe
    public void onAsyncTaskResult(AsyncTaskSoapObjectResultEvent event) {

        if (event.getCodeRequest()== Constants.ACTIVITY_UPDATE_USER){
            setWaitinUI(false);
            Intent returnIntent = new Intent();
            if (event.getResult()!=null){
                AllemUser user = SoapObjectParsers.toAllemUser(event.getResult());
                AllemUser currentUser= Constants.getUser(EditProfileActivity.this);
                user.saludo= currentUser.saludo;
                user.idSesion= currentUser.idSesion;
               Constants.saveUser(EditProfileActivity.this,user,currentUser.channel);

                new AlertDialog.Builder(ctx).setTitle(getString(R.string.txt_lbl_notification)).setMessage(getString(R.string.edit_successful)).setPositiveButton("ok", null).show();
            }else{
                Toast.makeText(EditProfileActivity.this, event.getFaultString(), Toast.LENGTH_LONG).show();
                setResult(RESULT_CANCELED, returnIntent);
            }

        }
    }

}
