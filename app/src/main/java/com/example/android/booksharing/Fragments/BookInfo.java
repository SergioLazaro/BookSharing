package com.example.android.booksharing.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.booksharing.Objects.Publication;
import com.example.android.booksharing.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Created by Sergio on 4/6/16.
 */
public class BookInfo extends Fragment {

    private Publication publication;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Getting arguments
        if(getArguments() != null){
            String tmpPublication = getArguments().getString("publication");
            try {
                JSONObject json = new JSONObject(tmpPublication);
                publication = new Publication(json.getString("username"),json.getString("title"),
                        json.getString("author"),json.getString("description"),
                        Float.valueOf(json.getString("rate")),json.getString("type"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.book_info_fragment, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Setting username field
        TextView username = (TextView) view.findViewById(R.id.userView);
        username.setText("Username: " + publication.getUsername());

        //Setting title field
        TextView title = (TextView) view.findViewById(R.id.titleView);
        title.setText("Title: " + publication.getTitle());

        //Setting author field
        TextView author = (TextView) view.findViewById(R.id.authorView);
        author.setText("Author: " + publication.getAuthor());

        //Setting rating field
        TextView rating = (TextView) view.findViewById(R.id.ratingView);
        rating.setText("Rating: " + publication.getRate() + "/5");

        //Setting description field
        TextView description = (TextView) view.findViewById(R.id.descriptionView);
        description.setText(publication.getDescription());

        Button button = (Button) view.findViewById(R.id.chatButton);
        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //Start chat Fragment
                Toast.makeText(v.getContext(),"CHAT #TODO",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
