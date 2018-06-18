package com.interactivebois.dyetheworld;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class LocationHandler
{
    private static final String TAG = "LocationHandler";

    private MapActivity view;

    private String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;

    private String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private float DEFAULT_ZOOM = 15f;

    private final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    private boolean centerCameraOnPlayer = true;

    private boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;

    public LocationHandler (MapActivity mapActivity)
    {
        this.view = mapActivity;
    }

    public void getLocationPermission()
    {
        Log.d(TAG, "getLocationPermission: getting location permission");
        String[] permissions = {FINE_LOCATION,
                COURSE_LOCATION};

        if (ContextCompat.checkSelfPermission(view.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            if (ContextCompat.checkSelfPermission(view.getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                mLocationPermissionGranted = true;
                //initMap();
                view.initMap();
            }
            else
            {
                ActivityCompat.requestPermissions(view, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
        else
        {
            ActivityCompat.requestPermissions(view, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    public void handlePermissionsResult(int requestCode, @NonNull int[] grantResults)
    {
        Log.d(TAG, "handlePermissionsResult: called.");
        this.mLocationPermissionGranted = false;

        switch (requestCode)
        {
            case LOCATION_PERMISSION_REQUEST_CODE:
            {
                if (grantResults.length <= 0)
                {
                    return;
                }

                for (int i = 0; i < grantResults.length;i++)
                {
                    if(grantResults[i] != PackageManager.PERMISSION_GRANTED)
                    {
                        this.mLocationPermissionGranted = false;
                        Log.d(TAG, "handlePermissionsResult: permission failed.");
                        return;
                    }
                }
                Log.d(TAG, "handlePermissionsResult: permission granted.");
                this.mLocationPermissionGranted = true;
                //Initialise our map
                //initMap();
                view.initMap();
            }
        }
    }

    public void getDeviceLocation()
    {
        Log.d(TAG, "getDeviceLocation: Getting the device current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view);

        try
        {
            if (mLocationPermissionGranted)
            {
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener()
                {
                    @Override
                    public void onComplete(@NonNull Task task)
                    {
                        if (task.isSuccessful())
                        {
                            Log.d(TAG, "onComplete: found location");
                            Location currentLocation = (Location) task.getResult();

                            Toast.makeText(view, "Location Changed", Toast.LENGTH_SHORT).show();

                            LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                            view.getMapContainer().setCurrentLatLng(currentLatLng);

                            view.getMapHandler().moveCamera(currentLatLng, DEFAULT_ZOOM);
                            //moveCamera(currentLatLng, DEFAULT_ZOOM);



                            //MapActivity.this.currentLatLng = currentLatLng;
                        }
                        else
                        {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(view, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
            }
        } catch (SecurityException e)
        {
            Log.e(TAG, "getDeviceLocation: SecurityException" + e.getMessage());
        }
    }

    public void createLocationRequest()
    {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setSmallestDisplacement(10);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void createLocationCallBack()
    {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                LatLng latLng = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());

                MapContainer.getInstance().setCurrentLatLng(latLng);
                //MapActivity.this.currentLatLng = latLng;

                if (centerCameraOnPlayer)
                {
                    view.getmMap().animateCamera(CameraUpdateFactory.newLatLng(latLng));
                }

                if (MapContainer.getInstance().isRouteStarted())
                {
                    float distance = view.getMapHandler().calculateDistance();

                    TextView distanceText = (TextView) view.findViewById(R.id.Distance);
                    distanceText.setText("Distance: " + distance);

                    if (distance >= 25)
                    {
                        view.getMapHandler().createPoint(latLng);
                        Toast.makeText(view, "Lat: " + latLng.latitude + ", lon: " + latLng.longitude, Toast.LENGTH_SHORT).show();
                    }
                }
            };
        };
    }

    public void setCenterCameraOnPlayer(boolean centerCameraOnPlayer)
    {
        this.centerCameraOnPlayer = centerCameraOnPlayer;
    }

    public boolean isCenterCameraOnPlayer()
    {
        return centerCameraOnPlayer;
    }

    public float getDefaultZoom()
    {
        return DEFAULT_ZOOM;
    }

    public boolean ismLocationPermissionGranted()
    {
        return mLocationPermissionGranted;
    }
}
