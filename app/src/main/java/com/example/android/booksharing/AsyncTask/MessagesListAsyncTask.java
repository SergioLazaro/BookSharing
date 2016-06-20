package com.example.android.booksharing.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.android.booksharing.Activities.MainActivity;
import com.example.android.booksharing.Fragments.ListBooks;
import com.example.android.booksharing.Fragments.MessagesList;
import com.example.android.booksharing.Objects.Message;
import com.example.android.booksharing.Objects.Publication;
import com.example.android.booksharing.Objects.UserComment;

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
public class MessagesListAsyncTask extends AsyncTask<String,Void,String> {
    private Context context;
    private String username;
    private MessagesList.messageListCallBack fragmentCallback;
    private ProgressDialog dialog;

    public MessagesListAsyncTask(Context context, MessagesList.messageListCallBack fragmentCallback) {
        this.context = context;
        this.fragmentCallback = fragmentCallback;
        dialog = new ProgressDialog(context);

    }

    protected void onPreExecute() {
        dialog.setMessage("Loading...");
        dialog.show();
    }

    protected String doInBackground(String... arg0) {

        username = (String) arg0[0];

        try {
            String link = "https://booksharing-sergiolazaro.rhcloud.com/loadConversations.php";
            String data  = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");

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
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            JSONObject json = new JSONObject(line);
            JSONArray jsonArray = json.getJSONArray("messages");   //Getting JSON array
            fragmentCallback.onTaskDone(jsonArray.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
