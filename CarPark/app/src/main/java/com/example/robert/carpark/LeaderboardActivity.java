package com.example.robert.carpark;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.robert.carpark.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class LeaderboardActivity extends AppCompatActivity {

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String userID;
    private TextView user1rank,user1username,user1prestige,user2rank,user2username,user2prestige,
            user3rank,user3username,user3prestige,user4rank,user4username,user4prestige,
            user5rank,user5username,user5prestige,user6rank,user6username,user6prestige,
            user7rank,user7username,user7prestige,user8rank,user8username,user8prestige,
            user9rank,user9username,user9prestige,user10rank,user10username,user10prestige,
            user11rank,user11username,user11prestige,user12rank,user12username,user12prestige,
            user13rank,user13username,user13prestige,user14rank,user14username,user14prestige,
            user15rank,user15username,user15prestige,user16rank,user16username,user16prestige,
            user17rank,user17username,user17prestige,user18rank,user18username,user18prestige,
            user19rank,user19username,user19prestige,user20rank,user20username,user20prestige;
    public List Users = new ArrayList<User>();
    private Button topUsers,aroundUsers;
    private static final String TAG = "Leaderboard Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        setupFirebaseAuth();
        topUsers = findViewById(R.id.topUsers);
        aroundUsers = findViewById(R.id.aroundUsers);
        user1rank = findViewById(R.id.user1rank);
        user2rank = findViewById(R.id.user2rank);
        user3rank = findViewById(R.id.user3rank);
        user4rank = findViewById(R.id.user4rank);
        user5rank = findViewById(R.id.user5rank);
        user6rank = findViewById(R.id.user6rank);
        user7rank = findViewById(R.id.user7rank);
        user8rank = findViewById(R.id.user8rank);
        user9rank = findViewById(R.id.user9rank);
        user10rank = findViewById(R.id.user10rank);
        user11rank = findViewById(R.id.user11rank);
        user12rank = findViewById(R.id.user12rank);
        user13rank = findViewById(R.id.user13rank);
        user14rank = findViewById(R.id.user14rank);
        user15rank = findViewById(R.id.user15rank);
        user16rank = findViewById(R.id.user16rank);
        user17rank = findViewById(R.id.user17rank);
        user18rank = findViewById(R.id.user18rank);
        user19rank = findViewById(R.id.user19rank);
        user20rank = findViewById(R.id.user20rank);
        user1username = findViewById(R.id.user1username);
        user2username = findViewById(R.id.user2username);
        user3username = findViewById(R.id.user3username);
        user4username = findViewById(R.id.user4username);
        user5username = findViewById(R.id.user5username);
        user6username = findViewById(R.id.user6username);
        user7username = findViewById(R.id.user7username);
        user8username = findViewById(R.id.user8username);
        user9username = findViewById(R.id.user9username);
        user10username = findViewById(R.id.user10username);
        user11username = findViewById(R.id.user11username);
        user12username = findViewById(R.id.user12username);
        user13username = findViewById(R.id.user13username);
        user14username = findViewById(R.id.user14username);
        user15username = findViewById(R.id.user15username);
        user16username = findViewById(R.id.user16username);
        user17username = findViewById(R.id.user17username);
        user18username = findViewById(R.id.user18username);
        user19username = findViewById(R.id.user19username);
        user20username = findViewById(R.id.user20username);
        user1prestige = findViewById(R.id.user1prestige);
        user2prestige = findViewById(R.id.user2prestige);
        user3prestige = findViewById(R.id.user3prestige);
        user4prestige = findViewById(R.id.user4prestige);
        user5prestige = findViewById(R.id.user5prestige);
        user6prestige = findViewById(R.id.user6prestige);
        user7prestige = findViewById(R.id.user7prestige);
        user8prestige = findViewById(R.id.user8prestige);
        user9prestige = findViewById(R.id.user9prestige);
        user10prestige = findViewById(R.id.user10prestige);
        user11prestige = findViewById(R.id.user11prestige);
        user12prestige = findViewById(R.id.user12prestige);
        user13prestige = findViewById(R.id.user13prestige);
        user14prestige = findViewById(R.id.user14prestige);
        user15prestige = findViewById(R.id.user15prestige);
        user16prestige = findViewById(R.id.user16prestige);
        user17prestige = findViewById(R.id.user17prestige);
        user18prestige = findViewById(R.id.user18prestige);
        user19prestige = findViewById(R.id.user19prestige);
        user20prestige = findViewById(R.id.user20prestige);

        //initializeTable(getUsers());
        //


        topUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InitializeTable();
            }
        });

        aroundUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initializeAroundTable();
            }
        });
    }



    public void InitializeTable(){


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("Users");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            HashMap Data = (HashMap) dataSnapshot.getValue();
                            Iterator it = Data.entrySet().iterator();
                            while (it.hasNext()) {
                                Map.Entry pair = (Map.Entry) it.next();
                                User userData = new User();
                                HashMap Data2 = (HashMap) pair.getValue();
                                userData.setUserID((String) pair.getKey());
                                Iterator it2 = Data2.entrySet().iterator();
                                while (it2.hasNext()) {
                                    Map.Entry pair2 = (Map.Entry) it2.next();

                                    if (pair2.getKey().equals("username")) {
                                        userData.setUsername((String) pair2.getValue());
                                    }
                                    if (pair2.getKey().equals("prestige")) {
                                        userData.setPrestige((long) pair2.getValue());
                                    }
                                    if (pair2.getKey().equals("phoneNumber")) {
                                        userData.setPhoneNumber((String) pair2.getValue());
                                    }
                                    it2.remove();
                                }
                                userData.setPhotoUrl("a");
                                Users.add(userData);
                                it.remove();
                            }
                arrangeUsers(Users);
                populateViewTop(Users);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void populateViewTop(List users) {
    }

    private void arrangeUsers(List users) {
        Collections.sort(users);
        Users=users;
    }


    public void initializeAroundTable () {

    }


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
