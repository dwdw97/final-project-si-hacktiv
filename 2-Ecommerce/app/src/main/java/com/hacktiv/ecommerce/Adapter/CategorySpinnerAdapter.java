package com.hacktiv.ecommerce.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.hacktiv.ecommerce.R;

public class CategorySpinnerAdapter extends ArrayAdapter<String> {

    private String[] items;

    public CategorySpinnerAdapter(Context context, String[] items) {
        super(context, android.R.layout.simple_spinner_item, items);
        this.items = items;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        if(position==0) {
//            return initialSelection(true);
//        }
//        return getCustomView(position, convertView, parent);
        return initialSelection(true, position);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        if (position == 0) {
//            return initialSelection(false);
//        }
//        return getCustomView(position, convertView, parent);
        return initialSelection(false, position);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    private View initialSelection(boolean dropdown, int position) {
        // Just an example using a simple TextView. Create whatever default view
        // to suit your needs, inflating a separate layout if it's cleaner.
        TextView view = new TextView(getContext());
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.tahoma_regular);
        view.setTypeface(typeface);
        view.setTextSize(20);
        int spacing = getContext().getResources().getDimensionPixelSize(com.google.android.material.R.dimen.cardview_default_radius);
        view.setPadding(0, spacing, 0, spacing);
        if(position == 0) {
            view.setText(items[0]);
            view.setTextColor(ContextCompat.getColor(getContext(), R.color.text_hint));
        } else {
            view.setText(items[position]);
            view.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        }

        if (dropdown && position == 0) { // Hidden when the dropdown is opened
            view.setHeight(0);
        }

        return view;
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        // Distinguish "real" spinner items (that can be reused) from initial selection item
        View row = convertView != null && !(convertView instanceof TextView)
                ? convertView :
                LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);

        position = position - 1; // Adjust for initial selection item
        String item = getItem(position);
        // ... Resolve views & populate with data ...
        return row;
    }
}
