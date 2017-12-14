package grp1.grp1;

/**
 * Created by Din on 11/12/2017.
 */


import android.app.ProgressDialog;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;



public class Crimedata {
    private final static String USER_AGENT = "Mozilla/5.0";

    public ArrayList sendGet(LatLng latlng, String date) throws Exception {
        // ArrayList<LatLng> crimelatlng = new ArrayList();
        //ArrayList<String> crimeCat = new ArrayList<>();

        ArrayList<CData> cdata = new ArrayList<>();


        double lat = latlng.latitude;
        double lng = latlng.longitude;

        String url = "https://data.police.uk/api/crimes-street/all-crime?lat=50.822530&lng=-0.137163&date=2017-01";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        System.setProperty("http.agent", "");
        //con.setRequestProperty("User-Agent", "");

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        StringBuffer response;
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                response.append(inputLine);
            }
        }

        //splitting string by commas
        String[] resultArray = response.toString().split(",");

        //iterate over the array
        for (int i = 0; i < resultArray.length; i++) {
            //|| !resultArray[i].contains("hello")
            //System.out.println(resultArray[i]);
            if (resultArray[i].contains("category") && !resultArray[i].contains("outcome_status")) {
                String category = resultArray[i];
                String catRemoveBracket = category.replaceAll("\\{", " ");
                String catReplace = catRemoveBracket.replace("\"category\":\"", "");
                String catFinal = catReplace.replace("\"", "");

                //getting latitude string and replacing all non-numeric characters
                String latitude = resultArray[i + 2];
                String latReplace = latitude.replaceAll("[^\\d.]", "");

                //getting longitude string and replacing non-numerics
                String longitude = resultArray[i + 5].replaceAll("[^\\d.]", "");
                String longreplace = "-" + longitude;

                System.out.println(catFinal + " " + latReplace + " " + longreplace);
                //crimelatlng.add(new LatLng(Double.parseDouble(latReplace), Double.parseDouble(longreplace)));
                //crimeCat.add(catFinal);
                cdata.add(new CData((new LatLng(Double.parseDouble(latReplace), Double.parseDouble(longreplace))), catFinal));
            }
        }
        return cdata;
    }

}
