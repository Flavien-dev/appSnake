package com.chapfla.appsnake;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CaseGVAdapter extends ArrayAdapter<CaseGridView> {
    ArrayList<CaseGridView> SnakeGamePosition;
    public CaseGVAdapter(@NonNull Context context, ArrayList<CaseGridView> snakeGame) {
        super(context, 0, snakeGame);
        SnakeGamePosition = snakeGame;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.card_item, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.iv_case_blanche);

        CaseGridView maCase = SnakeGamePosition.get(position);

        imageView.setImageResource(maCase.getMonImage());

        return convertView;
    }
}
