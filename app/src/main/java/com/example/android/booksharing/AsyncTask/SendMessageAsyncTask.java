package com.example.android.booksharing.AsyncTask;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.android.booksharing.Activities.MainActivity;
import com.example.android.booksharing.Fragments.Messages;
import com.example.android.booksharing.Objects.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Sergio on 7/6/16.
 */
public class SendMessageAsyncTask extends AsyncTask<String,Void,String> {
    private Context context;
    private String sender, receiver, message, date, publicationID;

    public SendMessageAsyncTask(Context context) {
        this.context = context;
    }

    protected void onPreExecute() {

    }

    protected String doInBackground(String... arg0) {
        sender = (String) arg0[0];
        receiver = (String) arg0[1];
        message = (String) arg0[2];
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy H:m:ss");
        date = formatter.format(today);
        publicationID = (String) arg0[3];
        try {
            String link = "https://booksharing-sergiolazaro.rhcloud.com/storeMessage.php";
            String data  = URLEncoder.encode("sender", "UTF-8") + "=" + URLEncoder.encode(sender, "UTF-8");
            data += "&" + URLEncoder.encode("receiver", "UTF-8") + "=" + URLEncoder.encode(receiver, "UTF-8");
            data += "&" + URLEncoder.encode("message", "UTF-8") + "=" + URLEncoder.encode(message, "UTF-8");
            data += "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8");
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
            boolean result = Boolean.parseBoolean(json.getString("result"));
            if(!result){
                Toast.makeText(context, "Comment cannot be added successfuly", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "Message sent to " + receiver, Toast.LENGTH_SHORT).show();
                new LoadMessagesAsyncTask(context).execute(sender, receiver,publicationID);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
