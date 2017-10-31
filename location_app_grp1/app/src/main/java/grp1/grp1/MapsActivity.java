package grp1.grp1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.RotateDrawable;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    Button currentl;
    GPS gps;
    TextView laview, lnview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        currentl = (Button) findViewById(R.id.clb);
        laview = (TextView) findViewById(R.id.latview);
        lnview = (TextView) findViewById(R.id.lngview);


        currentl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps = new GPS(MapsActivity.this);
                double latitude, longitude;
                if(gps.cgetLocation()){
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    laview.setText(""+latitude+"");
                    lnview.setText(""+longitude+"");
                    mMap.clear();
                    LatLng latlng = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions().position(latlng).title("Current Position"));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 10));

                    //pushing to database
                    String latstr= Double.toString(latitude);
                    String langstr= Double.toString(longitude);
                    String didstr = Integer.toString(1);
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");

                                if(success){
                                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    };
                    Dbreq dbreq = new Dbreq("232323","23232323", didstr, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(MapsActivity.this);
                    queue.add(dbreq);
                }else{
                    gps.settingAlert();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //method displays University of Sussex on the map on app startup
        mMap = googleMap;
        LatLng uos = new LatLng(50.867090, -0.087914);
        mMap.addMarker(new MarkerOptions().position(uos).title("University of Sussex"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(uos));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(uos,12));
    }
}
