package com.roomies.simonrenoult.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.roomies.simonrenoult.models.Collocation;
import com.roomies.simonrenoult.roomies.R;

import java.util.ArrayList;

public class CollocationsListAdapter extends ArrayAdapter<Collocation> {
    public CollocationsListAdapter(Context context, ArrayList<Collocation> collocations) {
        super(context, 0, collocations);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Collocation collocation = getItem(position);
        
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_collocation, parent, false);
        }

        TextView nameView = (TextView) convertView.findViewById(R.id.collocation_name);
        TextView addressView = (TextView) convertView.findViewById(R.id.collocation_address);

        nameView.setText(collocation.getName());
        addressView.setText(collocation.getAddress());
        convertView.setTag(collocation.getUuid());

        return convertView;
    }
}
