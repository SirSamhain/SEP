package com.example.tempname;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class WorkoutLogActivity extends AppCompatActivity {


    //BACKEND STUFF (FIREBASE)
    private FirebaseAuth firebaseAuth;
    private FirebaseUser fbuser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    //ELEMENTS ON THE PAGE
    private ListView listViewWorkouts;
    private Button buttonAddWorkout;
    private Button buttonRemoveWorkout;
    /////

    private ArrayList<String> workoutLogs;
    private ArrayAdapter<String> adapter;
    private int pos = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_log);

        ////INITIALIZING EVERYTHING
        firebaseAuth = FirebaseAuth.getInstance();
        fbuser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        workoutLogs = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, workoutLogs);
        listViewWorkouts = (ListView)findViewById(R.id.workouts);
        buttonAddWorkout = (Button) findViewById(R.id.add_workout);
        buttonRemoveWorkout = (Button) findViewById(R.id.remove_workout);
        initWorkoutLog();
        ////////////////////////////

        listViewWorkouts.setAdapter(adapter);


        listViewWorkouts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
            }
        });

        buttonAddWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WorkoutLogActivity.this, AddWorkoutActivity.class);
                startActivity(intent);
            }
        });

        buttonRemoveWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove();
            }
        });

    }


    private void initWorkoutLog(){
        databaseReference = databaseReference.child(fbuser.getUid()).child("workouts");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String name = dataSnapshot.child("workout_name").getValue().toString().trim();
                String type = dataSnapshot.child("workout_type").getValue().toString().trim();
                String time = dataSnapshot.child("workout_time").getValue().toString().trim();
                String workout = "Name: " +name+ "\nType: " + type + "\nTime: " + time;
                workoutLogs.add(workout);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                workoutLogs.remove(pos);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void remove(){
        databaseReference = FirebaseDatabase.getInstance().getReference(fbuser.getUid()).child("workouts");
        String target = workoutLogs.get(pos);
        target = target.split("\n", 3)[0].toString().trim();
        target = target.split(" ", 2)[1].toString().trim();
        System.out.println(target);
        Toast.makeText(this, "Removed: "+target, Toast.LENGTH_SHORT).show();
        databaseReference.child(target).removeValue();
        databaseReference = firebaseDatabase.getReference();

    }


}
