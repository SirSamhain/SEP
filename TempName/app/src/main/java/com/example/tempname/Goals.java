package com.example.tempname;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;

public class Goals {

    public String goal;
    public String description;
    public int primary;
    public String date;

    public Goals(){

    }
    public Goals(String goal, String description){
        this.goal = goal;
        this.description = description;
    }
    public Goals(String goal, String description, int primary, String date){
        this.goal = goal;
        this.description = description;
        this.primary = primary;
        this.date = date;
    }





}
