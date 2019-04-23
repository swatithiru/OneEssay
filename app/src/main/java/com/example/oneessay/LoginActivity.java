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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class LoginActivity extends AppCompatActivity {

    private EditText User;
    private EditText Password;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    public static DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    public static FirebaseUser currentUser;

    public static EssayActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        activity = null;

        LoginActivity.mRootRef.child("activity").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();

                while(iterator.hasNext()){
                    DataSnapshot s = iterator.next();
                    activity = s.getValue(EssayActivity.class);
                    if(activity.getStatus())
                        break;
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void onClickSignUp(View view) {
        Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
        startActivity(intent);
    }

    public void onClickLogin(View view) {

        User = (EditText) findViewById(R.id.editText);
        String user = User.getText().toString();
        Password = (EditText) findViewById(R.id.editText2);
        String password = Password.getText().toString();
        Boolean result = validateEmptyFields(user, password);

        if (result) {
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

                        if (user.getEmail().equals("p@gmail.com"))
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
        else
        {
            Toast.makeText(this,"The User Name and Password Fields cannot be empty",Toast.LENGTH_LONG).show();
        }
    }

    private boolean validateEmptyFields(String user, String password) {
        Boolean res = false;
         if(!(user.isEmpty() || password.isEmpty()))
        {
            res = true;
        }

        return res;
    }
}
