package com.example.oneessay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddStudentActivity extends AppCompatActivity {
    Button btnAdd;
    EditText editName;
    EditText editEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        btnAdd=(Button)findViewById(R.id.btnAdd);
        editName=(EditText) findViewById(R.id.editName);
        editEmail=(EditText) findViewById(R.id.editEmail);
    }
    public void add(View v){
        String name=editName.getText().toString();
        String email=editName.getText().toString();
        if(name.equals("")||email.equals("")){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Invalid Credentials",
                    Toast.LENGTH_SHORT);

            toast.show();
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Student Added",
                    Toast.LENGTH_SHORT);
            toast.show();
            Intent intent = new Intent(this, ProfessorHomePageActivity.class);
            startActivity(intent);
        }
    }
}
