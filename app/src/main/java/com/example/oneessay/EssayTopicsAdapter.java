package com.example.oneessay;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

public class EssayTopicsAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] values;

    public static String selectedTopic = "None";

    public EssayTopicsAdapter(Context context,String[] values)
    {
        super(context, R.layout.row_essaytopic, values);
        this.context = context;
        this.values = values;
    }

    TextView essayText;
    View rowView;

    int loc=0;

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        rowView = convertView;
        rowView = (rowView == null) ? inflater.inflate(R.layout.row_essaytopic,parent,false) : rowView ;

        essayText = (TextView) rowView.findViewById(R.id.essayrow_topic);

        essayText.setText(values[position]);

        essayText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View parentRow = (View) view.getParent();
                ListView lv = (ListView) parentRow.getParent();
                View prevView = (View) lv.getChildAt(loc);

                TextView prevRow = (TextView) prevView.findViewById(R.id.essayrow_topic);

                prevRow.setBackgroundColor(Color.WHITE);
                prevRow.setTextColor(Color.BLACK);

                TextView temp = (TextView) view;

                temp.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                temp.setTextColor(Color.WHITE);
                selectedTopic = temp.getText().toString();

                loc = lv.getPositionForView(parentRow);

            }
        });


        notifyDataSetChanged();

        return rowView;
    }

}
