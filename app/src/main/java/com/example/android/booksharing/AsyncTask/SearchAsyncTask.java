package com.example.android.booksharing.AsyncTask;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.example.android.booksharing.Activities.MainActivity;
import com.example.android.booksharing.Fragments.BookInfo;
import com.example.android.booksharing.Fragments.ListBooks;
import com.example.android.booksharing.Objects.Publication;
import com.example.android.booksharing.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sergio on 16/6/16.
 */
public class SearchAsyncTask extends AsyncTask<String,Void,String> {

    private Context context;
    private String query, username;
    private MainActivity.SearchCallBack mFragmentCallback;

    public SearchAsyncTask(Context context, MainActivity.SearchCallBack fragmentCallback) {
        this.context = context;
        this.mFragmentCallback = fragmentCallback;
    }

    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... arg0) {

        query = (String)arg0[0];
        username = (String) arg0[1];

        try {
            String link = "https://booksharing-sergiolazaro.rhcloud.com/search.php";
            String data  = URLEncoder.encode("query", "UTF-8") + "=" + URLEncoder.encode(query, "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    protected void onPostExecute(String line){
        try {
            JSONObject json = new JSONObject(line);
            String type = json.getString("type");
            JSONArray jsonArray = json.getJSONArray("result");   //Getting JSON array
            if(jsonArray.length() > 0){
                mFragmentCallback.onTaskDone(type,jsonArray.toString());
            }
            else{   //Empty results
                Toast.makeText(context,"No results found",Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}
