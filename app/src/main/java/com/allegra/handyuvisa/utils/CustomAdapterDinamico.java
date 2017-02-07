package com.allegra.handyuvisa.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.allegra.handyuvisa.ProofDinamico.model.Cobertura;
import com.allegra.handyuvisa.R;

import java.util.ArrayList;

/**
 * Created by jsandoval on 23/11/16.
 */

public class CustomAdapterDinamico extends ArrayAdapter<Cobertura> implements View.OnClickListener{

    private ArrayList<Cobertura> dataSet;
    Context mContext;
    String TAG = "CustomAdapterDinamico";

    private static class ViewHolder {
        TextView txttitlecobertura;
        TextView txtdescripcioncobertura;
    }

    public CustomAdapterDinamico(ArrayList<Cobertura> data, Context context) {
        super(context, R.layout.title_cobertura_valor, data);
        this.dataSet = data;
        this.mContext=context;

        //Check
       /* for (int i = 0; i < data.size(); i++){

        }*/

    }


    @Override
    public void onClick(View view) {

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Cobertura dataModeltest = getItem(position);
        ViewHolder viewHolder;
        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.title_cobertura_valor, parent, false);
            viewHolder.txttitlecobertura = (TextView) convertView.findViewById(R.id.name);
            viewHolder.txtdescripcioncobertura = (TextView)convertView.findViewById(R.id.type);

            result=convertView;

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.txttitlecobertura.setText(dataModeltest.getValorTexto());
        Log.d (TAG, "Nomb "+dataModeltest.getValorTexto());
        viewHolder.txtdescripcioncobertura.setText(dataModeltest.getNombre());
        Log.d (TAG, "Valor "+dataModeltest.getNombre());
        return convertView;
    }
}
