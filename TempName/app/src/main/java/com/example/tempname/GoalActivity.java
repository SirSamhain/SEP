package com.example.tempname;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class GoalActivity extends AppCompatActivity {
    String[] testArray = {"sample 01", "sample02", "sample03", "sample04", "sample05"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        //ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_goal, testArray);

        //ListView listView = (ListView) findViewById(R.id.app_goal_list);
        //listView.setAdapter(adapter);

        Button addGoalButton = (Button) findViewById(R.id.add_goal);
        addGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GoalActivity.this, AddGoalActivity.class);
                startActivity(intent);
            }
        });
    }
}
