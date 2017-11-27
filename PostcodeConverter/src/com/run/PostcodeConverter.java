package com.run;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PostcodeConverter {

    private final String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args) throws Exception {
        //initialise test object
        PostcodeConverter test = new PostcodeConverter();
        
        
        test.processCsvSingle();
        //test.sendGet("BN2 9UF");
        //test.filterCsv();
    }

    //filterCsv - Method to filter price paid data to records starting with 'BN'
    public void filterCsv() throws FileNotFoundException, IOException {
        
        //initialising reader and writer to input and output documents
        CSVReader reader;
        reader = new CSVReader(new FileReader("C:\\Users\\Jake\\Documents\\NetBeansProjects\\JavaApplication11\\src\\javaapplication11\\pp-complete.csv"), ',', '"', 1);
        CSVWriter writer = new CSVWriter(new FileWriter("C:\\Users\\Jake\\Documents\\NetBeansProjects\\JavaApplication11\\src\\javaapplication11\\new.csv"));

        
        String[] nextLine;
        //while document hasn't ended - read next line
        while ((nextLine = reader.readNext()) != null) {
            //get postcode and price info from line
            String postcode = nextLine[3];
            String price = nextLine[1];
            
            //if postcode is 'BN'
            if (postcode.startsWith("BN")) {
                //build record to be added to the output doc
                String[] row = {postcode, price};
                //write record to output
                writer.writeNext(row);
            }
        }
    }
    
    public void processCsvSingle() throws FileNotFoundException, IOException, Exception {
        //initialise reader and writer
        //to target csv file and output csv file
        CSVReader reader = new CSVReader(new FileReader("C:\\\\Users\\\\Jake\\\\Documents\\\\NetBeansProjects\\\\JavaApplication11\\\\src\\\\javaapplication11\\\\old.csv"), ',', '"', 1);
        CSVWriter writer = new CSVWriter(new FileWriter("C:\\Users\\Jake\\Documents\\NetBeansProjects\\JavaApplication11\\src\\javaapplication11\\new.csv"));
        
        
        String[] nextLine;
        //while document hasn't ended - set nextLine variable to hold next line
        while ((nextLine = reader.readNext()) != null) {

            try {
                //
                String postcode = nextLine[0];
                String[] longlatResult = {"", ""};
                
                //process postcode if its of valid length
                if (postcode.length() > 6) {
                    //send get request to be handled
                    longlatResult = sendGet(postcode);
                }
                
                //get longitude and latitude from the result
                String longitude = longlatResult[0]; 
                String latitude = longlatResult[1];
                
                //format long and lat string to be stored numerically
                String longreplace = longitude.replace("\"longitude\":", "");
                String latreplace = latitude.replace("\"latitude\":", "");
                
                //get price from the current line
                String price = nextLine[1];
                
                //prepare csv line to be output
                String[] row = {postcode, longreplace, latreplace, price};
                //write line to the output doc
                writer.writeNext(row);

            } catch (FileNotFoundException e) {
            }
        }
    }
    
    public void processCsvBatch() throws FileNotFoundException, IOException {
        CSVReader reader = new CSVReader(new FileReader("C:\\\\Users\\\\Jake\\\\Documents\\\\NetBeansProjects\\\\JavaApplication11\\\\src\\\\javaapplication11\\\\old.csv"), ',', '"', 1);
        CSVWriter writer = new CSVWriter(new FileWriter("C:\\Users\\Jake\\Documents\\NetBeansProjects\\JavaApplication11\\src\\javaapplication11\\new.csv"));

        for (int i = 0; i < 100; i++) {
            
        }
    }

    // HTTP GET request
    private String[] sendGet(String postcode) throws Exception {
        String[] resultArray;

        //Setting up connection
        String url = "https://api.postcodes.io/postcodes/" + postcode;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);
        //get and print response code
        int responseCode = con.getResponseCode();
        System.out.println("Response Code : " + responseCode);
        
        //initialise string building variables
        StringBuilder sb;
        
        //get the input stream
        try (BufferedReader in = new BufferedReader(
            new InputStreamReader(con.getInputStream()))) {
            //initialise input string variable and input buffer
            String inputLine;
            sb = new StringBuilder();
            //while input reader hasn't ended
            while ((inputLine = in.readLine()) != null) {
                //append the line to the stringbuilder
                sb.append(inputLine);
                System.out.println(inputLine);
            }

            //Splitting the result string by commas
            String[] result = sb.toString().split(",");

            //Outputting the lat and long as an array
            System.out.println(result[7]);
            System.out.println(result[8]);
            
            resultArray = new String[2];
            resultArray[0] = result[7];
            resultArray[1] = result[8];
            return resultArray;
        }
    }
}
