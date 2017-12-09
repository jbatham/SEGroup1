package grp1.grp1;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.RotateDrawable;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.health.SystemHealthManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
//    private HeatmapTileProvider smProvider;
//    private HeatmapTileProvider mmProvider;
//    private HeatmapTileProvider hmProvider;
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;

    Button currentl, heatmapb1;
    GPS gps;
    TextView laview, lnview;
    JSONArray jsonArray;
//    ArrayList<LatLng> slocations = new ArrayList();
//    ArrayList<LatLng> mlocations = new ArrayList();
//    ArrayList<LatLng> hlocations = new ArrayList();
    ArrayList<WeightedLatLng> locations = new ArrayList();
    double latitude1, longitude1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        currentl = (Button) findViewById(R.id.clb);
        heatmapb1 = (Button) findViewById(R.id.hmb);
        laview = (TextView) findViewById(R.id.latview);
        lnview = (TextView) findViewById(R.id.lngview);


        currentl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps = new GPS(MapsActivity.this);
                double latitude, longitude;
                if(gps.cgetLocation()){
//                    latitude = gps.getLatitude();
//                    longitude = gps.getLongitude();
                    latitude = 50.8388481140;
                    longitude = -0.1175390035;
                    //laview.setText(""+latitude+"");
                    lnview.setText(""+longitude+"");
//                    mMap.clear();
                    LatLng latlng = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions().position(latlng).title("Current Position"));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 12));

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
                    Dbreq dbreq = new Dbreq(latstr,langstr, didstr, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(MapsActivity.this);
                    queue.add(dbreq);
                }else{
                    gps.settingAlert();
                }
            }
        });


        heatmapb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                double latitude1, longitude1;
                latitude1 = 50.8388481140;
                longitude1 = -0.1175390035;
                String link = "http://ec2-35-176-136-57.eu-west-2.compute.amazonaws.com:/price-data/get\\?lat="+latitude1+"&long="+longitude1+"&distance=1";
                new GetDataTask().execute(link);
            };
        });
    }


    private void addLocation() {
//        slocations.clear();
//        mlocations.clear();
//        hlocations.clear();
        locations.clear();
        System.out.println(jsonArray.length());
        if(jsonArray.length()!=0){
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int price = jsonObject.getInt("price");
                    double lat = jsonObject.getDouble("lat");
                    double lng = jsonObject.getDouble("lng");
                    System.out.println(jsonObject.toString());
                    locations.add(new WeightedLatLng(new LatLng(lat,lng),Math.log(price)));
//                    if(price<=100000){
//                        slocations.add(new LatLng(lat,lng));
//                    }else if((price>100000)&&(price<=200000)){
//                        mlocations.add(new LatLng(lat,lng));
//                    }else if(price>200000){
//                        hlocations.add(new LatLng(lat,lng));
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    private void addHeatMap(){
        int[] colors = {Color.rgb(0,25,240), Color.rgb(0,255,0), Color.rgb(240,255,0), Color.rgb(255,0,0)};
        float[] startPoints = {
                0.2f, 0.4f, 0.6f, 1f
        };
        Gradient gradient = new Gradient(colors, startPoints);
        LatLng curLocation = new LatLng(latitude1,longitude1);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(curLocation));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curLocation,13));
        mProvider = new HeatmapTileProvider.Builder().weightedData(locations).gradient(gradient).radius(38).opacity(0.6).build();
        mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));

//        int[] colors = {Color.rgb(102,225,0), Color.rgb(102,225,0)}; //green
//        int[] colors2 = {Color.rgb(225,225,0), Color.rgb(225,225,0)}; //yellow
//        int[] colors3 = {Color.rgb(225,0,0), Color.rgb(225,0,0)}; //red
//        float[] startPoints = {
//            0.2f, 1f
//        };
//        Gradient gradient1 = new Gradient(colors, startPoints);
//        Gradient gradient2 = new Gradient(colors2, startPoints);
//        Gradient gradient3 = new Gradient(colors3, startPoints);
//        if(!slocations.isEmpty()) {
//            smProvider = new HeatmapTileProvider.Builder().data(slocations).gradient(gradient1).build();
//            mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(smProvider));
//        }
//        if(!mlocations.isEmpty()) {
//            mmProvider = new HeatmapTileProvider.Builder().data(mlocations).gradient(gradient2).build();
//            mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mmProvider));
//        }
//        if(!hlocations.isEmpty()) {
//            hmProvider = new HeatmapTileProvider.Builder().data(hlocations).gradient(gradient3).build();
//            mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(hmProvider));
//        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //method displays University of Sussex on the map on app startup
        mMap = googleMap;
        LatLng uos = new LatLng(50.867090, -0.087914);
//        mMap.addMarker(new MarkerOptions().position(uos).title("University of Sussex"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(uos));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(uos,12));
    }

     class GetDataTask extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MapsActivity.this);
            progressDialog.setMessage("Loading data");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            StringBuilder result = new StringBuilder();
            //connect to server
            try{
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setReadTimeout(10000); //ms
                urlConnection.setConnectTimeout(10000); //ms
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.connect();

                //server response
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                line=bufferedReader.readLine();
               // while((line=bufferedReader.readLine())!=null){
                    //result.append(line).append("\n");
                jsonArray = new JSONArray(line);
                    System.out.println("-------------------------");
                    System.out.println(jsonArray.length());
                addLocation();
            }catch (IOException e){
                return "Network error!";
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //test
            /*try {
                JSONObject jsonObject = jsonArray.getJSONObject(1);
                System.out.println(jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            addHeatMap();
            if (progressDialog!=null){
                progressDialog.dismiss();
            }
        }
    }

}
