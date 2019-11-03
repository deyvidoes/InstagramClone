package com.deyvitineo.instagram.Login;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.deyvitineo.instagram.R;
import com.deyvitineo.instagram.Utils.FirebaseMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private Context mContext;
    private String email, username, password;
    private EditText mEmail, mUsername, mPassword;
    private TextView loadingPleaseWait;
    private Button btnRegister;
    private ProgressBar mProgressBar;
    private FirebaseMethods firebaseMethods;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference databaseReference;

    private String append = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext = RegisterActivity.this;
        Log.d(TAG, "onCreate: Started");
        firebaseMethods = new FirebaseMethods(mContext);
        initWidgets();
        setupFirebaseAuth();

        init();

    }

    private void init(){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mEmail.getText().toString();
                username = mUsername.getText().toString();
                password = mPassword.getText().toString();

                if(checkInputs(email, username, password)){
                    mProgressBar.setVisibility(View.VISIBLE);
                    loadingPleaseWait.setVisibility(View.VISIBLE);
                    firebaseMethods.registerNewEmail(email, password, username);
                }

            }
        });
    }

    private boolean checkInputs(String email, String username, String password){
        Log.d(TAG, "checkInputs: Checking inputs for null values");
        if(isStringNull(email) || isStringNull(username) || isStringNull(password)){
            Toast.makeText(mContext, "All fields must be filled out", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    //Initialize activity widgets
    private void initWidgets(){
        Log.d(TAG, "initWidgets: Initializing widgets");
        mEmail = findViewById(R.id.input_email);
        mUsername = findViewById(R.id.input_username);
        mPassword = findViewById(R.id.input_password);
        btnRegister = findViewById(R.id.btn_register);
        loadingPleaseWait = findViewById(R.id.registerPleaseWait);
        mProgressBar = findViewById(R.id.registerProgressBar);

        mProgressBar.setVisibility(View.GONE);
        loadingPleaseWait.setVisibility(View.GONE);
    }




    private boolean isStringNull(String str){
        Log.d(TAG, "isStringNull: checking if string is null");
        if(str.equals("")){
            return true;
        } else{
            return false;
        }
    }
    //capitalize each beginning letter
    private String capitalize(String capString){
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()){
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }
        return capMatcher.appendTail(capBuffer).toString();
    }

    /**
     * ------------------------------------- FIREBASE SETUP -------------------------------------
     */

    /*
     *setup firebase auth object
     */
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase authentication");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    //user is signed in
                    Log.d(TAG, "onAuthStateChanged: signed in" + user.getUid());

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //Make sure that username is not being used
                            if(firebaseMethods.checkIfUsernameExists(username, dataSnapshot)){
                                append = databaseReference.push().getKey().substring(3,10);
                                Log.d(TAG, "onDataChange: username already exists. Appending random string to name: " + append);
                            }
                            String appended = username + append;

                            //add new user to the database
                            firebaseMethods.addNewUser(email, capitalize(username), appended, "", "", "");

                            Toast.makeText(mContext, "Signed up successfully. Sending verification email.", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else{
                    //user is signed out
                    Log.d(TAG, "onAuthStateChanged: signed out");
                }
            }
        };
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop(){
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
