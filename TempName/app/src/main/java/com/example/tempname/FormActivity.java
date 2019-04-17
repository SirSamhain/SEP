package com.example.tempname;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FormActivity extends AppCompatActivity {

    //////declaring the firebase stuff :)
    private FirebaseAuth firebaseAuth;
    private FirebaseUser fbuser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    ///////The actual things on the string
    private Button buttonComplete;
    private Spinner spinnerFt;
    private Spinner spinnerInches;
    private EditText editTextCurrentWeight;
    private EditText editTextGoalWeight;

    ///////misc'
    private ArrayList<String> ft;
    private ArrayList<String> inches;
    private ArrayAdapter<String> adapterFt;
    private ArrayAdapter<String> adapterInches;
    private int ftpos = 0;
    private int inpos = 0;
    private Users user;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        /////////////////////////INITIALIZING
        firebaseAuth          = FirebaseAuth.getInstance();
        fbuser                = firebaseAuth.getCurrentUser();
        firebaseDatabase      = FirebaseDatabase.getInstance();
        databaseReference     = firebaseDatabase.getReference();
        buttonComplete        = (Button)   findViewById(R.id.complete_form);
        editTextCurrentWeight = (EditText) findViewById(R.id.current_weight);
        editTextGoalWeight    = (EditText) findViewById(R.id.goal_weight);
        spinnerFt             = (Spinner)  findViewById(R.id.ft);
        spinnerInches         = (Spinner)  findViewById(R.id.inches);
        initHeight();
        adapterFt = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ft);
        adapterInches = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, inches);
        spinnerInches.setAdapter(adapterInches);
        spinnerFt.setAdapter(adapterFt);

        /////////////////////////////////////////


        buttonComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDatabase();
            }
        });

        spinnerFt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ftpos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ftpos = 0;
            }
        });

        spinnerInches.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                inpos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                inpos = 0;
            }
        });


    }

    private void toDatabase(){
        String heightFt = ft.get(ftpos);
        String heightIn = inches.get(inpos);
        String currentWeight = editTextCurrentWeight.getText().toString().trim();
        String goalWeight = editTextGoalWeight.getText().toString().trim();
        Boolean valid = true;

        if(TextUtils.isEmpty(currentWeight)){
            editTextCurrentWeight.setError("Enter a valid weight");
            valid = false;
        }
        if(TextUtils.isEmpty(goalWeight)){
            editTextGoalWeight.setError("Enter a valid weight");
            valid = false;
        }
        if(!valid){
            return;
        }

        //////////push the information to the database here //////////////////////////////////////

        user = new Users(heightFt, heightIn, currentWeight, goalWeight);
        databaseReference.child(fbuser.getUid()).child("personal_details").setValue(user)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(FormActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void initHeight(){
        ft = new ArrayList<String>();
        inches = new ArrayList<String>();
        for (int i = 1; i < 13; i++){
            inches.add(Integer.toString(i)+" in");
            if(i < 7){
                ft.add(Integer.toString(i)+" ft");
            }
        }
    }

}
