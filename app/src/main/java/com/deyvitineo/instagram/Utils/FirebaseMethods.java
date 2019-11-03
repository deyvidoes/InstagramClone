package com.deyvitineo.instagram.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.deyvitineo.instagram.Models.User;
import com.deyvitineo.instagram.Models.UserAccountSettings;
import com.deyvitineo.instagram.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseMethods {

    private static final String TAG = "FirebaseMethods";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference databaseReference;


    private Context mContext;
    private String userID;

    public FirebaseMethods(Context context){
        mAuth = FirebaseAuth.getInstance();
        mContext = context;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = mFirebaseDatabase.getReference();

        if(mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
        }

    }

    public boolean checkIfUsernameExists(String username, DataSnapshot dataSnapshot){
        Log.d(TAG, "checkIfUsernameExists: Checking if username already exists");

        User user = new User();
        for(DataSnapshot ds: dataSnapshot.child(mContext.getString(R.string.dbname_users)).getChildren()){
            Log.d(TAG, "checkIfUsernameExists: data snapshot: " + ds);
            user.setUsername(ds.getValue(User.class).getUsername());
            Log.d(TAG, "checkIfUsernameExists: username: " + user.getUsername());

            if(StringManipulation.expandUsername(user.getUsername()).equals(username)){
                Log.d(TAG, "checkIfUsernameExists: FOUND A MATCH " + user.getUsername());
                return true;
            }
        }

        return false;
    }

    /**
     * Register a new account to firebase
     * @param email
     * @param password
     * @param username
     */
    public void registerNewEmail(String email, String password, String username){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            userID = mAuth.getCurrentUser().getUid();
                            Log.d(TAG, "createUserWithEmail:success " + userID);
                            Toast.makeText(mContext, "Registration Completed", Toast.LENGTH_SHORT).show();


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure ", task.getException());
                            Toast.makeText(mContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    public void addNewUser(String email, String displayName, String username, String description, String website, String profile_photo){

        User user = new User(userID, 1, email, StringManipulation.condenseUsername(username));

        //saves data to database
        databaseReference.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .setValue(user);

        UserAccountSettings settings = new UserAccountSettings(
                description,
                displayName,
                0,
                0,
                0,
                profile_photo,
                StringManipulation.condenseUsername(username),
                website
        );

        //saves data to database
        databaseReference.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(userID)
                .setValue(settings);

    }

}
