package com.example.android.booksharing.AsyncTask;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.android.booksharing.Activities.MainActivity;
import com.example.android.booksharing.Fragments.ListBooks;
import com.example.android.booksharing.Objects.Publication;

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
 * Created by Sergio on 4/6/16.
 */
public class RefreshListAsyncTask extends AsyncTask<String,Void,String> {

    private Context context;
    private String type, caller, username;
    ListBooks.refreshCallBack refreshCallBack;

    public RefreshListAsyncTask(Context context, ListBooks.refreshCallBack refreshCallBack) {
        this.context = context;
        this.refreshCallBack = refreshCallBack;

    }

    protected void onPreExecute() {

    }

    protected String doInBackground(String... arg0) {

        type = (String) arg0[0];
        caller = (String) arg0[1];

        try {
            String link = "";
            String data = "";
            if(caller.equals("main")){
                link = "https://booksharing-sergiolazaro.rhcloud.com/list.php";
                data  = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8");
            }
            else{
                username = (String) arg0[2];
                link = "https://booksharing-sergiolazaro.rhcloud.com/getPublishedBooks.php";
                data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
                data += "&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8");
            }

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


    protected void onPostExecute(String line){
        try {
            JSONObject json = new JSONObject(line);
            JSONArray jsonArray = json.getJSONArray("posts");   //Getting JSON array
            refreshCallBack.onTaskDone(jsonArray.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
