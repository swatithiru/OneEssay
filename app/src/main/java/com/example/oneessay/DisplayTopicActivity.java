package com.example.oneessay;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
    private Menu menu;
    EditText essaytopic;
    ListView essayListView;

    int count = 100;

    Essay essay;

    List<String> essayList;
    EssayTopicsAdapter essayAdapter;

    ArrayList<Student> studentObjectList;
    ArrayList<String> studentList;
    Student student;

    Student firstStudent;


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

    int activityCount = 1000;
    EssayActivity activity;

    int loopcounter = 0;

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

                if(essayList.size() > 0) {
                    count = essayList.size() + 100;

                    essayAdapter = new EssayTopicsAdapter(DisplayTopicActivity.this, essayList.toArray(new String[0]));
                    essayListView.setAdapter(essayAdapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        LoginActivity.mRootRef.child("student").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                studentList = new ArrayList<String>();

                studentObjectList = new ArrayList<Student>();

                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();

                while(iterator.hasNext()){
                    DataSnapshot s = iterator.next();
                    student = s.getValue(Student.class);
                    studentObjectList.add(student);
                }

                if(studentObjectList.size() > 0) {

                    Collections.shuffle(studentObjectList);

                    firstStudent = studentObjectList.get(0);

                    studentObjectList.remove(0);

                    Collections.sort(studentObjectList);

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        LoginActivity.mRootRef.child("activity").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                int i = 0;
                while(iterator.hasNext()){
                    DataSnapshot s = iterator.next();
                    activity = s.getValue(EssayActivity.class);
                    i++;
                }

                activityCount = i+1000;


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void onClickEssay(View view) {

        Essay ess = new Essay("3001","Fall of Fountain Mountain");

        if(!EssayTopicsAdapter.selectedTopic.equals("None")) {
            if(studentObjectList.size() > 0) {

                essayTopicsRef = LoginActivity.mRootRef.child("activity").child("" + activityCount);

                essayTopicsRef.child("essaytopic").setValue(EssayTopicsAdapter.selectedTopic);
                essayTopicsRef.child("id").setValue("" + activityCount);
                essayTopicsRef.child("essaycontent").setValue("");
                essayTopicsRef.child("status").setValue(Boolean.TRUE);
                essayTopicsRef.child("currentstudent").setValue(firstStudent);
                essayTopicsRef.child("nextstudents").setValue(studentObjectList);
                essayTopicsRef.child("time").setValue("300000");

                Intent intent = new Intent(DisplayTopicActivity.this, MainActivity.class);

                intent.putExtra("essayselected", ess);

                startActivity(intent);
            }
            else
                Toast.makeText(DisplayTopicActivity.this, "You don't have sufficient students to start the activity", Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(DisplayTopicActivity.this, "Please select a topic", Toast.LENGTH_LONG).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);

           /* SharedPreferences sharedPref = getSharedPreferences("Billing", Context.MODE_PRIVATE);
            /*if (sharedPref.getBoolean(getString(R.string.noads_purchased), false)) {
                item = menu.findItem(R.id.buy_noads);
                item.setVisible(false);
            }
            if (sharedPref.getBoolean(getString(R.string.bilingual_purchased), false)) {
                item = menu.findItem(R.id.buy_bilingual);
                item.setVisible(false);
                item = menu.findItem(R.id.action_purchase);
                item.setVisible(false);
            }*/
            this.menu = menu;
            restoreActionBar();
            return true;

        //return super.onCreateOptionsMenu(menu);
    }
    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("One Essay");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout) {

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(DisplayTopicActivity.this);

            builder.setTitle("ONE ESSAY");
            builder.setMessage("Are you sure you want to log out?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    Intent intent = new Intent(DisplayTopicActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    dialog.dismiss();
                }

            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing
                    dialog.dismiss();
                }
            });

            android.app.AlertDialog alert = builder.create();
            alert.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
