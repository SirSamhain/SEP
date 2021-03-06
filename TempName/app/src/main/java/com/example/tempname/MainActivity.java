package com.example.tempname;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

import static android.text.TextUtils.substring;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private String userEmail;
    private TextView textViewUserEmail;
    private TextView textViewUserName;
    private ArrayList<String> test;
    private TextView mainGoal;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private View newHeaderView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser fbuser;
    private static final String APP_ID = "ca-app-pub-7245659941748351~3408187119";
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, APP_ID);
        AdView adview = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adview.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7245659941748351/8917364782");
        mInterstitialAd.loadAd(adRequest);

        //THIS IS INITIALIZING EVERYTHING
        test = new ArrayList<String>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        newHeaderView = navigationView.getHeaderView(0);
        firebaseAuth = FirebaseAuth.getInstance();
        fbuser = firebaseAuth.getCurrentUser();
        firebaseDatabase = firebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        mainGoal = (TextView) findViewById(R.id.main_goal);
        if(mainGoal != null) {
            showPrimaryGoal();
        }
        //IF THE USER IS LOGGED OUT THEN IT RETURNS THEM TO THE LOGIN PAGE
        System.out.println("********************************************************************************************");
        System.out.println("********************************************************************************************");
        System.out.println("********************************************************************************************");
        System.out.println("user = " + firebaseAuth.getCurrentUser());
        if(firebaseAuth.getCurrentUser() == null){
            Toast.makeText(MainActivity.this, "Logged Out!", Toast.LENGTH_SHORT).show();
            finish();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        // THIS SETS THE USER INFORMATION THE DRAWER ON THE TOP LEFT
        textViewUserEmail=newHeaderView.findViewById(R.id.textViewUserEmail);
        userEmail = fbuser.getEmail();
        textViewUserEmail.setText(userEmail);
        textViewUserName = newHeaderView.findViewById(R.id.name);
        getName();



        // THIS SETS UP THE FLOATING MAIL ICON
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        //THIS TOGGLES THE NAVIGATION BAR TO BE CLOSED OR OPENED
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.app_form) {

            Intent intent = new Intent(MainActivity.this, FormActivity.class);
            startActivity(intent);

            // Handle the camera action
        } else if (id == R.id.app_goals) {

            Intent intent = new Intent(MainActivity.this, GoalActivity.class);
            startActivity(intent);

        } else if (id == R.id.app_logout) {

            //logout of the app
            firebaseAuth.signOut();
            finish();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);

        } else if (id == R.id.app_settings) {

            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);


        } else if (id == R.id.app_workout_log) {

            Intent intent = new Intent(MainActivity.this, WorkoutLogActivity.class);
            startActivity(intent);


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getName(){
        databaseReference = firebaseDatabase.getReference();
        databaseReference = databaseReference.child(fbuser.getUid()).child("user_info");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("name").exists()) {
                    String name = dataSnapshot.child("name").getValue().toString().trim();
                    textViewUserName.setText(name);
                }else {
                    textViewUserName.setText("no name");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showPrimaryGoal(){
        databaseReference = databaseReference.child(fbuser.getUid()).child("goals");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String name = dataSnapshot.child("goal").getValue().toString().trim();
                String desc = dataSnapshot.child("description").getValue().toString().trim();
                int primary = Integer.parseInt(dataSnapshot.child("primary").getValue().toString().trim());
                String localDate = dataSnapshot.child("date").toString().trim();
                localDate = localDate.split(",")[1].split("=")[1].trim();
                localDate = substring(localDate,0,localDate.length()-2);
                String goal = name + "\n" + desc + "\n" + primary + "\n" + localDate;
                test.add(goal);
                if(primary == 1 & !goal.isEmpty() & goal != null){
                    try {
                        set(name, desc, localDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void set(String name, String desc, String localDate) throws ParseException {

        LocalDate start = LocalDate.parse(localDate);
        Period period = Period.between(start, LocalDate.now());
        int diff = period.getDays();

        mainGoal.setText(name + "\n\n" + desc + "\n\nDays: " + diff);
    }


}
