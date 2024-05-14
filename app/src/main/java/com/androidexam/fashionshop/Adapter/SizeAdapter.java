package com.androidexam.fashionshop.Adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class SizeAdapter extends ArrayAdapter<String> {

    private OnSizeSelectedListener listener;

    public SizeAdapter(Context context, List<String> sizeList, OnSizeSelectedListener listener) {
        super(context, android.R.layout.simple_list_item_1, sizeList);
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        String size = getItem(position);

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(size);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onSizeSelected(size);
                }
            }
        });

        return convertView;
    }

    public interface OnSizeSelectedListener {
        void onSizeSelected(String selectedSize);
    }
}
