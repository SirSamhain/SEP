package com.example.tempname;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FormActivity extends AppCompatActivity {

    //////declaring the firebase stuff :)
    private FirebaseAuth firebaseAuth;
    private FirebaseUser fbuser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    ///////The actual things on the string
    private Button buttonComplete;
    private EditText editTextHeight;
    private EditText editTextCurrentWeight;
    private EditText editTextGoalWeight;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        /////////////////////////INITIALIZING
        firebaseAuth = FirebaseAuth.getInstance();
        fbuser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        buttonComplete = (Button) findViewById(R.id.complete_form);
        editTextCurrentWeight = (EditText) findViewById(R.id.current_weight);
        editTextGoalWeight = (EditText) findViewById(R.id.goal_weight);
        editTextHeight = (EditText) findViewById(R.id.height);

        /////////////////////////////////////////


        buttonComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDatabase();
            }
        });



    }

    private void toDatabase(){
        String height = editTextHeight.getText().toString().trim();
        String currentWeight = editTextCurrentWeight.getText().toString().trim();
        String goalWeight = editTextGoalWeight.getText().toString().trim();
        Boolean valid = true;

        if(TextUtils.isEmpty(height)){
            editTextHeight.setError("Please Enter a valid height");
            valid = false;
        }
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



    }



}
