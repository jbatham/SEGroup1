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
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

/**
 * Created by Din on 20/10/2017.
 */

//Class to get GPS location
public class GPS implements LocationListener {
    //class to handle location and gps services
    Context context;

    //method called which returns location
    public Location getLocation(){
        //checking if user has given location access permission
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            Toast.makeText(context,"Location Access not grandted", Toast.LENGTH_LONG).show();
            return null;
        }
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSenabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);  //checking if GPS enabled
        if(isGPSenabled){
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, this);
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return location;
        }else{
            Toast.makeText(context,"Please enable GPS", Toast.LENGTH_LONG).show();
        }
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public GPS(Context c){
        context = c;
    }


}