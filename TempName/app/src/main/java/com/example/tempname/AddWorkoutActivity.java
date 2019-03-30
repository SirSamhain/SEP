package com.example.tempname;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class AddWorkoutActivity extends AppCompatActivity {


    //BACKEND STUFF (FIREBASE)
    private FirebaseAuth firebaseAuth;
    private FirebaseUser fbuser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Workouts workout;
    private ProgressDialog progressDialog;

    //ELEMENTS ON THE PAGE
    private EditText workoutTime;
    private EditText workoutName;
    private EditText workoutType;
    private Button buttonStartWorkout;
    private Button buttonPauseWorkout;
    private Button buttonStopWorkout;
    private int delay = 1000;
    private int period = 1000;
    private static Timer timer;
    private static int interval=0;
    private String totalTime;
    private String wName;
    private String wType;
    /////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);

        ////INITIALIZING EVERYTHING
        firebaseAuth = FirebaseAuth.getInstance();
        fbuser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        buttonStartWorkout = (Button) findViewById(R.id.start_workout_button);
        buttonPauseWorkout = (Button) findViewById(R.id.pause_workout_button);
        buttonStopWorkout = (Button) findViewById(R.id.stop_workout_button);
        workoutTime = (EditText) findViewById(R.id.timer);
        workoutType = (EditText) findViewById(R.id.workout_type);
        workoutName = (EditText) findViewById(R.id.workout_name);
        progressDialog = new ProgressDialog(this);
        ////////////////////////////

        ///THIS STARTS THE WORKOUT TIMER IF ALL FIELDS ARE FILLED OUT
        buttonStartWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWorkingout();
            }
        });

        ///THIS STOPS THE TIMER AND SAVES STUFF TO THE DATABASE
        buttonStopWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopWorkingout();
            }
        });

        ///THIS PAUSES THE TIMER UNTIL YOU HIT START AGAIN
        buttonPauseWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseWorkingout();
            }
        });

    }

    private void startWorkingout(){

        wName = workoutName.getText().toString().trim();
        wType = workoutType.getText().toString().trim();

        if(TextUtils.isEmpty(wName)){
            workoutName.setError("Please enter a name for this workout");
            return;
        }
        if(TextUtils.isEmpty(wType)){
            workoutName.setError("Please enter a type for this workout");
            return;
        }

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                System.out.println(setInterval()+"*****************************************************************");
                String time = String.format("%02d:%02d", (interval % 3600) / 60, (interval % 60));
                workoutTime.setText(time);
                System.out.println(time);
            }
        }, delay, period);
    }

    private void stopWorkingout(){
        timer.cancel();
        totalTime = String.format("%02d:%02d", (interval % 3600) / 60, (interval % 60));
        workout = new Workouts(wName, wType, totalTime);
        toDatabase();
        interval = 0;
    }

    private void pauseWorkingout(){
        timer.cancel();
    }

    private static final int setInterval() {
        return ++interval;
    }

    private void toDatabase(){
        progressDialog.setMessage("Adding Goal....");
        progressDialog.show();
        databaseReference.child(fbuser.getUid()).child("workouts").child(wName).setValue(workout)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        Toast.makeText(AddWorkoutActivity.this, "Successfully added!", Toast.LENGTH_SHORT);
                        finish();
                        Intent intent = new Intent(AddWorkoutActivity.this, WorkoutLogActivity.class);
                        startActivity(intent);
                    }
                });
    }

}
