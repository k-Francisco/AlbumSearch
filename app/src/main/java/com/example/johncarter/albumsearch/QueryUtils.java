package com.example.johncarter.albumsearch;

import android.text.TextUtils;
import android.util.Log;

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
import java.nio.charset.Charset;
import java.util.ArrayList;


/**
 * Created by john carter on 2/12/2017.
 */

public class QueryUtils {

    public QueryUtils() {
    }

    public static ArrayList<album> fetchAlbumData(String requestUrl, int requestCode){

        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        ArrayList<album> albums = new ArrayList<>();
        try{
            jsonResponse = makeHttpRequest(url);

        } catch (IOException e) {
            e.printStackTrace();
        }
        if(requestCode == 1) {
            albums = extractFeatureFromJson(jsonResponse);
        }
        else if(requestCode == 2)
        {
            albums = extractFeatureFromJson2(jsonResponse);
        }
        return albums;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
            }
        } catch (IOException e) {
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static ArrayList<album> extractFeatureFromJson(String albumJson){
        if(TextUtils.isEmpty(albumJson)){
            return null;
        }

        ArrayList<album> albums = new ArrayList<>();
        Log.d("charles",albumJson);

       try{
                JSONObject baseJsonResponse = new JSONObject(albumJson);
                JSONObject albumArray = baseJsonResponse.getJSONObject("topalbums");
                JSONArray secondArray = albumArray.getJSONArray("album");

                for (int i = 0; i < secondArray.length(); i++) {
                    JSONObject currentAlbum = secondArray.getJSONObject(i);

                    String name = currentAlbum.getString("name");
                    String artist = currentAlbum.getJSONObject("artist").getString("name");
                    String url = currentAlbum.getString("url");
                    String image = currentAlbum.getJSONArray("image").getJSONObject(2).getString("#text");

                    album album = new album(name, artist, url, image);
                    albums.add(album);
                }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return albums;
    }

    private static ArrayList<album> extractFeatureFromJson2(String albumJson){
        if(TextUtils.isEmpty(albumJson)){
            return null;
        }

        ArrayList<album> albums = new ArrayList<>();
        Log.d("charles",albumJson);
        try{
            JSONObject baseJsonResponse = new JSONObject(albumJson);
            JSONObject albumArray = baseJsonResponse.getJSONObject("results");
            JSONObject secondArray = albumArray.getJSONObject("albummatches");
            JSONArray thirdArray = secondArray.getJSONArray("album");

            for (int i = 0; i < thirdArray.length(); i++) {
                JSONObject currentAlbum = thirdArray.getJSONObject(i);

                String name = currentAlbum.getString("name");
                String artist = currentAlbum.getString("artist");
                String url = currentAlbum.getString("url");
                String image = currentAlbum.getJSONArray("image").getJSONObject(2).getString("#text");

                album album = new album(name, artist, url, image);
                albums.add(album);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return albums;
    }
}
