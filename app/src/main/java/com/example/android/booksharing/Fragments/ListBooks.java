package com.example.android.booksharing.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.booksharing.AsyncTask.PublishAsyncTask;
import com.example.android.booksharing.AsyncTask.RefreshListAsyncTask;
import com.example.android.booksharing.Objects.Publication;
import com.example.android.booksharing.R;

import java.util.ArrayList;

/**
 * Created by Sergio on 1/6/16.
 */
public class ListBooks extends Fragment {

    private String selectedOption = "";
    private Spinner spinner;
    private static ListView list;
    private static ArrayList<Publication> array;
    private static SimpleAdapter arrayAdapter;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.list_books_fragment, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinner = (Spinner) view.findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                selectedOption = spinner.getSelectedItem().toString();
                new RefreshListAsyncTask(getContext()).execute(selectedOption);
            }
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        //Setting up ListView
        list = (ListView) view.findViewById(R.id.listView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView adapterView, View view, int position, long id) {
                Publication p = array.get(position);    //Getting clicked element
                //Prepare new Fragment
                android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment fragment = new BookInfo();
                //Setting arguments
                Bundle arguments = new Bundle();
                arguments.putString("publication", p.generateJSONObject());
                fragment.setArguments(arguments);
                //Starting fragment transaction
                ft.replace(R.id.fragment_container, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }

    public static void setAdapter(SimpleAdapter newAdapter, ArrayList<Publication> newArray){
        Log.i("CHANGING ADAPTER","CHANGING ADAPTER");
        array = newArray;
        arrayAdapter = newAdapter;
        list.setAdapter(arrayAdapter);
    }
}
