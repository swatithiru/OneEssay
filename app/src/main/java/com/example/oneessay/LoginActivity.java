package com.example.oneessay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
private Button login;
private TextView signUp;
private EditText User;
private EditText Password;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        setContentView(R.layout.activity_login);
        //loginPage();
        //signUpPage();
    }

    private void signUpPage() {
        signUp =(TextView)findViewById(R.id.signup);
        User=(EditText)findViewById(R.id.editText);
        Password=(EditText)findViewById(R.id.editText2);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //User = (EditText) findViewById(R.id.name);
                //Password = (EditText) findViewById(R.id.password);
                //final String user = User.getText().toString();
                //String password = Password.getText().toString();
                progressDialog.setMessage("You are Registering .....");
                progressDialog.show();
                System.out.print("User+pass" + User.getText().toString());


                //Toast.makeText(LoginActivity.this,"clicked",Toast.LENGTH_LONG).show();

            }
        });

    }

    private void loginPage() {
        login=(Button)findViewById(R.id.button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }


    public void onClick(View view) {
        Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
        startActivity(intent);
//        User = (EditText) findViewById(R.id.editText);
//        Password = (EditText) findViewById(R.id.editText2);
//        final String user = User.getText().toString();
//        String password = Password.getText().toString();
//        progressDialog.setMessage("You are Registering .....");
//        progressDialog.show();
//        System.out.print("User+pass" + User.getText().toString());
//        firebaseAuth.createUserWithEmailAndPassword(User.getText().toString(), Password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isComplete()) {
//
//                    Toast.makeText(getApplicationContext(), "Registered Succesfully " + User.getText().toString(), Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
//
//                } else {
//                    Toast.makeText(LoginActivity.this, "Could not Register Succesfully", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//
//        });
    }

    public void onClickLogin(View view) {
        login=(Button)findViewById(R.id.button);
        User = (EditText) findViewById(R.id.editText);
        Password = (EditText) findViewById(R.id.editText2);

        progressDialog.setMessage("Logging in");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(User.getText().toString(), Password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, ProfessorHomePageActivity.class));
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Could not Login Succesfully", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}
