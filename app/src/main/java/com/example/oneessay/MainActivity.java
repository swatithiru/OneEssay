package com.example.oneessay;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    EssayActivity activity, updateActivity;
    private ProgressDialog progressDialog;

    LinearLayout container;

    DatabaseReference essayActivityRef;

    TextView essaytopic, currentstudent, nextstudent, nextinline, time, noactiveessay;
    EditText essaycontent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(this);

        essaytopic = (TextView) findViewById(R.id.essaytopic);
        essaycontent = (EditText) findViewById(R.id.essaycontentText);
        currentstudent = (TextView) findViewById(R.id.currentstudent);
        nextstudent = (TextView) findViewById(R.id.nextStudent);
        nextinline = (TextView) findViewById(R.id.nextInLine);
        time = (TextView) findViewById(R.id.time);

        noactiveessay = (TextView) findViewById(R.id.noactiveessay);
        container = (LinearLayout) findViewById(R.id.content_frame);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        activity = LoginActivity.activity;

        if (activity != null) {
            essaytopic.setText(activity.getEssaytopic());
            essaycontent.setText(activity.getEssaycontent());
            currentstudent.setText(activity.getCurrentstudent().getName());
            time.setText(activity.getTime());
            if (activity.getNextstudents().size() > 0) {
                nextinline.setText("Next in Line: " + activity.getNextstudents().get(0).getName());
            } else {
                nextinline.setText("Next in Line: N/A ");

            }
            noactiveessay.setVisibility(View.GONE);
            container.setVisibility(View.VISIBLE);
            initMainActivity();
        } else {
            noactiveessay.setVisibility(View.VISIBLE);
            container.setVisibility(View.GONE);

        }
    }

    private void initMainActivity() {
        if(!LoginActivity.currentUser.getEmail().equals(activity.getCurrentstudent().getEmail()))
        {
            essaycontent.setEnabled(false);
            LoginActivity.mRootRef.child("activity").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();

                    while(iterator.hasNext()){
                        DataSnapshot s = iterator.next();
                        updateActivity = s.getValue(EssayActivity.class);
                        if(updateActivity.getStatus())
                            break;
                    }

                    time.setText(updateActivity.getTime());
                    essaycontent.setText(updateActivity.getEssaycontent());


                    if(activity.getCurrentstudent().getEmail().equals(updateActivity.getCurrentstudent().getEmail()))
                    {
                        Intent intent = new Intent(MainActivity.this,MainActivity.class);
                        startActivity(intent);
                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        else {

            essaycontent.setEnabled(true);

            new CountDownTimer(300000, 1000) {

                public void onTick(long millisUntilFinished) {
                    time.setText((millisUntilFinished / 1000) + "");

                    essayActivityRef = LoginActivity.mRootRef.child("activity")
                            .child(activity.getId());

                    essayActivityRef.child("time").setValue((millisUntilFinished / 1000) + "");
                    essayActivityRef.child("essaycontent").setValue(essaycontent.getText().toString());
                }

                public void onFinish() {
                    essaycontent.setEnabled(false);
                    essayActivityRef = LoginActivity.mRootRef.child("activity")
                            .child(activity.getId());
                    essayActivityRef.child("essaycontent").setValue(essaycontent.getText().toString());

                    if(activity.getNextstudents().size()>0) {
                        essayActivityRef.child("currentstudent").setValue(activity.getNextstudents().get(0));
                        activity.getNextstudents().remove(0);
                        essayActivityRef.child("nextstudents").setValue(activity.getNextstudents());

                        progressDialog.setMessage(" in");
                        progressDialog.show();

                        Intent intent = new Intent(MainActivity.this,MainActivity.class);
                        startActivity(intent);
                        progressDialog.dismiss();

                    }
                    else
                    {
                        essayActivityRef.child("status").setValue(Boolean.FALSE);
                        progressDialog.setMessage("Logging in");
                        progressDialog.show();
                        Intent intent = new Intent(MainActivity.this,MainActivity.class);
                        startActivity(intent);
                        progressDialog.dismiss();

                    }
                }
            }.start();
        }

    }


    // System.out.println(LoginActivity.currentUser.getEmail()+" "+activity.getCurrentstudent().getEmail());

    public void onClickSubmit(View view) {
        essaycontent.setEnabled(false);
        essayActivityRef = LoginActivity.mRootRef.child("activity")
                .child(activity.getId());
        essayActivityRef.child("essaycontent").setValue(essaycontent.getText().toString());
        if(activity.getNextstudents().size()>0) {
            essayActivityRef.child("currentstudent").setValue(activity.getNextstudents().get(0));
            activity.getNextstudents().remove(0);
            essayActivityRef.child("nextstudents").setValue(activity.getNextstudents());
            Intent intent = new Intent(MainActivity.this,MainActivity.class);
            startActivity(intent);
        }
        else
        {
            essayActivityRef.child("status").setValue(Boolean.FALSE);
            Intent intent = new Intent(MainActivity.this,MainActivity.class);
            startActivity(intent);

        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId())
        {
            case R.id.Profile:
                Intent intent=new Intent(MainActivity.this,ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.Status:
                Intent intentStatus=new Intent(MainActivity.this,StatusActivity.class);
                startActivity(intentStatus);
                break;
            case R.id.add:
                Intent intentAdd=new Intent(MainActivity.this,ProfessorHomePageActivity.class);
                startActivity(intentAdd);
                break;

            case R.id.logout:
                Intent intentLogout=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intentLogout);
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
