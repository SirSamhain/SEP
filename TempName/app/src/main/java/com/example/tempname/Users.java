package com.example.tempname;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class Users {
    public String name;
    public String username;
    public String email;

    public Users(){

    }

    public Users(String name, String username, String email){
        this.name = name;
        this.username = username;
    }

    public String ft;
    public String inches;
    public String currentWeight;
    public String goalWeight;

    public Users(String ft, String inches,String currentWeight, String goalWeight){
        this.ft = ft;
        this.inches = inches;
        this.currentWeight = currentWeight;
        this.goalWeight = goalWeight;
    }


}
