package com.example.android.booksharing.AsyncTask;

/**
 * Created by Sergio on 02/06/16.
 */
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


import com.example.android.booksharing.Activities.Login;
import com.example.android.booksharing.Activities.MainActivity;
import com.example.android.booksharing.Fragments.ListBooks;
import com.example.android.booksharing.Objects.User;
import com.example.android.booksharing.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


public class LoginAsyncTask  extends AsyncTask<String,Void,String>{
    private Context context;
    private JSONObject user;

    public LoginAsyncTask(Context context) {
        this.context = context;
    }

    protected void onPreExecute() {

    }

    protected String doInBackground(String... arg0) {
        String tmpString = (String) arg0[0];

        try {
            user = new JSONObject(tmpString);
            String link = "https://booksharing-sergiolazaro.rhcloud.com/login.php";
            String data  = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(user.getString("username"), "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(user.getString("password"), "UTF-8");

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
                Toast.makeText(context, "User " + user.getString("username") + " does not exist", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "Welcome " + user.getString("username"), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context,MainActivity.class);
                intent.putExtra("username",user.getString("username"));
                context.startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}
