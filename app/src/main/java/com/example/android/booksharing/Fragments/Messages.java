package com.example.android.booksharing.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.booksharing.AsyncTask.LoadMessagesAsyncTask;
import com.example.android.booksharing.AsyncTask.MessagesListAsyncTask;
import com.example.android.booksharing.AsyncTask.SendMessageAsyncTask;
import com.example.android.booksharing.Objects.Message;
import com.example.android.booksharing.Objects.Publication;
import com.example.android.booksharing.Objects.SQLInjection;
import com.example.android.booksharing.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;

/**
 * Created by Sergio on 6/6/16.
 */
public class Messages extends Fragment {

    private String sender, receiver, title;
    private int publicationID;
    private static ListView list;
    private static ArrayList<Message> array = new ArrayList<Message>();
    private static View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Getting arguments
        if(getArguments() != null){
            sender = getArguments().getString("sender");
            receiver = getArguments().getString("receiver");
            title = getArguments().getString("title");
            publicationID = getArguments().getInt("publicationID");
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.messages_fragment, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;

        TextView users = (TextView) view.findViewById(R.id.usersTextView);
        users.setText("Messages between " + sender + " and " + receiver);

        TextView titleView = (TextView) view.findViewById(R.id.titleTextView);
        titleView.setText("Book title: " + title);

        list = (ListView) view.findViewById(R.id.messagesLoad);

        //Message to send
        final EditText messageLabel = (EditText) view.findViewById(R.id.message);

        Button sendMessage = (Button) view.findViewById(R.id.messageButton);
        sendMessage.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                SQLInjection sql = new SQLInjection();
                String message = messageLabel.getText().toString();
                if (!message.equals("")) {
                    if (sql.checkSQLInjection(message)) { //EVERYTHING OK
                        String pID = publicationID + "";
                        new SendMessageAsyncTask(v.getContext()).execute(sender, receiver, message,
                                pID);
                        messageLabel.setText("");
                    } else {   //SQL INJECTION
                        Toast.makeText(v.getContext(), "Invalid message",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {   //EMPTY FIELD
                    Toast.makeText(v.getContext(), "You must type something",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        new LoadMessagesAsyncTask(view.getContext()).execute(sender,receiver,String.valueOf(publicationID));
    }

    public static void setAdapter(ArrayList<HashMap<String,String>> infoToShow, ArrayList<Message> newArray){
        array = newArray;
        SimpleAdapter arrayAdapter = new SimpleAdapter(view.getContext(), infoToShow,
                android.R.layout.two_line_list_item , new String[] { "Message","Info" },
                new int[] {android.R.id.text1, android.R.id.text2});
        list.setAdapter(arrayAdapter);
    }
}
