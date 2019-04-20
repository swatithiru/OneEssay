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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private Button login;
    private TextView signUp;
    private EditText User;
    private EditText Password;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    Student loginstudent;

    public static DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    public static FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

    }

    public void onClickSignUp(View view) {
        Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
        startActivity(intent);
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

                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    LoginActivity.currentUser = user;

                    if(user.getEmail().equals("p@gmail.com"))
                        startActivity(new Intent(LoginActivity.this, ProfessorHomePageActivity.class));

                    else
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Could not Login Succesfully", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
