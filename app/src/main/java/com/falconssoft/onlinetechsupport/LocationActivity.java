package com.falconssoft.onlinetechsupport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import im.delight.android.location.SimpleLocation;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class LocationActivity extends AppCompatActivity implements LocationListener {

    private static final int REQUEST_LOCATION = 1;
    Button btnGetLocation, update, distanceB;
    TextView showLocation, distance;
    LocationManager locationManager;
    String latitude, longitude, val;
    SimpleLocation one;
    SimpleLocation two;
    SimpleLocation.Point point1, point2;

    double longiOne, latOne, longiTwo, latTwo;
    FusedLocationProviderClient mFusedLocationClient;

    private SimpleLocation location;

    private LocationRequest mLocationRequest;

    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    boolean flag = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
//        initLocationLibraries();

//        ActivityCompat.requestPermissions(this,
//                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        showLocation = findViewById(R.id.showLocation);
        btnGetLocation = findViewById(R.id.btnGetLocation);
        update = findViewById(R.id.btnUpdateLocation);
        distance = findViewById(R.id.distance);
        distanceB = findViewById(R.id.btnDistance);
        startLocationUpdates();
//        location = new SimpleLocation(this, true, false, 1);

        // if we can't access the location yet
//        if (!location.hasLocationEnabled()) {
//            // ask the user to enable location access
//            SimpleLocation.openSettings(this);
//        }

//        one = new SimpleLocation(this);//new Location("dummyprovider");
//        two =new SimpleLocation(LocationActivity.this);// new Location("dummyprovider");

        requestPermission();
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startLocationUpdates();

//                location.beginUpdates();
                findDistance2(new SimpleLocation.Point(location.getLatitude(), location.getLongitude()));

//                two = location;
                val += "\nlong2:" + location.getLongitude() + "/lat2:" + location.getLatitude();
                showLocation.setText(val);
//                if (ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
//                    return;
//
//                LocationCallback callback = new LocationCallback() {
//                    @Override
//                    public void onLocationResult(LocationResult locationResult) {
//                        super.onLocationResult(locationResult);
//                        two = locationResult.getLastLocation();
//                        val += "\n/long2:" + two.getLongitude() + "/lat2:" + two.getLatitude();
//                        showLocation.setText(val);
//                    }
//                };

//                mFusedLocationClient  = LocationServices.getFusedLocationProviderClient(LocationActivity.this);
//                mFusedLocationClient.getLastLocation().addOnSuccessListener(LocationActivity.this, new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        if (location != null)
//                            two = location;
//
//
//                        val += "\n/long2:" + two.getLongitude() + "/lat2:" + two.getLatitude();
//                        showLocation.setText(val);
//
//
//                    }
//                });
            }
        });

//        distanceB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(LocationActivity.this,  "lon:" + location.getLongitude() + "/long2:" + two.getLongitude(), Toast.LENGTH_SHORT).show();
//                Log.e("distanceB","lon:" + location.getLongitude() + "/long2:" + two.getLongitude() );
//                double dis =location.calculateDistance(one.getLatitude(), one.getLongitude(), two.getLatitude(), two.getLongitude());
//                distance.setText("" + dis);
//            }
//        });

        btnGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                one = location;
                point1 = new SimpleLocation.Point(location.getLatitude(), location.getLongitude());
//                longiOne = location.getLongitude();
//                latOne = location.getLatitude();
                val = "long:" + location.getLongitude() + "/lat:" + location.getLatitude();
//                if (ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
//                    return;
//
//
//                mFusedLocationClient.getLastLocation().addOnSuccessListener(LocationActivity.this, new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        if (location != null)
//                            one = location;
//                        val = "long:" + location.getLongitude() + "/lat:" + location.getLatitude();
//
//                    }
//                });


//                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                    OnGPS();
//                } else {
////                    getLocation();
////                    getLastLocationNewMethod();
//
//                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//                    if (ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
//                            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    }
//                    Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
//
//                    onLocationChanged(location);
//
//                }
            }
        });

//        location.setListener(new SimpleLocation.Listener() {
//
//            public void onPositionChanged() {
////                two = location;
////                findDistance(location.getLongitude(), location.getLatitude());
//                Toast.makeText(LocationActivity.this, "OPC:lon:" + location.getLongitude() + "/lat:" + location.getLatitude(), Toast.LENGTH_SHORT).show();
////                findDistance2(new SimpleLocation.Point(location.getLatitude(), location.getLongitude()));
//                // new location data has been received and can be accessed
//            }
//
//        });
    }

    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }

    public void onLocationChanged(Location location) {
        // New location has now been determined
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        if (flag){
            longiOne = location.getLongitude();
            latOne = location.getLatitude();
            flag = false;
        }
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        findDistance(location.getLongitude(), location.getLatitude());
        // You can now create a LatLng Object for use with maps
//        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }

    public void getLastLocation() {
        // Get last known recent location using new Google Play Services SDK (v11+)
        FusedLocationProviderClient locationClient = getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // GPS location can be null if GPS is switched off
                        if (location != null) {
                            onLocationChanged(location);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MapDemoActivity", "Error trying to get last GPS location");
                        e.printStackTrace();
                    }
                });
    }

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//
//        if(checkPermissions()) {
//            googleMap.setMyLocationEnabled(true);
//        }
//    }

    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions();
            return false;
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                1);
    }
    //*************************************************************************************
    void  findDistance(double longi, double lat){
        double dis =location.calculateDistance(latOne, longiOne, lat, longi);
        distance.setText("" + dis);

    }

    void  findDistance2(SimpleLocation.Point point2){
        double dis =location.calculateDistance(point1,point2);
        distance.setText("" + dis);

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        // make the device update its location
//        location.beginUpdates();
//        Toast.makeText(this, "update:lon:" + location.getLongitude() +  "/lat:" + location.getLatitude(), Toast.LENGTH_SHORT).show();
//
//        // ...
//    }

    @Override
    protected void onPause() {
        // stop location updates (saves battery)
//        location.endUpdates();

        // ...

        super.onPause();
    }

    void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    // 3
    private void initLocationLibraries() {
        mFusedLocationClient = getFusedLocationProviderClient(this);
        SettingsClient mSettingsClient = LocationServices.getSettingsClient(this);

        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
//                mCurrentLocation = locationResult.getLastLocation();
                Log.e("ccccccheckIn", "long: " + locationResult.getLastLocation().getLongitude() + "/lat: " + locationResult.getLastLocation().getLatitude());

            }
        };

//        mRequestingLocationUpdates = false;

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(100000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest mLocationSettingsRequest = builder.build();
    }

    // 1
    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // 1
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                LocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                LocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
                showLocation.setText("Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude);
            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 2
    private void getLastLocationNewMethod() {
        FusedLocationProviderClient mFusedLocationClient = getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this
                , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this
                , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // GPS location can be null if GPS is switched off
                        if (location != null) {
                            double lat = location.getLatitude();
                            double longi = location.getLongitude();
                            latitude = String.valueOf(lat);
                            longitude = String.valueOf(longi);
                            showLocation.setText("Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MapDemoActivity", "Error trying to get last GPS location");
                        e.printStackTrace();
                    }
                });
    }

    protected void startLocationUpdates1() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        onLocationChanged(locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER));
    }

    protected void endLocationUpdates() {
        locationManager.removeUpdates(this);

    }

//    @Override
//    public void onLocationChanged(Location location) {
//        double longitude = location.getLongitude();
//        double latitude = location.getLatitude();
//        showLocation.setText("Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude);
//
//
//    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

}