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
import android.widget.EditText;
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

    int count = 100;

    Essay essay;

    List<Essay> essayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_topic);


        essayList = new ArrayList<Essay>();

        LoginActivity.mRootRef.child("essay").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();

                while(iterator.hasNext()){
                    DataSnapshot s = iterator.next();
                    essay = s.getValue(Essay.class);
                    essayList.add(essay);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        count = essayList.size() + 100;

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

                Toast.makeText(DisplayTopicActivity.this, "Ok has been clicked", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }

    public void onClickEssay(View view) {
        Intent intent=new Intent(DisplayTopicActivity.this,MainActivity.class);
        startActivity(intent);
    }
}
