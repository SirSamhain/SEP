package com.example.tempname;

import android.app.ProgressDialog;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddGoalActivity extends AppCompatActivity {
    //FIREBASE STUFF
    private FirebaseAuth firebaseAuth;
    private FirebaseUser fbuser;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private ProgressDialog progressDialog;
    ///////////////////
    private Goals goal_object;
    private Spinner spinner;
    private ArrayList<String> goals = new ArrayList<>();
    private ArrayList<String> descriptions = new ArrayList<>();

    private ArrayAdapter<String> arrayAdapter;
    private int pos = 0;
    private Button submit;

    private EditText editTextGoalName;
    private EditText editTextGoalDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);
        //initializing
        progressDialog = new ProgressDialog(this);
        editTextGoalDescription = findViewById(R.id.goal_description);
        editTextGoalName        = findViewById(R.id.goal_name);
        spinner = (Spinner) findViewById(R.id.goals_spinner);
        initGoals();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, goals);
        submit = (Button) findViewById(R.id.add_goal_button);
        firebaseAuth = FirebaseAuth.getInstance();
        fbuser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////

        //getting the adapter to show all of the pre-made goals on the spinner
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //this is the submit button which determines what to do if they have a pre-made goal selected or not
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pos > 0){
                    add_premade(pos);
                }else{
                    add_custom();
                }
            }
        });

    }

    private void add_premade(int pos){
        String goal = goals.get(pos);
        String description = descriptions.get(pos);
        toDatabase(goal, description);

    }

    private void add_custom(){
        String goal        = editTextGoalName.getText().toString().trim();
        String description = editTextGoalDescription.getText().toString().trim();


        if(TextUtils.isEmpty(goal)){
            editTextGoalName.setError("Please enter a goal name");
            return;
        }
        if(TextUtils.isEmpty(description)){
            editTextGoalDescription.setError("Please enter a description");
            return;
        }

        Toast.makeText(this, goal + " " + description, Toast.LENGTH_SHORT);
        toDatabase(goal, description);

    }

    private void toDatabase(String goal, String description) {
        progressDialog.setMessage("Adding Goal....");
        progressDialog.show();
        goal_object = new Goals(goal, description);
        databaseReference.child(fbuser.getUid()).child("goals").child(goal).setValue(goal_object)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        Toast.makeText(AddGoalActivity.this, "Successfully added!", Toast.LENGTH_SHORT);
                        finish();
                        Intent intent = new Intent(AddGoalActivity.this, GoalActivity.class);
                        startActivity(intent);
                    }
                });



    }

    private void initGoals(){
        goals.add("Select or add your own goal!");
        descriptions.add("NOTHING");

        goals.add("Quit Smoking");
        descriptions.add("Goal to quit smoking forever! :)");

        goals.add("Quit Drinking");
        descriptions.add("Goal is to quit drinking alcohol forever! :)");

        goals.add("Diet");
        descriptions.add("Goal is to keep on your diet to keep you healthy!");

        goals.add("Exercise Daily");
        descriptions.add("Goal is to exercise daily to keep in shape :)");

    }

}
