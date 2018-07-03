package com.example.robert.carpark;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.robert.carpark.models.Activity;
import com.example.robert.carpark.models.NotificationGenerator;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 13.06.2018.
 */

public class ActivityRecognizedService extends IntentService {

    ArrayList<Activity> activities = new ArrayList<Activity>();
    Activity NowActivity = new Activity();

    int ProbableActivity[] = new int[6];
    private static final String TAG = "ActivityRecognition";
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Boolean mLocationPermissionsGranted = true;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String userID;

    public int notNR = 0;

    public ActivityRecognizedService() {
        super("ActivityRecognizedService");
    }

    public ActivityRecognizedService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            handleDetectedActivities( result.getProbableActivities() );
        }
    }

    private void handleDetectedActivities(List<DetectedActivity> probableActivities) {

        for( DetectedActivity activity : probableActivities ) {
            switch( activity.getType() ) {
                case DetectedActivity.IN_VEHICLE: {
                    Log.e( "ActivityRecogition", "In Vehicle: " + activity.getConfidence() );
                    if( activity.getConfidence() >= 75 ) {

                        NowActivity.setActivityName("IN_VEHICLE");
                        NowActivity.setTime(System.currentTimeMillis());
                        if( !activities.isEmpty()) {
                            if (NowActivity.getTime() - (activities.get(0)).getTime() >= 180000) {
                                activities.remove(0);
                            }
                            activities.add(NowActivity);
                            notifyTemp(NowActivity.getActivityName());
                            getDeviceLocation();
                        }
                        else {
                            activities.add(NowActivity);
                            notifyTemp(NowActivity.getActivityName());
                            getDeviceLocation();
                        }
                    } else {
                        ProbableActivity[0]=activity.getConfidence();
                    }
                    break;
                }
                case DetectedActivity.ON_BICYCLE: {
                    Log.e( "ActivityRecogition", "On Bicycle: " + activity.getConfidence() );
                    if( activity.getConfidence() >= 75 ) {
                        NowActivity.setActivityName("ON_BICYCLE");
                        NowActivity.setTime(System.currentTimeMillis());
                        if( !activities.isEmpty()) {
                            if (NowActivity.getTime() - (activities.get(0)).getTime() >= 180000) {
                                activities.remove(0);
                            }
                                activities.add(NowActivity);
                                notifyTemp(NowActivity.getActivityName());
                                getDeviceLocation();
                        }
                        else {
                            activities.add(NowActivity);
                            notifyTemp(NowActivity.getActivityName());
                            getDeviceLocation();
                        }
                    } else {
                        ProbableActivity[1]=activity.getConfidence();
                    }
//                    if( activity.getConfidence() >= 75 ) {
//                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//                        builder.setContentText( "Bike" );
//                        builder.setSmallIcon( R.mipmap.ic_launcher );
//                        builder.setContentTitle( getString( R.string.app_name ) );
//                        NotificationManagerCompat.from(this).notify(0, builder.build());
//                    }
                    break;
                }
                case DetectedActivity.ON_FOOT: {
                    Log.e( "ActivityRecogition", "On Foot: " + activity.getConfidence() );
                    if( activity.getConfidence() >= 75 ) {
                        checkActivities();
                        NowActivity.setActivityName("ON_FOOT");
                        NowActivity.setTime(System.currentTimeMillis());
                        if( !activities.isEmpty()) {
                            if (NowActivity.getTime() - (activities.get(0)).getTime() >= 180000) {
                                activities.remove(0);
                            }
                                activities.add(NowActivity);
                                notifyTemp(NowActivity.getActivityName());
                                getDeviceLocation();

                        }
                        else {
                            activities.add(NowActivity);
                            notifyTemp(NowActivity.getActivityName());
                            getDeviceLocation();
                        }
                    } else {
                        ProbableActivity[2]=activity.getConfidence();
                    }
//                    if( activity.getConfidence() >= 75 ) {
//                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//                        builder.setContentText( "Foot" );
//                        builder.setSmallIcon( R.mipmap.ic_launcher );
//                        builder.setContentTitle( getString( R.string.app_name ) );
//                        NotificationManagerCompat.from(this).notify(0, builder.build());
//                    }
                    break;
                }
                case DetectedActivity.RUNNING: {
                    Log.e( "ActivityRecogition", "Running: " + activity.getConfidence() );
                    if( activity.getConfidence() >= 75 ) {
                        NowActivity.setActivityName("RUNNING");
                        NowActivity.setTime(System.currentTimeMillis());
                        if( !activities.isEmpty()) {
                            if (NowActivity.getTime() - (activities.get(0)).getTime() >= 180000) {
                                activities.remove(0);
                            }
                            activities.add(NowActivity);
                            notifyTemp(NowActivity.getActivityName());
                            getDeviceLocation();

                        }
                        else {
                            activities.add(NowActivity);
                            notifyTemp(NowActivity.getActivityName());
                            getDeviceLocation();
                        }
                    } else {
                        ProbableActivity[4]=activity.getConfidence();
                    }
                    break;
                }
                case DetectedActivity.STILL: {
                    Log.e( "ActivityRecogition", "Still: " + activity.getConfidence() );
                     if( activity.getConfidence() >= 75 ) {
                         NowActivity.setActivityName("STILL");
                            NowActivity.setTime(System.currentTimeMillis());
                            if( !activities.isEmpty()) {
                                if (NowActivity.getTime() - (activities.get(0)).getTime() >= 180000) {
                                    activities.remove(0);
                                }
                                    activities.add(NowActivity);
                                notifyTemp(NowActivity.getActivityName());
                                    getDeviceLocation();

                            }
                            else {
                                activities.add(NowActivity);
                                notifyTemp(NowActivity.getActivityName());
                                getDeviceLocation();
                            }
                        } else {
                            ProbableActivity[3]=activity.getConfidence();
                        }

//                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//                        builder.setContentText( "Still" );
//                        builder.setSmallIcon( R.drawable.ic_car );
//                        builder.setContentTitle( getString( R.string.app_name ) );
//                        NotificationManagerCompat.from(this).notify(0, builder.build());

                    break;
                }
                case DetectedActivity.TILTING: {
                    Log.e( "ActivityRecogition", "Tilting: " + activity.getConfidence() );

//                    if( activity.getConfidence() >= 75 ) {
//                        NotificationGenerator.openActivityNotification(getApplicationContext());
//                    }
                    break;
                }
                case DetectedActivity.WALKING: {
//                    Log.e( "ActivityRecogition", "Walking: " + activity.getConfidence() );
//                    if( activity.getConfidence() >= 75 ) {
//                        NowActivity.setActivityName("IN_VEHICLE");
//                        NowActivity.setTime(System.currentTimeMillis());
//                        if( !activities.isEmpty()) {
//                            if (NowActivity.getTime() - (activities.get(0)).getTime() >= 180000) {
//                                activities.remove(0);
//                                activities.add(NowActivity);
//                            }
//                        }
//                        else {
//                            activities.add(NowActivity);
//                        }
//                    } else {
//                        ProbableActivity[0]=activity.getConfidence();
//                    }
//                    if( activity.getConfidence() >= 75 ) {
//                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//                        builder.setContentText( "Are you walking?" );
//                        builder.setSmallIcon( R.mipmap.ic_launcher );
//                        builder.setContentTitle( getString( R.string.app_name ) );
//                        NotificationManagerCompat.from(this).notify(0, builder.build());
//                    }
                    break;
                }
                case DetectedActivity.UNKNOWN: {
                    Log.e( "ActivityRecogition", "Unknown: " + activity.getConfidence() );
                    if( activity.getConfidence() >= 75 ) {
                        NowActivity.setActivityName("UNKNOWN");
                        NowActivity.setTime(System.currentTimeMillis());
                        if( !activities.isEmpty()) {
                            if (NowActivity.getTime() - (activities.get(0)).getTime() >= 180000) {
                                activities.remove(0);
                            }
                                activities.add(NowActivity);
                            notifyTemp(NowActivity.getActivityName());
                                getDeviceLocation();

                        }
                        else {
                            activities.add(NowActivity);
                            notifyTemp(NowActivity.getActivityName());
                            getDeviceLocation();
                        }
                    } else {
                        ProbableActivity[5]=activity.getConfidence();
                        buildActivity();
                    }
//                    if( activity.getConfidence() >= 75 ) {
//                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//                        builder.setContentText( "Unknown" );
//                        builder.setSmallIcon( R.mipmap.ic_launcher );
//                        builder.setContentTitle( getString( R.string.app_name ) );
//                        NotificationManagerCompat.from(this).notify(0, builder.build());
//                    }
                    break;
                }
            }
        }
    }

    private void buildActivity() {
        //build activity from array;
        if (ProbableActivity[0] + ProbableActivity[3] + ProbableActivity[5] > 80) {
            NowActivity.setActivityName("UNKNOWN+");
            NowActivity.setTime(System.currentTimeMillis());
            if (!activities.isEmpty()) {
                if (NowActivity.getTime() - (activities.get(0)).getTime() >= 180000) {
                    activities.remove(0);
                }
                activities.add(NowActivity);
                notifyTemp(NowActivity.getActivityName());
                getDeviceLocation();

            } else {
                activities.add(NowActivity);
                notifyTemp(NowActivity.getActivityName());
                getDeviceLocation();
            }
        } else {
            if (ProbableActivity[0] + ProbableActivity[3] + ProbableActivity[5] > 50) {
                NowActivity.setActivityName("UNKNOWN");
                NowActivity.setTime(System.currentTimeMillis());
                if (!activities.isEmpty()) {
                    if (NowActivity.getTime() - (activities.get(0)).getTime() >= 180000) {
                        activities.remove(0);
                    }
                    activities.add(NowActivity);
                    notifyTemp(NowActivity.getActivityName());
                    getDeviceLocation();

                } else {
                    activities.add(NowActivity);
                    notifyTemp(NowActivity.getActivityName());
                    getDeviceLocation();
                }
            } else {
                NowActivity.setActivityName("UNKNOWN-");
                NowActivity.setTime(System.currentTimeMillis());
                if (!activities.isEmpty()) {
                    if (NowActivity.getTime() - (activities.get(0)).getTime() >= 180000) {
                        activities.remove(0);
                    }
                    activities.add(NowActivity);
                    notifyTemp(NowActivity.getActivityName());
                    getDeviceLocation();
                } else {
                    activities.add(NowActivity);
                    notifyTemp(NowActivity.getActivityName());
                    getDeviceLocation();
                }
            }
        }
    }


    private void checkActivities() {
        int IN_VEHICLE = 0;
        int UNKNOWN = 0;
        int UNKNOWN_PLUS = 0;
        int UNKNOWN_MINUS = 0;
        int TILTING = 0;
        int STILL = 0;
        int ON_FOOT = 0;

        for (int i=0 ; i<activities.size(); i++ ) {
            if (activities.get(i).getActivityName().equals("IN_VEHICLE")) {
                IN_VEHICLE++;
            }
            if (activities.get(i).getActivityName().equals("UNKNOWN")) {
                UNKNOWN++;
            }
            if (activities.get(i).getActivityName().equals("UNKNOWN+")) {
                UNKNOWN_PLUS++;
            }
            if (activities.get(i).getActivityName().equals("UNKNOWN-")) {
                UNKNOWN_MINUS++;
            }
            if (activities.get(i).getActivityName().equals("RUNNING")) {
                TILTING++;
            }
            if (activities.get(i).getActivityName().equals("STILL")) {
                STILL++;
            }
            if (activities.get(i).getActivityName().equals("ON_FOOT")) {
                ON_FOOT++;
            }
        }




        if( IN_VEHICLE >= 1){
            int sum = IN_VEHICLE + UNKNOWN_PLUS + STILL + UNKNOWN;
            double percentage = sum/activities.size()*100;
            notifyTemp(String.valueOf(percentage));
            if (percentage > 75.00) {
                NotificationGenerator.openActivityNotification(getApplicationContext());
            }
        }

        //Check activities to show notification;
    }

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user !x= null) {
            userID = mAuth.getCurrentUser().getUid();

            try {
                if (mLocationPermissionsGranted) {

                    final Task location = mFusedLocationProviderClient.getLastLocation();
                    location.addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "onComplete: found location!");
                                Location currentLocation = (Location) task.getResult();
                                String latitude = String.valueOf(currentLocation.getLatitude());
                                String longitude = String.valueOf(currentLocation.getLongitude());
                                String currentLocationString = latitude + "," + longitude;


                                myRef.child("Users")
                                        .child(userID)
                                        .child("LastKnownLocation")
                                        .setValue(currentLocationString);

//                            moveCameraUser(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
//                                    DEFAULT_ZOOM);

                            } else {
                                Log.d(TAG, "onComplete: current location is null");
                            }
                        }
                    });
                }
            } catch (SecurityException e) {
                Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
            }
        }
    }

    public void notifyTemp(String Type) {
        notNR++;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                        builder.setContentText(Type);
                        builder.setSmallIcon( R.drawable.ic_car );
                        builder.setContentTitle( getString( R.string.app_name ) );
                        NotificationManagerCompat.from(this).notify(0, builder.build());

    }
}










