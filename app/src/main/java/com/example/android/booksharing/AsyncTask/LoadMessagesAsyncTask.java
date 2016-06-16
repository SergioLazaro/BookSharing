package com.example.android.booksharing.AsyncTask;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.android.booksharing.Activities.MainActivity;
import com.example.android.booksharing.Fragments.ListBooks;
import com.example.android.booksharing.Fragments.Messages;
import com.example.android.booksharing.Objects.Message;
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
 * Created by Sergio on 6/6/16.
 */
public class LoadMessagesAsyncTask extends AsyncTask<String,Void,String> {
    private Context context;
    private String sender, receiver, publicationID;
    private Messages.messageCallBack fragmentCallback;

    public LoadMessagesAsyncTask(Context context, Messages.messageCallBack fragmentCallback) {
        this.context = context;
        this.fragmentCallback = fragmentCallback;
    }

    protected void onPreExecute() {

    }

    protected String doInBackground(String... arg0) {
        sender = (String) arg0[0];
        receiver = (String) arg0[1];
        publicationID = (String) arg0[2];

        try {
            String link = "https://booksharing-sergiolazaro.rhcloud.com/loadMessages.php";
            String data  = URLEncoder.encode("sender", "UTF-8") + "=" + URLEncoder.encode(sender, "UTF-8");
            data += "&" + URLEncoder.encode("receiver", "UTF-8") + "=" + URLEncoder.encode(receiver, "UTF-8");
            data += "&" + URLEncoder.encode("publicationID", "UTF-8") + "=" + URLEncoder.encode(publicationID, "UTF-8");

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
            JSONArray jsonArray = json.getJSONArray("messages");   //Getting JSON array
            fragmentCallback.onTaskDone(jsonArray.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
