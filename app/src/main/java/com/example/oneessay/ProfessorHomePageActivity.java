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

    DatabaseReference studentsRef;

    Student student;
    List<String> studentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_home_page);

        studentListView = (ListView) findViewById(R.id.studentlist);

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

                studentListView.setAdapter(adapter);

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
