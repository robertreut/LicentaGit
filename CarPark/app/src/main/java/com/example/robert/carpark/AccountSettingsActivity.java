package com.example.robert.carpark;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.robert.carpark.models.UniversalImageLoader;
import com.example.robert.carpark.models.Upload;
import com.example.robert.carpark.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
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
    public User userData = new User();
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(AccountSettingsActivity.this));
        mProfilePhoto = (CircleImageView) findViewById(R.id.profile_photo);
        mUsername = findViewById(R.id.username);
        mPrestigePoints = findViewById(R.id.prestige_points);
        mPhoneNumber = findViewById(R.id.phoneNumber);
        mChangeProfilePhoto = findViewById(R.id.changeProfilePhoto);
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");

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



        if(!userData.getUsername().equals(username)){
            //update username
            myRef.child("Users")
                    .child(userID)
                    .child("username")
                    .setValue(username);
        }
        if(!userData.getPhoneNumber().equals(phoneNumber)) {
            //update phone number
            myRef.child("Users")
                    .child(userID)
                    .child("phoneNumber")
                    .setValue(phoneNumber);
        }
    }



    private void setProfileWidgets(User userData){
        User data= userData;

        UniversalImageLoader.setImage(data.getPhotoUrl(), mProfilePhoto, null, "");
        mUsername.setText(data.getUsername());
        mPrestigePoints.setText((Integer.toString((int) data.getPrestige())) + " (prestige points)" );
        mPhoneNumber.setText(data.getPhoneNumber());

        mChangeProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();

            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            uploadFile(mImageUri);


        }
    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    private void uploadFile(Uri mImageUri) {
        if(mImageUri != null) {
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() +
            "." + getFileExtension(mImageUri));
            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(AccountSettingsActivity.this,"Upload successful", Toast.LENGTH_SHORT).show();
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUrl = uri;
                                    Upload upload = new Upload(userData.getUsername(),downloadUrl.toString());
                                    updateProfilePhoto(upload);
                                }
                            });
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateProfilePhoto(Upload upload) {
        myRef.child("Users")
                .child(userID)
                .child("photoUrl")
                .setValue(upload.getImageUrl());
    }

    public User getUserData(DataSnapshot dataSnapshot) {
        Log.d(TAG, "getUserSettings: retrieving user account settings from firebase.");
        //User userData = new User();
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



















