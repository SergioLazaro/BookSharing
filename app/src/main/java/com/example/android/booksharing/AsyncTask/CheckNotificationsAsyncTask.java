package com.example.android.booksharing.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.booksharing.Activities.MainActivity;
import com.example.android.booksharing.Fragments.Messages;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Sergio on 16/6/16.
 */
public class CheckNotificationsAsyncTask extends AsyncTask<String,Void,String> {
    private Context context;
    private String username;
    private MainActivity.CheckNotifications fragmentCallback;

    public CheckNotificationsAsyncTask(Context context, MainActivity.CheckNotifications fragmentCallback) {
        this.context = context;
        this.fragmentCallback = fragmentCallback;
    }

    protected void onPreExecute() {

    }

    protected String doInBackground(String... arg0) {
        username = (String) arg0[0];

        try {
            String link = "https://booksharing-sergiolazaro.rhcloud.com/checkNotifications.php";
            String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");

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


    protected void onPostExecute(String line) {
        try {
            JSONObject json = new JSONObject(line);
            boolean notifications = Boolean.valueOf(json.getString("notifications"));
            fragmentCallback.onTaskDone(notifications);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
