package grp1.grp1;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import java.util.concurrent.ExecutionException;

/**
 * Created by Din on 20/10/2017.
 */

public class GPS extends Service implements LocationListener {
    //class to handle location and gps services

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private final Context context;
    boolean GPSenabled = false;
    boolean Nenabled = false;
    boolean getLocation = false;
    double latitude;
    double longitude;
    Location location;


    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_UPDATES = 1000 * 60 * 1;

    protected LocationManager locationManager;

    public GPS(Context context) {
        this.context = context;
        getLocation();
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            GPSenabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            Nenabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!GPSenabled && !Nenabled) {
            } else {
                this.getLocation = true;
                if (Nenabled) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                    }
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if(location!=null){
                        latitude= location.getLatitude();
                        longitude= location.getLongitude();
                    }
                }
            }
            if(GPSenabled){
                if(location==null) {

                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER, MIN_TIME_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    public void GPSstop(){
        if(locationManager!=null){
            locationManager.removeUpdates(GPS.this);
        }
    }

    public double getLatitude(){
        if(location !=null){
            latitude = location.getLatitude();
        }
        return latitude;
    }

     public double getLongitude(){
         if(location !=null){
             longitude = location.getLongitude();
         }
         return longitude;
     }

     public boolean cgetLocation(){
         return this.getLocation;
     }

     public void settingAlert(){
         AlertDialog.Builder alertDialog  = new AlertDialog.Builder(context);
         alertDialog.setTitle("GPS settings");
         alertDialog.setMessage("GPS not enabled");
         alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener(){
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                 context.startActivity(intent);
             }
         });
         alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 dialog.cancel();
             }
         });
         alertDialog.show();
     }
}
