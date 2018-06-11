package com.example.robert.carpark;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.robert.carpark.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountSettingsActivity extends AppCompatActivity {

    private static final String TAG = "EditProfileFragment";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String userID;
    //private User mUser;


    //EditProfile Fragment widgets
    private EditText mUsername, mPhoneNumber;
    private TextView mChangeProfilePhoto, mPrestigePoints;
    private CircleImageView mProfilePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(AccountSettingsActivity.this));
        mProfilePhoto = (CircleImageView) findViewById(R.id.profile_photo);
        //mProfilePhoto = findViewById(R.id.profile_photo);
        mUsername = findViewById(R.id.username);
        mPrestigePoints = findViewById(R.id.prestige_points);
        mPhoneNumber = findViewById(R.id.phoneNumber);
        mChangeProfilePhoto = findViewById(R.id.changeProfilePhoto);


        setupFirebaseAuth();

        ImageView backArrow = (ImageView) findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating back to ProfileActivity");
                finish();
            }
        });

        ImageView checkmark = (ImageView) findViewById(R.id.saveChanges);
        checkmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attempting to save changes.");
                saveProfileSettings();
            }
        });

    }

    /**
     * Retrieves the data contained in the widgets and submits it to the database
     * Before donig so it chekcs to make sure the username chosen is unqiue
     */
    private void saveProfileSettings(){
        final String username = mUsername.getText().toString();
        final String phoneNumber = mPhoneNumber.getText().toString();
        final long prestige = Long.parseLong(mPrestigePoints.getText().toString());

//        if(!mUserSettings.getSettings().getDisplay_name().equals(displayName)){
//            //update displayname
//            mFirebaseMethods.updateUserAccountSettings(displayName, null, null, 0);
//        }
//        if(!mUserSettings.getSettings().getWebsite().equals(website)){
//            //update website
//            mFirebaseMethods.updateUserAccountSettings(null, website, null, 0);
//        }
//        if(!mUserSettings.getSettings().getDescription().equals(description)){
//            //update description
//            mFirebaseMethods.updateUserAccountSettings(null, null, description, 0);
//        }
//        if(!mUserSettings.getSettings().getProfile_photo().equals(phoneNumber)){
//            //update phoneNumber
//            mFirebaseMethods.updateUserAccountSettings(null, null, null, phoneNumber);
//        }
    }



    private void setProfileWidgets(User userData){
        User data= userData;

        UniversalImageLoader.setImage(data.getPhotoUrl(), mProfilePhoto, null, "");
        mUsername.setText(data.getUsername());
        mPrestigePoints.setText((Integer.toString((int) data.getPrestige())) + " (prestige points)" );
        mPhoneNumber.setText(data.getPhoneNumber());

//        mChangeProfilePhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: changing profile photo");
//                Intent intent = new Intent(getActivity(), ShareActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //268435456
//                getActivity().startActivity(intent);
//                getActivity().finish();
//            }
//        });
    }

    public User getUserData(DataSnapshot dataSnapshot) {
        Log.d(TAG, "getUserSettings: retrieving user account settings from firebase.");
        User userData = new User();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userID = user.getUid();
            userData.setUserID(userID);

            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                if (ds.getKey().equals("Users")) {
                    Log.d(TAG, "getUserSettings: user account settings node datasnapshot: " + ds);
                    try {
                        HashMap Data = (HashMap) ds.child(userID).getValue();
                        Iterator it = Data.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry) it.next();
                            if (pair.getKey().equals("phoneNumber")) {
                                userData.setPhoneNumber((String) pair.getValue());
                            }
                            if (pair.getKey().equals("username")) {
                                userData.setUsername((String) pair.getValue());
                            }
                            if (pair.getKey().equals("prestige")) {
                                userData.setPrestige((long) pair.getValue());
                            }
                            if (pair.getKey().equals("photoUrl")) {
                                userData.setPhotoUrl((String) pair.getValue());
                            }
                            it.remove();
                        }
                    } catch (NullPointerException e) {
                        Log.e(TAG, "getUserAccountSettings: NullPointerException: " + e.getMessage());
                    }
                }
            }
        }
        return userData;
    }


        /*
    ------------------------------------ Firebase ---------------------------------------------
     */

    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        userID = mAuth.getCurrentUser().getUid();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();


                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //retrieve user information from the database
                setProfileWidgets(getUserData(dataSnapshot));

                //retrieve images for the user in question

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}



















