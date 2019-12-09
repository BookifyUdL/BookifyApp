package com.example.readify;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.example.readify.Models.Genre;
import com.example.readify.Models.ServerCallback;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ApiConnector extends AsyncTask<String, Integer, String> {

    private static String ALL_GENRES = "genres";

    //Context context;
    //RequestQueue queue = Volley.newRequestQueue(context);
    static String urlv ="http://10.0.2.2:3000/";


    public void setContext(Context context){
        //this.context = context;
    }

    public static void getGenres(Context context, final ServerCallback callback){
        final ArrayList<Genre> genres = new ArrayList<>();
        try{
            RequestFuture<JSONObject> jsonObjectRequestFuture = RequestFuture.newFuture();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, urlv + ALL_GENRES, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            String aux = response.toString();
                            try{
                                //String aux2 = response.get("genres");
                                //String aux2 = response.get("genres").toString();

                                JSONArray jsonarray = new JSONArray(response.get("genres").toString());
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    genres.add(new Genre(jsonarray.getJSONObject(i)));
                                    /*JSONObject jsonobject = jsonarray.getJSONObject(i);
                                    String name = jsonobject.getString("name");
                                    String url = jsonobject.getString("url");*/
                                }
                                MockupsValues.setGenres(genres);

                                callback.onSuccess(response);
                                //aux2 = "";
                            } catch (org.json.JSONException e) {

                                System.out.println("Error");

                            }
                            //textView.setText("Response: " + response.toString());
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error
                            System.out.println("Error");

                        }
                    });

            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(jsonObjectRequest);
            queue.start();
            //Wait_until_Downloaded();
            //jsonObjectRequestFuture.get(30, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.out.println(e);
        }
        //return genres;
    }


    @Override
    protected String doInBackground(String... urlString) {
        ArrayList<Genre> genres = new ArrayList<>();
        try {
            URL url = new URL(urlv + ALL_GENRES);
            StringBuilder buffer = new StringBuilder();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            //connection.setDoOutput(true);
            //connection.setConnectTimeout(5000);
            //connection.setReadTimeout(5000);
            connection.connect();
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String content = "", line;
            while ((line = rd.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if(buffer.length() == 0)
                return null;

            JSONObject jsonObj = new JSONObject(buffer.toString());
            try{
                //String aux2 = response.get("genres");
                JSONArray jsonarray = new JSONArray(jsonObj.get("genres"));
                for (int i = 0; i < jsonarray.length(); i++) {
                    genres.add(new Genre(jsonarray.getJSONObject(i)));
                                    /*JSONObject jsonobject = jsonarray.getJSONObject(i);
                                    String name = jsonobject.getString("name");
                                    String url = jsonobject.getString("url");*/
                }
                //aux2 = "";
            } catch (org.json.JSONException e) {

            }
            //shareeditflag
            //System.out.println(buffer);
            MockupsValues.setGenres(genres);
            return content;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
