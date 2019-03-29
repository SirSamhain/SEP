package com.example.tempname;

import android.app.ProgressDialog;
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

public class RegisterActivity extends AppCompatActivity {


    private Button buttonRegister;
    private AutoCompleteTextView autoCompleteTextViewEmail;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        editTextPassword = (EditText) findViewById(R.id.password);
        editTextConfirmPassword = (EditText) findViewById(R.id.confirm_password);
        autoCompleteTextViewEmail = (AutoCompleteTextView) findViewById(R.id.email);

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
                    Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(RegisterActivity.this, "Uh oh :( Something went wrong", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }










}
