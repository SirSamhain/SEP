package com.example.tempname;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class Users {
    public String name;
    public String username;

    public Users(){

    }

    public Users(String name, String username){
        this.name = name;
        this.username = username;
    }



}
