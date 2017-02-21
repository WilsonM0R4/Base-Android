package com.allegra.handyuvisa.views;

import android.app.Dialog;
import android.content.Context;

import com.allegra.handyuvisa.R;
import com.allegra.handyuvisa.utils.CustomizedTextView;

/**
 * Created by gangchen on 2/13/17.
 */

//TODO: use theme to customize color scheme
public class NetworkIssueDialog extends Dialog {

    private CustomizedTextView errorTitle;
    private CustomizedTextView errorMessage;
    private CustomizedTextView errorCode;
    public CustomizedTextView btnOk;

    public NetworkIssueDialog(Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.dialog_issue_network);
        errorTitle = (CustomizedTextView) findViewById(R.id.titleNetworkIssueError);
        errorMessage = (CustomizedTextView) findViewById(R.id.txtNetworkIssueMessage);
        errorCode = (CustomizedTextView) findViewById(R.id.txtErrorCode);
        btnOk = (CustomizedTextView) findViewById(R.id.txtOK);
    }


    public NetworkIssueDialog(Context context, int themeResId, int errorTitleId) {
        this(context, themeResId);
        errorTitle.setText(getContext().getString(errorTitleId));
    }

    public NetworkIssueDialog(Context context, int themeResId, int errorTitleId, int errorMessageId) {
        this(context, themeResId, errorTitleId);
        errorMessage.setText(getContext().getString(errorMessageId));
    }

    public NetworkIssueDialog(Context context, int themeResId, int errorTitleId, int errorMessageId, int errorCodeId) {
        this(context, themeResId, errorTitleId, errorMessageId);
        errorCode.setText(getContext().getString(R.string.txt_error_code) + getContext().getString(errorCodeId));
    }
    public void setErrorTitle(int errorTitleId) {
        errorTitle.setText(getContext().getString(errorTitleId));
    }

    public void setErrorMessage(int errorMessageId) {
        errorMessage.setText(getContext().getString(errorMessageId));
    }

    public void setErrorCode(int errorCodeId) {
        errorCode.setText(getContext().getString(R.string.txt_error_code) + getContext().getString(errorCodeId));
    }

    public CustomizedTextView getButton() {
        return btnOk;
    }
}