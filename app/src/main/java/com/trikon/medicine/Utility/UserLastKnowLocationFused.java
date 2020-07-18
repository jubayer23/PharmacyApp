package com.trikon.medicine.Utility;

import android.app.Activity;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class UserLastKnowLocationFused {

    private FusedLocationProviderClient mFusedLocationClient;

    public UserLastKnowLocationFused(Activity context, final LocationResultFused resultCallBack){
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(context, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            // zoomToSpecificLocation(new LatLng(location.getLatitude(), location.getLongitude()));

                            resultCallBack.gotLocation(location);
                        } else {
                            //location = null means the location service is turned off in user device. Lets ask them to turn it on
                           // dismissProgressDialog();
                            //GpsEnableTool gpsEnableTool = new GpsEnableTool(getActivity());
                            //gpsEnableTool.enableGPs();

                            resultCallBack.gotLocation(null);
                        }
                    }
                });
    }


    public static abstract class LocationResultFused {
        public abstract void gotLocation(Location location);
    }
}
