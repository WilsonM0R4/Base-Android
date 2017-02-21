package com.allegra.handyuvisa.views;

import android.app.Dialog;
import android.content.Context;

import com.allegra.handyuvisa.R;
import com.allegra.handyuvisa.utils.CustomizedTextView;

//TODO: use theme to customize color scheme
public class SystemErrorDialog extends Dialog {

    private CustomizedTextView errorTitle;
    private CustomizedTextView errorMessage;
    private CustomizedTextView errorCode;
    private CustomizedTextView btnOk;

    public SystemErrorDialog(Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.dialog_error_system);
        errorTitle = (CustomizedTextView) findViewById(R.id.titleSystemError);
        errorMessage = (CustomizedTextView) findViewById(R.id.txtSystemErrorMessage);
        errorCode = (CustomizedTextView) findViewById(R.id.txtErrorCode);
        btnOk = (CustomizedTextView) findViewById(R.id.txtOK);
        this.setCancelable(false);
    }

    public SystemErrorDialog(Context context, int themeResId, int errorTitleId) {
        this(context, themeResId);
        errorTitle.setText(getContext().getString(errorTitleId));
    }

    public SystemErrorDialog(Context context, int themeResId, int errorTitleId, int errorMessageId) {
        this(context, themeResId, errorTitleId);
        errorMessage.setText(getContext().getString(errorMessageId));
    }

    public SystemErrorDialog(Context context, int themeResId, int errorTitleId, int errorMessageId, int errorCodeId) {
        this(context, themeResId, errorTitleId, errorMessageId);
        errorCode.setText(getContext().getString(R.string.txt_error_code) + getContext().getString(errorCodeId));
    }

    public void setErrorTitle(int errorTitleId) {
        errorTitle.setText(errorTitleId);
    }

    public void setErrorMessage(int errorMessageId) {
        errorMessage.setText(errorMessageId);
    }

    public void setErrorCode(int errorCodeId) {
        errorCode.setText(getContext().getString(R.string.txt_error_code) + getContext().getString(errorCodeId));
    }

    public CustomizedTextView getButton() {
        return btnOk;
    }
}
