package grp1.grp1;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Spinner;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
import java.util.Collection;
import java.util.List;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private HeatmapTileProvider smProvider;
    private HeatmapTileProvider mmProvider;
    private HeatmapTileProvider hmProvider;
    private TileOverlay mOverlay;

    Button currentl, heatmapb1, crimeb;
    Spinner spinnerm, spinnery;
    GPS gps;
    TextView laview, lnview;
    JSONArray jsonArray, jsonArray2;
    //ArrayList<LatLng> slocations = new ArrayList();   //array for fixed (non dynamic) heatmap, small price
   // ArrayList<LatLng> mlocations = new ArrayList();   //array for fixed (non dynamic) heatmap, medium price
   // ArrayList<LatLng> hlocations = new ArrayList();   //array for fixed (non dynamic) heatmap high price
    ArrayList<LatLng> clocations = new ArrayList(); //store crime location
    ArrayList<CData> cdata = new ArrayList<>();     //store crime data
    ArrayList<CData> cdata2 = new ArrayList<>();    //store crime data
    ArrayList<Marker> markerremove = new ArrayList<>();     //store markers to remove
    ArrayList<WeightedLatLng> wlocations = new ArrayList();     //weighted locations for dynamic heatmap
    private HeatmapTileProvider mProvider;

    double defaultlat = 50.82253000000001, defaultlong = -0.13716299999998682;  //default location if not sending latlong to app

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
        spinnerm = (Spinner) findViewById(R.id.cmonth);
        spinnery = (Spinner) findViewById(R.id.cyear);
        crimeb = (Button) findViewById(R.id.cdatab);


        //get current location
        ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 111);
        currentl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //calling GPS class method
                gps = new GPS(MapsActivity.this);
                double latitude, longitude;
                Location location = gps.getLocation();
                if(location!=null){
                    //latitude = 50.82253000000001;
                   // longitude = -0.13716299999998682;
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    laview.setText(""+latitude+"");
                    lnview.setText("" + longitude + "");
                    defaultlat = latitude;
                    defaultlong = longitude;
                    //displaying current location
                    mMap.clear();
                    LatLng latlng = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions().position(latlng).title("Current Position").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 12));
                }
            }
        });

        //heatmap button: get json from edited link and process data through GetDataTask() method
        heatmapb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String link = "http://ec2-35-176-136-57.eu-west-2.compute.amazonaws.com:/price-data/get\\?lat="+defaultlat+"&long="+defaultlong+"&distance=2";
                new GetDataTask().execute(link);
            };
        });

        //crime button: get json from edited link(applying selected date) and process data through GetCrimeData() method
        crimeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String month, year;
                month = String.valueOf(spinnerm.getSelectedItem());
                year = String.valueOf(spinnery.getSelectedItem());
                String link = "https://data.police.uk/api/crimes-street/all-crime?lat="+defaultlat+"&lng="+defaultlong+"&date="+year+"-"+month;
                new GetCrimeData().execute(link);
            }
        });
    }

    //processing house price data and storing on wlocations array
    private void addLocation() {
        //slocations.clear();
        //mlocations.clear();
        //hlocations.clear();
        wlocations.clear();
        if(jsonArray.length()!=0){
            for (int i = 0; i < 3000; i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int price = jsonObject.getInt("price");
                    double lat = jsonObject.getDouble("lat");
                    double lng = jsonObject.getDouble("lng");
                    wlocations.add(new WeightedLatLng(new LatLng(lat, lng), Math.log(price)));
                    /*if(price<=100000){
                        slocations.add(new LatLng(lat,lng));
                    }else if((price>100000)&&(price<=200000)){
                        mlocations.add(new LatLng(lat,lng));
                    }else if(price>200000){
                        hlocations.add(new LatLng(lat,lng));
                    }*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //processing crime data and storing on cdata array
    private void addCrime(){
        cdata2= cdata;
        cdata.clear();
        int jsonArray2length;
        //remove if statement for full crime data, keep else statement
        if(jsonArray2.length()>500){
            jsonArray2length=500;
        }else{
            jsonArray2length=jsonArray2.length();
        }
        if(jsonArray2.length()!=0){
            for(int i=0; i<jsonArray2length; i++){//jsonArray2.length()
                try{
                    JSONObject jsonObject = jsonArray2.getJSONObject(i);
                    String catogery = jsonObject.getString("category");
                    JSONObject jsonObject1 = jsonObject.getJSONObject("location");
                    double lat = jsonObject1.getDouble("latitude");
                    double lng = jsonObject1.getDouble("longitude");
                    //System.out.println(lat);
                    clocations.add(new LatLng(lat,lng));
                    cdata.add(new CData((new LatLng(lat,lng)), catogery));
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }

    //adding crime marker on map after removing previous markers
    public void addCrimeMarker(){
        if(cdata2.size()!=0){
            for(int i=0; i<markerremove.size();i++){
                Marker marker2 = markerremove.get(i);
                marker2.remove();
            }
        }
        LatLng curLocation = new LatLng(defaultlat,defaultlong);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curLocation,14));
        markerremove.clear();
        for(int i=0; i<cdata.size(); i++) {
            CData cdatalist = cdata.get(i);
            //mMap.addMarker(new MarkerOptions().position(cdatalist.clatlng).title(cdatalist.cat));
            Marker marker = mMap.addMarker(new MarkerOptions().position(cdatalist.clatlng).title(cdatalist.cat).alpha(0.5f).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_name)));
            markerremove.add(marker);
        }
    }

    //adding weighted heatmap
    private void addHeatMap(){
        int[] colors = {Color.rgb(0,25,240), Color.rgb(0,255,0), Color.rgb(240,255,0), Color.rgb(255,0,0)};
        float[] startPoints = {
                0.2f, 0.4f, 0.6f, 1f
        };
        Gradient gradient = new Gradient(colors, startPoints);
        LatLng curLocation = new LatLng(defaultlat,defaultlong);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(curLocation));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curLocation,13));
        mProvider = new HeatmapTileProvider.Builder().weightedData(wlocations).gradient(gradient).radius(27).opacity(0.9).build();
        mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));

        //below code for non dynamic and fixed heatmap
        /*int[] colors = {Color.rgb(102,225,0), Color.rgb(102,225,0)}; //green
        int[] colors2 = {Color.rgb(225,225,0), Color.rgb(225,225,0)}; //yellow
        int[] colors3 = {Color.rgb(225,0,0), Color.rgb(225,0,0)}; //red
        float[] startPoints = {
                0.2f, 1f
        };
        Gradient gradient1 = new Gradient(colors, startPoints);
        Gradient gradient2 = new Gradient(colors2, startPoints);
        Gradient gradient3 = new Gradient(colors3, startPoints);
        if(!slocations.isEmpty()) {
            smProvider = new HeatmapTileProvider.Builder().data(slocations).gradient(gradient1).build();
            mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(smProvider));
        }
        if(!mlocations.isEmpty()) {
            mmProvider = new HeatmapTileProvider.Builder().data(mlocations).gradient(gradient2).build();
            mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mmProvider));
        }
        if(!hlocations.isEmpty()) {
            hmProvider = new HeatmapTileProvider.Builder().data(hlocations).gradient(gradient3).build();
            mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(hmProvider));
        }*/
    }


    //on start
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //method displays University of Sussex on the map on app startup
        mMap = googleMap;
        LatLng uos = new LatLng(50.867090, -0.087914);
        mMap.addMarker(new MarkerOptions().position(uos).title("University of Sussex").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(uos));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(uos,12));
    }

    //posting data and reading input from server for house data
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
                jsonArray = new JSONArray(line);
                addLocation();
            }catch (IOException e){
                return "Network error!";
            } catch (JSONException e) {
                e.printStackTrace();
            }
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


    //posting data and reading input from server for crime data
    class GetCrimeData extends AsyncTask<String, Void, String> {
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
                jsonArray2 = new JSONArray(line);
                System.out.println("-------------------------");
                addCrime();
            }catch (IOException e){
                return "Network error!";
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = jsonArray2.getJSONObject(1);
                System.out.println(jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            addCrimeMarker();
            //addHeatMap();
            if (progressDialog!=null){
                progressDialog.dismiss();
            }
        }
    }

}