package com.example.oneessay;

import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.TextView;

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

    EssayActivity activity;

    LinearLayout container;

    TextView essaytopic, essaycontent, currentstudent, nextstudent, time, noactiveessay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        essaytopic = (TextView) findViewById(R.id.essaytopic);
        essaycontent = (TextView) findViewById(R.id.essaycontentText);
        currentstudent = (TextView) findViewById(R.id.currentstudent);
        nextstudent = (TextView) findViewById(R.id.nextStudent);
        time = (TextView) findViewById(R.id.time);

        noactiveessay = (TextView) findViewById(R.id.noactiveessay);
        container = (LinearLayout) findViewById(R.id.content_frame);

        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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

                if(activity != null)
                {
                    essaytopic.setText(activity.getEssaytopic());
                    essaycontent.setText(activity.getEssaycontent());
                    currentstudent.setText(activity.getCurrentstudent());
                    time.setText(activity.getTime());
                    nextstudent.setText(activity.getNextstudents().get(0));

                    noactiveessay.setVisibility(View.GONE);
                    container.setVisibility(View.VISIBLE);
                }
                else
                {
                    noactiveessay.setVisibility(View.VISIBLE);
                    container.setVisibility(View.GONE);

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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
