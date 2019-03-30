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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class GoalActivity extends AppCompatActivity {
    private ArrayList<String> test;
    private ArrayAdapter<String> adapter;
    private ListView goal_list;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser fbuser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /////INITIALIZING
        setContentView(R.layout.activity_goal);
        test = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, test);
        firebaseAuth = FirebaseAuth.getInstance();
        fbuser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        initGoals();

        //////////////////////

        goal_list = (ListView) findViewById(R.id.goal_list);
        goal_list.setAdapter(adapter);


        goal_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
            }
        });

        Button addGoalButton = (Button) findViewById(R.id.add_goal);
        addGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GoalActivity.this, AddGoalActivity.class);
                startActivity(intent);
            }
        });

        Button deleteGoalButton = (Button) findViewById(R.id.remove_goal);
        deleteGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!test.isEmpty()) {
                    removeFromDatabase();
                }
            }
        });
    }

    private void initGoals(){
        databaseReference = databaseReference.child(fbuser.getUid()).child("goals");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
               String name = dataSnapshot.child("goal").getValue().toString().trim();
               String desc = dataSnapshot.child("description").getValue().toString().trim();
               String goal = name + "\n" + desc;
               test.add(goal);
               adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                test.remove(pos);
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

    private void removeFromDatabase(){
        databaseReference = FirebaseDatabase.getInstance().getReference(fbuser.getUid()).child("goals");
        String target = test.get(pos);
        target = target.split("\n", 2)[0].toString().trim();
        System.out.println(target);
        Toast.makeText(this, "Removed: "+target, Toast.LENGTH_SHORT).show();
        databaseReference.child(target).removeValue();
        databaseReference = firebaseDatabase.getReference();
    }


}
