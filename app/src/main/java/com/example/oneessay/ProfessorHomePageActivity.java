package com.example.oneessay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ProfessorHomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_home_page);
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
