package com.example.tempname;

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

import java.util.ArrayList;

public class AddGoalActivity extends AppCompatActivity {

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

        editTextGoalDescription = findViewById(R.id.goal_description);
        editTextGoalName        = findViewById(R.id.goal_name);

        initGoals();
        spinner = (Spinner) findViewById(R.id.goals_spinner);
        //getting the adapter to show all of the pre-made goals on the spinner
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, goals);
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
        submit = (Button) findViewById(R.id.add_goal_button);
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
        Toast.makeText(this, Integer.toString(pos), Toast.LENGTH_SHORT).show();
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

    private void toDatabase(String goal, String description){
    
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
