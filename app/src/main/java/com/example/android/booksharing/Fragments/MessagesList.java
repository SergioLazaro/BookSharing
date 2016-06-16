package com.example.android.booksharing.Fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.android.booksharing.AsyncTask.MessagesListAsyncTask;
import com.example.android.booksharing.Objects.Message;
import com.example.android.booksharing.Objects.UnreadMessage;
import com.example.android.booksharing.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sergio on 6/6/16.
 */
public class MessagesList extends Fragment {

    private String username;
    private ListView list;
    private ArrayList<Message> messageArray = new ArrayList<Message>();
    private ArrayList<Message> displayedMessages = new ArrayList<Message>();
    private ArrayList<UnreadMessage> unreadMessages = new ArrayList<UnreadMessage>();
    private View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Getting arguments
        if (getArguments() != null) {
            username = getArguments().getString("username");
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.message_list_fragment, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;

        //Setting up ListView
        list = (ListView) view.findViewById(R.id.messageList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView adapterView, View view, int position, long id) {
                Message message = displayedMessages.get(position);
                String receiver = "";
                if (message.getSender().equals(username)) {
                    receiver = message.getReceiver();
                } else {
                    receiver = message.getSender();
                }
                //Prepare new Fragment
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment fragment = new Messages();
                //Setting arguments
                Bundle arguments = new Bundle();
                arguments.putString("sender", username);
                arguments.putString("receiver", receiver);
                arguments.putString("title", message.getTitle());
                arguments.putInt("publicationID", message.getPublicationID());

                fragment.setArguments(arguments);
                //Starting fragment transaction
                ft.replace(R.id.fragment_container, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        //Getting all last messages info
        MessagesListAsyncTask messagesListAsyncTask = new MessagesListAsyncTask(view.getContext(),
                new messageListCallBack(){
                    public void onTaskDone(String jsonArray){
                        loadMessageList(jsonArray);
                    }
                });
        messagesListAsyncTask.execute(username);
    }

    public interface messageListCallBack {
        public void onTaskDone(String jsonArray);
    }


    private void loadMessageList(String json){
        try {
            JSONArray jsonArray = new JSONArray(json);
            messageArray = new ArrayList<Message>();
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject elem = jsonArray.getJSONObject(i);
                Message m = generateMessageObject(elem);
                messageArray.add(m);
            }
            setInfoToShow();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getUnreadMessages(){
        for(Message m : messageArray){
            int i = 0;
            boolean found = false;
            while(!found && i < unreadMessages.size()){
                UnreadMessage unreadMessage = unreadMessages.get(i);
                if(unreadMessage.getMessageID() == m.getId()){
                    found = true;
                    if(!unreadMessage.isUnread() && m.getRead() == 0 && !username.equals(m.getSender())){  //Found unread message
                        unreadMessage.setUnread(true);
                    }
                }
                i ++;
            }
            if(!found){
                boolean read = false;
                if(m.getRead() == 0){   //not read
                    read = true;
                }
                unreadMessages.add(new UnreadMessage(m.getId(),read));
            }
        }
    }

    private void reduceConversationMessages(){
        for(Message m : messageArray){
            int i = 0;
            boolean found = false;
            while(i < displayedMessages.size() && !found){
                if(displayedMessages.get(i).getId() == m.getId()){
                    found = true;
                }
                i ++;
            }
            if(!found){
                displayedMessages.add(m);
            }
        }
    }

    private boolean getMessageState(int id){
        boolean unread = false;
        for(UnreadMessage m : unreadMessages){
            if(m.getMessageID() == id){
                unread = m.isUnread();
            }
        }
        return unread;
    }

    private void setInfoToShow() {
        getUnreadMessages();
        reduceConversationMessages();
        ArrayList<HashMap<String,String>> infoToShow = generateInfo();
        SimpleAdapter arrayAdapter = new SimpleAdapter(view.getContext(), infoToShow, android.R.layout.two_line_list_item,
                new String[]{"User", "Unread"},
                new int[]{android.R.id.text1, android.R.id.text2});
        list.setAdapter(arrayAdapter);
    }

    private ArrayList<HashMap<String,String>> generateInfo(){
        ArrayList<HashMap<String,String>> infoToShow = new ArrayList<HashMap<String,String>>();
        //displayedMessages.clear();
        for(int i = 0; i < displayedMessages.size(); i++){
            Message m = displayedMessages.get(i);
            HashMap<String, String> din = new HashMap<String, String>(2);
            String usertext = "";
            if(m.getSender().equals(username)){
                usertext = "Conversation with: " + m.getReceiver() + " Book: " + m.getTitle();
            }
            else{
                usertext = "Conversation with: " + m.getSender() + " Book: " + m.getTitle();
            }
            din.put("User",usertext);
            if(getMessageState(m.getId())){
                din.put("Unread","Unread messages available");
            }
            else{
                din.put("Unread", "All messages read");
            }
            infoToShow.add(din);
        }

        return infoToShow;
    }

    private Message generateMessageObject(JSONObject elem){
        try {
            return new Message(elem.getInt("id"),elem.getString("sender"),
                    elem.getString("receiver"), elem.getInt("read"),elem.getInt("publicationID"),
                    elem.getString("title"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
