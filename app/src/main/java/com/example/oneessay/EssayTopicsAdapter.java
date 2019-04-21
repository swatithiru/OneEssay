package com.example.oneessay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

public class EssayTopicsAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] values;

    public EssayTopicsAdapter(Context context,String[] values)
    {
        super(context, R.layout.row_essaytopic, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.row_essaytopic, parent, false);

        TextView essayText = (TextView) rowView.findViewById(R.id.essayrow_topic);
        RadioButton radioButton = (RadioButton) rowView.findViewById(R.id.essayrow_select);

        essayText.setText(values[position]);

        notifyDataSetChanged();

        return rowView;
    }

}
