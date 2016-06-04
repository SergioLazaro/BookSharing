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
    private String type;

    public RefreshListAsyncTask(Context context) {
        this.context = context;
    }

    protected void onPreExecute() {

    }

    protected String doInBackground(String... arg0) {

        type = (String) arg0[0];
        try {
            String link = "https://booksharing-sergiolazaro.rhcloud.com/list.php";
            String data  = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8");

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
            Log.i("RESULT:",line);
            JSONObject json = new JSONObject(line);
            JSONArray jsonArray = json.getJSONArray("posts");   //Getting JSON array
            ArrayList<Publication> array = new ArrayList<Publication>();
            ArrayList<HashMap<String,String>> infoToShow = new ArrayList<HashMap<String,String>>();
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject elem = jsonArray.getJSONObject(i);
                Publication p = generatePublication(elem);
                array.add(p);
                //Populating info to show
                HashMap<String, String> din = new HashMap<String, String>(2);
                din.put("User","User: " + p.getUsername());
                din.put("Book",p.getTitle() + " - " + p.getAuthor());
                infoToShow.add(din);
            }
            //Modify the adapter to show publications dynamically
            SimpleAdapter arrayAdapter = new SimpleAdapter(context, infoToShow, android.R.layout.two_line_list_item ,
                    new String[] { "Book","User" },
                    new int[] {android.R.id.text1, android.R.id.text2});
            ListBooks.setAdapter(arrayAdapter, array);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Publication generatePublication(JSONObject elem){
        try {
            return new Publication(elem.getString("username"),elem.getString("title"),
                    elem.getString("author"),elem.getString("type"),Float.valueOf(elem.getString("rate")),
                    elem.getString("description"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
