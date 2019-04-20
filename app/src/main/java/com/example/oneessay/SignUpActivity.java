package com.example.oneessay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        setContentView(R.layout.activity_sign_up);
        Spinner spinnerSelect=(Spinner)findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> arrayAdapter=ArrayAdapter.createFromResource(getApplicationContext(),R.array.Professor,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSelect.setAdapter(arrayAdapter);
        spinnerSelect.setOnItemSelectedListener(this);
        spinnerSelect.setSelection(0);

        // signUp();
    }

    private void signUp() {
        EditText name= (EditText)findViewById(R.id.Name);
        final EditText email= (EditText)findViewById(R.id.Email);
        EditText password= (EditText)findViewById(R.id.Password);
        Spinner spinner= (Spinner) findViewById(R.id.spinner);
        progressDialog.setMessage("You are Registering .....");
        progressDialog.show();
        System.out.print("User+pass" + email.getText().toString());
        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isComplete()) {

                   // Toast.makeText(getApplicationContext(), "Registered Succesfully " + email.getText().toString(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Intent in=new Intent(SignUpActivity.this,LoginActivity.class);
                    startActivity(in);

                } else {
                    Toast.makeText(SignUpActivity.this, "Could not Register Succesfully", Toast.LENGTH_SHORT).show();
                }
            }


        });
    }

    public void onClickRegister(View view) {
        EditText name= (EditText)findViewById(R.id.Name);
        final EditText email= (EditText)findViewById(R.id.Email);
        EditText password= (EditText)findViewById(R.id.Password);
       // Spinner spinner= (Spinner) findViewById(R.id.spinner);

        progressDialog.setMessage("You are Registering .....");
        progressDialog.show();
        System.out.print("User+pass" + email.getText().toString());
        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isComplete()) {

                    Toast.makeText(getApplicationContext(), "Registered Succesfully " + email.getText().toString(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Intent in=new Intent(SignUpActivity.this,LoginActivity.class);
                    startActivity(in);

                } else {
                    Toast.makeText(SignUpActivity.this, "Could not Register Succesfully", Toast.LENGTH_SHORT).show();
                }
            }


        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
