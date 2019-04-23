package com.example.oneessay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProfessorHomePageActivity extends AppCompatActivity {

    ListView studentListView;
    ListView activityListView;

    DatabaseReference studentsRef;

    Student student;
    List<String> studentList;

    EssayActivity activity;
    List<String> activityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_home_page);

        studentListView = (ListView) findViewById(R.id.studentlist);
        new Jolly(ProfessorHomePageActivity.this).execute(10);
        studentList = new ArrayList<String>();

        LoginActivity.mRootRef.child("student").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();

                studentList.clear();

                while(iterator.hasNext()){
                    DataSnapshot s = iterator.next();
                    student = s.getValue(Student.class);
                    studentList.add(student.getName());
                }

                ArrayAdapter adapter = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_list_item_1,studentList);

                if(studentList.size() > 0)
                    studentListView.setAdapter(adapter);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        activityListView = (ListView) findViewById(R.id.activitylist);

        activityList = new ArrayList<String>();


        LoginActivity.mRootRef.child("activity").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();

                activityList.clear();

                while(iterator.hasNext()){
                    DataSnapshot s = iterator.next();
                    activity = s.getValue(EssayActivity.class);
                    if(activity.getStatus())
                        activityList.add(activity.getEssaytopic());
                }

                ArrayAdapter adapter = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_list_item_1,activityList);

                if(activityList.size() > 0)
                    activityListView.setAdapter(adapter);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    public void studAdd(View v){
        Intent intent = new Intent(this, AddStudentActivity.class);
        startActivity(intent);
    }

    public void onClickTopic(View view) {
        Intent intent = new Intent(this, DisplayTopicActivity.class);
        startActivity(intent);
    }
}
