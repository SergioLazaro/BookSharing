package com.example.android.booksharing.AsyncTask;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.android.booksharing.Activities.MainActivity;
import com.example.android.booksharing.Objects.Publication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Sergio on 3/6/16.
 */
public class PublishAsyncTask extends AsyncTask<String,Void,String>{

    private String json;
    private JSONObject publication;

    private Context context;

    public PublishAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected String doInBackground(String... arg0) {

        String tmpString = (String) arg0[0];

        try {
            publication = new JSONObject(tmpString);
            String link = "https://booksharing-sergiolazaro.rhcloud.com/publishbook.php";
            String data  = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(publication.getString("username"), "UTF-8");
            data += "&" + URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(publication.getString("title"), "UTF-8");
            data += "&" + URLEncoder.encode("author", "UTF-8") + "=" + URLEncoder.encode(publication.getString("author"), "UTF-8");
            data += "&" + URLEncoder.encode("description", "UTF-8") + "=" + URLEncoder.encode(publication.getString("description"), "UTF-8");
            data += "&" + URLEncoder.encode("rate", "UTF-8") + "=" + URLEncoder.encode(publication.getString("rate"), "UTF-8");
            data += "&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(publication.getString("type"), "UTF-8");

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
            boolean result = Boolean.parseBoolean(json.getString("result"));
            if(!result){
                Toast.makeText(context, "Unable to connect.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "Publication added successfuly", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context,MainActivity.class);
                intent.putExtra("username", publication.getString("username"));
                context.startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
