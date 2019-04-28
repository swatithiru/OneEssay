package com.example.oneessay;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EssayTopicsAdapter extends ArrayAdapter<Essay> {

    private final Context context;
    private List<Essay> values;
    private ArrayList<String> str = new ArrayList<String>();
    DatabaseReference essayTopicsRef;

    public static String selectedTopic = "None";

    public EssayTopicsAdapter(Context context, ArrayList<Essay> values) {
        super(context, R.layout.row_essaytopic, values);
        this.context = context;
        this.values = values;
    }

    TextView essayText;
    ImageView minus;
    View rowView;

    int loc = 0;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        rowView = convertView;
        rowView = (rowView == null) ? inflater.inflate(R.layout.row_essaytopic, parent, false) : rowView;

        essayText = (TextView) rowView.findViewById(R.id.essayrow_topic);
        minus = (ImageView) rowView.findViewById(R.id.minus);


        essayText.setText(values.get(position).getTopic());


        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View parentRow = (View) v.getParent();
                ListView lv = (ListView) parentRow.getParent();
                loc = lv.getPositionForView(parentRow);
                values.remove(loc);
                if(values.size()>0) {
                    LoginActivity.mRootRef.child("essay").child(values.get(loc).getId()).removeValue();
                }
                //essayTopicsRef.child("topic").setValue(essaytopic.getText().toString());
               // str.addAll(Arrays.asList(values));
                //str.remove(loc);
                //values =  str.toArray(new String[0]);
               // Arrays.asList(values).remove(loc);


            }
        });
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
