package com.example.android.booksharing.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;

import com.example.android.booksharing.Fragments.Messages;
import com.example.android.booksharing.Fragments.MessagesList;
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
 * Created by Sergio on 12/6/16.
 */
public class LoadUserPublicationsAsyncTask extends AsyncTask<String,Void,String> {
    private Context context;
    private String username;

    public LoadUserPublicationsAsyncTask(Context context) {
        this.context = context;
    }

    protected void onPreExecute() {

    }

    protected String doInBackground(String... arg0) {
        username = (String) arg0[0];

        try {
            String link = "https://booksharing-sergiolazaro.rhcloud.com/loadUserPublications.php";
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
            JSONObject json = new JSONObject(line);
            JSONArray jsonArray = json.getJSONArray("publications");   //Getting JSON array
            ArrayList<String> titlesArray = new ArrayList<String>();
            ArrayList<Integer> idsArray = new ArrayList<Integer>();
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject elem = jsonArray.getJSONObject(i);
                int publicationID = elem.getInt("publicationID");
                String title = elem.getString("title");
                titlesArray.add(title);
                idsArray.add(publicationID);
            }

            MessagesList.setSpinner(titlesArray,idsArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
