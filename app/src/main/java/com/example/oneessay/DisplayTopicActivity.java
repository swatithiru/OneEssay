package com.example.oneessay;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class DisplayTopicActivity extends AppCompatActivity {

    DatabaseReference essayTopicsRef;

    EditText essaytopic;
    ListView essayListView;

    int count = 100;

    Essay essay;

    List<String> essayList;
    EssayTopicsAdapter essayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_topic);

        essayListView = (ListView) findViewById(R.id.essaytopicsListView);

        essayList = new ArrayList<String>();

        updateList();

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    private void openDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayTopicActivity.this);

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.enter_topic, null);

        essaytopic = (EditText) view.findViewById(R.id.newessaytopic);

        builder.setView(view);
        builder.setTitle("Enter New Topic");
        builder.setCancelable(true);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                essayTopicsRef = LoginActivity.mRootRef.child("essay").child(""+count);

                essayTopicsRef.child("topic").setValue(essaytopic.getText().toString());
                essayTopicsRef.child("id").setValue(""+(count++));

                updateList();

                Toast.makeText(DisplayTopicActivity.this, "New Topic has been added", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }

    private void updateList()
    {
        LoginActivity.mRootRef.child("essay").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                essayList.clear();
                while(iterator.hasNext()){
                    DataSnapshot s = iterator.next();
                    essay = s.getValue(Essay.class);
                    essayList.add(essay.getTopic());
                }
                count = essayList.size() + 100;
                essayAdapter = new EssayTopicsAdapter(DisplayTopicActivity.this, essayList.toArray(new String[0]));
                essayListView.setAdapter(essayAdapter);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onResume() {
        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
        essayList.clear();
        super.onResume();
    }

    public void onClickEssay(View view) {
        Intent intent=new Intent(DisplayTopicActivity.this,MainActivity.class);
        startActivity(intent);
    }
}
