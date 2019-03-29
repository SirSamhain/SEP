package com.example.tempname;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {


    private Button buttonRegister;
    private AutoCompleteTextView autoCompleteTextViewEmail;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private EditText editTextName;
    private EditText editTextUsername;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser fbuser;

    private DatabaseReference databaseReference;
    private Users user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        editTextPassword = (EditText) findViewById(R.id.password);
        editTextConfirmPassword = (EditText) findViewById(R.id.confirm_password);
        autoCompleteTextViewEmail = (AutoCompleteTextView) findViewById(R.id.email);
        editTextName = (EditText) findViewById(R.id.name);
        editTextUsername = (EditText) findViewById(R.id.username);

        buttonRegister = (Button) findViewById(R.id.email_register_button);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });


    }



    private void registerUser(){
        String email = autoCompleteTextViewEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();
        final String name = editTextName.getText().toString().trim();
        final String username = editTextUsername.getText().toString().trim();

        if(TextUtils.isEmpty(username)){
            editTextConfirmPassword.setError("Enter valid username");
            return;
        }
        if(TextUtils.isEmpty(name)){
            editTextConfirmPassword.setError("Enter valid name");
            return;
        }
        if(TextUtils.isEmpty(email)){
            autoCompleteTextViewEmail.setError("Please enter a valid email");
            return;
        }
        if(TextUtils.isEmpty(password)){
            editTextPassword.setError("Enter valid password");
            return;
        }
        if(TextUtils.isEmpty(confirmPassword)){
            editTextConfirmPassword.setError("Enter valid password");
            return;
        }
        if(!password.equals(confirmPassword)){
            editTextPassword.setError("Passwords don't match!!!");
            return;
        }

        progressDialog.setMessage("Registering User....");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Sucess!", Toast.LENGTH_SHORT).show();
                    storeUser(name, username);
                    finish();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(RegisterActivity.this, "Uh oh :( Something went wrong", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }



    private void storeUser(String name, String username){
        user = new Users(name, username);
        fbuser = firebaseAuth.getCurrentUser();
        databaseReference.child(fbuser.getUid()).setValue(user);
    }






}
