package grp1.grp1;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private HeatmapTileProvider mProvider;
    private TileOverlayOptions mOverlay;

    Button currentl, heatmapb1;
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
        heatmapb1 = (Button) findViewById(R.id.hmb);
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
                    //laview.setText(""+latitude+"");
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
                double latitude1, longitude1;
                latitude1 = 50.8388481140;
                longitude1 = -0.1175390035;
                String link = "http://ec2-35-176-136-57.eu-west-2.compute.amazonaws.com:/price-data/get\\?lat="+latitude1+"&long="+longitude1+"&distance=2";
                new GetDataTask().execute(link);

                //JSONArray jsonArray = jsonArrayParser.getJSONFromUrl()
            };
        });
    }



    private void addHeatMap(){
        ArrayList<LatLng> locations = new ArrayList();
        locations.add(new LatLng(50.822530, -0.137163));
        locations.add(new LatLng(50.823064, -0.138692));
        locations.add(new LatLng(50.819742, -0.138370));

        mProvider = new HeatmapTileProvider.Builder().data(locations).build();
        mProvider.setRadius(HeatmapTileProvider.DEFAULT_RADIUS);
        mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
        // mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        //method displays University of Sussex on the map on app startup
        mMap = googleMap;
        LatLng uos = new LatLng(50.867090, -0.087914);
        mMap.addMarker(new MarkerOptions().position(uos).title("University of Sussex"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(uos));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(uos,12));
        addHeatMap();
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
            JSONArray jsonArray = null;
            //connect to server
            try{
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(10000);
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
                  //  JSONObject jo = new JSONObject();
                   // jsonArray.put(jo);

                //}
                jsonArray = new JSONArray(line);
                if(jsonArray!=null&&jsonArray.length()>0){
                    System.out.println("-------------------------");
                }else if(jsonArray==null){
                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                }
                //JSONObject jsonObject = new JSONObject(line);
                //jsonArray = jsonObject.getJSONArray("postcode");
                //jsonArray = jsonArray(result.toString());


               // System.out.println(jsonArray);
            }catch (IOException e){
                return "Network error!";
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = jsonArray.getJSONObject(1);
                System.out.println(jsonObject.toString());
                //System.out.println(jsonObject.getString("postocde"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //set textview
            laview.setText(result);

            if (progressDialog!=null){
                progressDialog.dismiss();
            }
        }
    }
}
