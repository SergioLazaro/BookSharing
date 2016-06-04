package com.example.android.booksharing.Fragments;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.booksharing.Activities.MainActivity;
import com.example.android.booksharing.AsyncTask.PublishAsyncTask;
import com.example.android.booksharing.Objects.Publication;
import com.example.android.booksharing.Objects.SQLInjection;
import com.example.android.booksharing.R;

/**
 * Created by Sergio on 1/6/16.
 */
public class PublishBook extends Fragment {

    private String username;
    private String selectedOption = "";
    private EditText title, author, description;
    private Spinner spinner;
    private RatingBar rating;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Getting arguments
        if(getArguments() != null){
            username = getArguments().getString("username");
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.publish_book_fragment, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        title = (EditText) view.findViewById(R.id.bookTitle);
        author = (EditText) view.findViewById(R.id.bookAuthor);
        rating = (RatingBar) view.findViewById(R.id.ratingBar);
        LayerDrawable stars = (LayerDrawable) rating.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        description = (EditText) view.findViewById(R.id.bookDescription);

        spinner = (Spinner) view.findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                selectedOption = spinner.getSelectedItem().toString();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        Button confirmPublication = (Button) view.findViewById(R.id.confirmPublication);

        confirmPublication.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                confirm();
            }
        });

    }

    private void confirm(){
        SQLInjection sql = new SQLInjection();
        String title = this.title.getText().toString();
        String author = this.author.getText().toString();
        String description = this.description.getText().toString();
        if(sql.checkSQLInjection(title) && sql.checkSQLInjection(author) &&
                sql.checkSQLInjection(description)){
            Log.e("USERNAME: ", username);
            Publication p = new Publication(username,title,author,selectedOption,
                    rating.getRating(),description);
            new PublishAsyncTask(getContext()).execute(p.generateJSONObject());
        }
        else{
            Toast.makeText(getContext(),"Invalid fields detected",Toast.LENGTH_SHORT).show();
        }
    }

}
