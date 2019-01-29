package com.wisekrakr.david.wiseweather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

class DataGetter {

    private final static String WEATHER_API = "https://openweathermap.org/data/2.5/weather?"; //sign up and get a key
    private final static String PARAM_QUERY = "q=";
    private final static String apiKey = Utils.KEY; //your personal api key

    private static String data = "";
    private static String singleParsed = "";
    private static String dataParsed = "";

    static URL buildUrl(String searchQuery) {

        URL url = null;
        try{
            url = new URL(WEATHER_API + PARAM_QUERY + searchQuery +"&units=metric" + apiKey);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        return url;
    }

    static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static String getDataParsed(String string) {

        data = data + string;

        JSONObject jsonObject;
        try {

            jsonObject = new JSONObject(data);

            singleParsed = "Name: " + jsonObject.get("name") + "\n" + "\n" +
                     createJSONMainString(jsonObject) + "\n" +
                    createJSONWindString(jsonObject) + "\n" +
                    createJSONWeatherString(jsonObject) + "\n";

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return singleParsed;
    }

    private static String createJSONMainString(JSONObject jsonObject) throws JSONException {

        JSONObject mainObject = jsonObject.getJSONObject("main");

        String main = "Current Temp.: " + mainObject.get("temp") + "\n" +
                "Humidity: " + mainObject.get("humidity") + "\n" +
                "Min. Temp.: " + mainObject.get("temp_min") + "\n" +
                "Max. Temp.: " + mainObject.get("temp_max") + "\n";

        return main;
    }

    private static String createJSONWeatherString(JSONObject jsonObject) throws JSONException {

        JSONArray jsonArray = jsonObject.getJSONArray("weather");
        String weather = "";

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject weatherObject = (JSONObject) jsonArray.get(i);
            weather = "Mainly: " + weatherObject.get("main") + "\n" +
                    "Description: " + weatherObject.get("description") + "\n";
        }

        return weather;
    }

    private static String createJSONWindString(JSONObject jsonObject) throws JSONException {

        JSONObject mainObject = jsonObject.getJSONObject("wind");

        String main = "Wind speed: " + mainObject.get("speed") + "\n";

        return main;
    }

}
