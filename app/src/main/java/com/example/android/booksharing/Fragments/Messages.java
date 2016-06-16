package com.example.android.booksharing.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

import org.json.JSONArray;
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
    private ListView list;
    private ArrayList<Message> array = new ArrayList<Message>();
    private View view;

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
                        SendMessageAsyncTask messageAsyncTask = new SendMessageAsyncTask(v.getContext(),new sendMessageCallBack(){
                            @Override
                            public void onTaskDone() {
                                launchLoadMessagesAsyncTask();
                            }
                        });
                        messageAsyncTask.execute(sender, receiver, message, pID);
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

        launchLoadMessagesAsyncTask();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

    }

    public interface messageCallBack {
        public void onTaskDone(String json);
    }

    public interface sendMessageCallBack{
        public void onTaskDone();
    }

    private void launchLoadMessagesAsyncTask(){
        LoadMessagesAsyncTask asyncTask = new LoadMessagesAsyncTask(view.getContext(),new messageCallBack() {
            @Override
            public void onTaskDone(String json) {
                populateArrays(json);
            }

        });
        asyncTask.execute(sender,receiver,String.valueOf(publicationID));
    }

    private void setAdapter(ArrayList<HashMap<String,String>> infoToShow, ArrayList<Message> newArray){
        SimpleAdapter arrayAdapter = new SimpleAdapter(view.getContext(), infoToShow,
                android.R.layout.two_line_list_item , new String[] { "Message","Info" },
                new int[] {android.R.id.text1, android.R.id.text2});
        list.setAdapter(arrayAdapter);
    }

    private void populateArrays(String json){
        try {
            JSONArray jsonArray = new JSONArray(json);
            array = new ArrayList<Message>();
            ArrayList<HashMap<String, String>> infoToShow = new ArrayList<HashMap<String, String>>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject elem = jsonArray.getJSONObject(i);
                Message m = generateMessage(elem);
                array.add(m);
                //Populating info to show
                HashMap<String, String> din = new HashMap<String, String>(2);
                if (m.getSender().equals(sender)) {
                    din.put("Message", "You: " + m.getText());
                } else {
                    din.put("Message", m.getSender() + ": " + m.getText());
                }
                din.put("Info", m.getDate());
                infoToShow.add(din);
            }
            setAdapter(infoToShow,array);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Message generateMessage(JSONObject elem){
        try {
            return new Message(elem.getInt("id"),elem.getString("sender"),
                    elem.getString("receiver"),elem.getString("message"),elem.getString("date"),
                    elem.getInt("read"), elem.getInt("publicationID"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
