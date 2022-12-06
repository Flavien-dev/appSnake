package com.chapfla.appsnake;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chapfla.appsnake.Models.player;

import java.util.ArrayList;

public class PlayerAdapter extends ArrayAdapter<player> {
    public PlayerAdapter(@NonNull Context context, ArrayList<player> arrayList) {
        super(context, 0, arrayList);
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // convertView which is recyclable view
        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_row, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        player playerPosition = getItem(position);

        // then according to the position of the view assign the desired TextView 1 for the same
        TextView textView1 = currentItemView.findViewById(R.id.tv_name_player);
        textView1.setText(playerPosition.getNamePlayer());

        // then according to the position of the view assign the desired TextView 2 for the same
        TextView textView2 = currentItemView.findViewById(R.id.tv_score_player);
        textView2.setText(String.valueOf(playerPosition.getScore()));

        // then return the recyclable view
        return currentItemView;
    }
}
