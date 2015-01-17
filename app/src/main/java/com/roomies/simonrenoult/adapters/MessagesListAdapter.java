package com.roomies.simonrenoult.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.roomies.simonrenoult.models.Message;
import com.roomies.simonrenoult.roomies.R;

import java.util.ArrayList;

public class MessagesListAdapter extends ArrayAdapter<Message> {
    public MessagesListAdapter(Context context, ArrayList<Message> messages) {
        super(context, 0, messages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_message, parent, false);
        }

        TextView contentView = (TextView) convertView.findViewById(R.id.message_content);
        TextView authorView = (TextView) convertView.findViewById(R.id.message_author);

        contentView.setText(message.getContent());
        authorView.setText(message.getRoomy().getUsername());

        return convertView;
    }
}