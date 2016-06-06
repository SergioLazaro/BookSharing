package com.example.android.booksharing.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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

/**
 * Created by Sergio on 6/6/16.
 */
public class Messages extends Fragment {

    private String sender, receiver;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Getting arguments
        if(getArguments() != null){
            sender = getArguments().getString("sender");
            receiver = getArguments().getString("receiver");
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.messages_fragment, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView users = (TextView) view.findViewById(R.id.usersTextView);
        users.setText("Messages between " + sender + " and " + receiver);

        //SCROLLVIEW HERE

        //Message to send
        EditText messageLabel = (EditText) view.findViewById(R.id.message);

        Button sendMessage = (Button) view.findViewById(R.id.messageButton);
        sendMessage.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"#TODO SEND MESSAGE",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
