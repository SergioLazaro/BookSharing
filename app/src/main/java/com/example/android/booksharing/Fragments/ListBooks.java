package com.example.android.booksharing.Fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.booksharing.AsyncTask.CheckNotificationsAsyncTask;
import com.example.android.booksharing.AsyncTask.PublishAsyncTask;
import com.example.android.booksharing.AsyncTask.RefreshListAsyncTask;
import com.example.android.booksharing.Objects.Publication;
import com.example.android.booksharing.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sergio on 1/6/16.
 */
public class ListBooks extends Fragment {

    private String selectedOption = "";
    private Spinner spinner;
    private ListView list;
    private SimpleAdapter arrayAdapter;
    private View view;
    private String username, json, type;
    private ArrayList<Publication> array = new ArrayList<Publication>();
    private ArrayList<HashMap<String,String>> infoToShow = new ArrayList<HashMap<String,String>>();
    private boolean firstTime = true;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(getArguments() != null) {
            username = getArguments().getString("username");
            json = getArguments().getString("json");
            type = getArguments().getString("type");
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.list_books_fragment, container, false);
    }



    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;

        array = new ArrayList<Publication>();
        spinner = (Spinner) view.findViewById(R.id.spinner);
        if(type == null) {  //Calling from MAIN

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                public void onItemSelected(AdapterView<?> arg0, View view,
                                           int position, long id) {
                    selectedOption = spinner.getSelectedItem().toString();
                    RefreshListAsyncTask asyncTask = new RefreshListAsyncTask(view.getContext(), new refreshCallBack() {
                        @Override
                        public void onTaskDone(String json) {
                            populateArrays(json);
                        }

                    });
                    asyncTask.execute(selectedOption,"main");

                }

                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
            list = (ListView) view.findViewById(R.id.listView);
        }
        else{
            //Not called from MAIN (Your_books,SearchToolBar)
            if(type.equals("book") || type.equals("user")) {    //SearchToolBar
                list = (ListView) view.findViewById(R.id.listView);
                hideSpinner();
            }
            else{   //your_books drawer option
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> arg0, View view,
                                               int position, long id) {
                        selectedOption = spinner.getSelectedItem().toString();
                        RefreshListAsyncTask asyncTask = new RefreshListAsyncTask(view.getContext(), new refreshCallBack() {
                            @Override
                            public void onTaskDone(String json) {
                                populateArrays(json);
                            }

                        });
                        asyncTask.execute(selectedOption,"publishedbooks",username);

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
                list = (ListView) view.findViewById(R.id.listView);
            }
        }

        //Setting up ListView
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView adapterView, View view, int position, long id) {
                Publication p = array.get(position);    //Getting clicked element
                //Prepare new Fragment
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment fragment = new BookInfo();
                //Setting arguments
                Bundle arguments = new Bundle();
                arguments.putString("publication", p.generateJSONObject());
                arguments.putString("username",username);
                fragment.setArguments(arguments);
                //Starting fragment transaction
                ft.replace(R.id.fragment_container, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        if(json != null){
            populateArrays(json);
        }
    }

    public interface refreshCallBack {
        public void onTaskDone(String json);
    }

    private void populateArrays(String json){
        array.clear();
        infoToShow.clear();
        try {
            JSONArray jsonArray = new JSONArray(json);
            if(jsonArray.length() > 0){
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
                Toast.makeText(view.getContext(),jsonArray.length() + " results found",
                        Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(view.getContext(),"No results found",Toast.LENGTH_SHORT).show();
            }
            setAdapter(infoToShow,array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Publication generatePublication(JSONObject elem){
        try {
            return new Publication(elem.getInt("publicationID"),elem.getString("username"),
                    elem.getString("title"), elem.getString("author"),elem.getString("type"),
                    Float.valueOf(elem.getString("rate")), elem.getString("description"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    public void setAdapter(ArrayList<HashMap<String,String>> infoToShow, ArrayList<Publication> newArray){
        array = newArray;
        arrayAdapter = new SimpleAdapter(view.getContext(), infoToShow, android.R.layout.two_line_list_item ,
                new String[] { "Book","User" },
                new int[] {android.R.id.text1, android.R.id.text2});
        list.setAdapter(arrayAdapter);
    }

    public void hideSpinner(){
        spinner.setVisibility(View.INVISIBLE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT
        );
        params.setMargins(20, 0, 20, 30); //left,top,right,bottom
        list.setLayoutParams(params);
    }

}
