package com.example.tempname;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity{


    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    // UI references.
    private AutoCompleteTextView autoCompleteTextViewEmail;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        firebaseAuth = FirebaseAuth.getInstance();
        //If the user is already logged in then it'll start the profile page automatically :)
        if(firebaseAuth.getCurrentUser() != null){
            finish();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);            
        }
        progressDialog = new ProgressDialog(this);

        autoCompleteTextViewEmail = (AutoCompleteTextView) findViewById(R.id.email);

        editTextPassword = (EditText) findViewById(R.id.password);
        editTextPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        Button mEmailRegisterButton = (Button) findViewById(R.id.email_register_button);
        mEmailRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    private void attemptLogin() {
        // Reset errors.
        autoCompleteTextViewEmail.setError(null);
        editTextPassword.setError(null);

        // Store values at the time of the login attempt.
        String email = autoCompleteTextViewEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        boolean valid = true;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            autoCompleteTextViewEmail.setError(getString(R.string.error_field_required));
            valid = false;
        } else if (!email.contains("@")) {
            autoCompleteTextViewEmail.setError(getString(R.string.error_invalid_email));
            valid = false;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)){ //&& !isPasswordValid(password)) {
            editTextPassword.setError(getString(R.string.error_invalid_password));
            valid = false;
        }
        if(!valid){return;}

        progressDialog.setMessage("Checking Credentials");
        progressDialog.show();


        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Sucess!", Toast.LENGTH_SHORT).show();
                            finish();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(LoginActivity.this, "Try again :)", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });


    }




}

