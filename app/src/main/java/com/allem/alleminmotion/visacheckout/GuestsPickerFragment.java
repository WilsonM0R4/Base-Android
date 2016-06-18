package com.allem.alleminmotion.visacheckout;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by lchui on 12/23/15.
 */
public class GuestsPickerFragment extends DialogFragment {

    private int numAdults;
    private int numInfants;
    private int numChildren;
    private OnGuestsSelectedListener listener;

    public interface OnGuestsSelectedListener {
        public void onGuestsSelected(int numAdults, int numChildren, int numInfants);
    }

    public GuestsPickerFragment() {
    }

    /** The system calls this to get the DialogFragment's layout, regardless
     of whether it's being displayed as a dialog or an embedded fragment. */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        return inflater.inflate(R.layout.dialog_passengers_picker, container, false);
    }

    /** The system calls this only when creating the layout in a dialog. */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        boolean includeInfant = false;
        if (getArguments() != null) {
            includeInfant = getArguments().getBoolean("type");
        }

        if (!includeInfant) {
            dialog.findViewById(R.id.ll_np_infants).setVisibility(View.GONE);
            dialog.findViewById(R.id.tv_infants).setVisibility(View.GONE);
        }

        final TextView addChildLabel = (TextView) dialog.findViewById(R.id.tv_children);
        addChildLabel.setText(R.string.txt_lbl_child_guests);

        final TextView adults = (TextView) dialog.findViewById(R.id.np_adults);
        adults.setText(Integer.toString(numAdults));

        final TextView children = (TextView) dialog.findViewById(R.id.np_children);
        children.setText(Integer.toString(numChildren));

        final ImageButton addAdult = (ImageButton) dialog.findViewById(R.id.ib_addAdults);
        addAdult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numAdults++;
                adults.setText(Integer.toString(numAdults));
            }
        });
        final ImageButton removeAdult = (ImageButton) dialog.findViewById(R.id.ib_removeAdults);
        removeAdult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numAdults > 0) {
                    numAdults--;
                    adults.setText(Integer.toString(numAdults));
                }
            }
        });

        final ImageButton addChild = (ImageButton) dialog.findViewById(R.id.ib_addChild);
        addChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numChildren++;
                children.setText(Integer.toString(numChildren));
            }
        });
        final ImageButton removeChild = (ImageButton) dialog.findViewById(R.id.ib_removeChild);
        removeChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numChildren > 0) {
                    numChildren--;
                    children.setText(Integer.toString(numChildren));
                }
            }
        });


        Button save = (Button) dialog.findViewById(R.id.btn_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onGuestsSelected(numAdults, numChildren, numInfants);
                dismiss();
            }
        });
        Button cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });



        return dialog;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnGuestsSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnGuestsSelectedListener");
        }
    }

}
