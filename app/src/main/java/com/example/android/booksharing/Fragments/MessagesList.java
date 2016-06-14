package com.example.android.booksharing.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.booksharing.AsyncTask.LoadUserPublicationsAsyncTask;
import com.example.android.booksharing.AsyncTask.MessagesListAsyncTask;
import com.example.android.booksharing.Objects.Message;
import com.example.android.booksharing.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sergio on 6/6/16.
 */
public class MessagesList extends Fragment {

    private static String username;
    private static ListView list;
    private static Spinner spinner;
    private static ArrayList<Message> messageArray = new ArrayList<Message>();
    private static ArrayList<String> titlesArray = new ArrayList<String>();
    private static ArrayList<Integer> idsArray = new ArrayList<Integer>();
    private static ArrayList<Message> displayedMessages = new ArrayList<Message>();
    private static View view;
    private static int selectedOption = -1;

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
        //Setting up Spinner book
        spinner = (Spinner) view.findViewById(R.id.bookSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                selectedOption = arg2;
                setInfoToShow();
            }
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        //Setting up ListView
        list = (ListView) view.findViewById(R.id.messageList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView adapterView, View view, int position, long id) {
                Message message = displayedMessages.get(position);
                String receiver = "";
                if(message.getSender().equals(username)){
                    receiver = message.getReceiver();
                }
                else{
                    receiver = message.getSender();
                }
                //Prepare new Fragment
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment fragment = new Messages();
                //Setting arguments
                Bundle arguments = new Bundle();
                arguments.putString("sender", username);
                arguments.putString("receiver", receiver);
                arguments.putString("title",titlesArray.get(selectedOption));
                arguments.putInt("publicationID", idsArray.get(selectedOption));
                Log.e("LAUNCH WINDOW",titlesArray.get(selectedOption) + " - " + idsArray.get(selectedOption));

                fragment.setArguments(arguments);
                //Starting fragment transaction
                ft.replace(R.id.fragment_container, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        //Populating Spinner
        new LoadUserPublicationsAsyncTask(view.getContext()).execute(username);
        //Getting all last messages info
        new MessagesListAsyncTask(view.getContext()).execute(username);
    }

    public static void setInfoToShow() {
        ArrayList<HashMap<String,String>> infoToShow = generateInfo();
        SimpleAdapter arrayAdapter = new SimpleAdapter(view.getContext(), infoToShow, android.R.layout.two_line_list_item,
                new String[]{"User", "CommentAndDate"},
                new int[]{android.R.id.text1, android.R.id.text2});
        list.setAdapter(arrayAdapter);
    }

    private static ArrayList<HashMap<String,String>> generateInfo(){
        ArrayList<HashMap<String,String>> infoToShow = new ArrayList<HashMap<String,String>>();
        int count = 0;
        int unread = 0;
        for(Message m : messageArray){
            if(m.getPublicationID() == idsArray.get(selectedOption)){
                displayedMessages.clear();
                displayedMessages.add(m);
                count++;
                //Populating info to show
                HashMap<String, String> din = new HashMap<String, String>(2);
                String usertext = "";
                if(m.getSender().equals(username)){
                    usertext = "Conversation with: " + m.getReceiver();
                }
                else{
                    usertext = "Conversation with: " + m.getSender();
                }
                if(m.getRead() == 0 && m.getReceiver().equals(username)){
                    usertext += " has unread messages";
                    unread ++;
                }

                din.put("User",usertext);
                din.put("CommentAndDate",m.getText() + " - " + m.getDate());
                infoToShow.add(din);
            }
        }
        if(count > 0){
            Toast.makeText(view.getContext(),unread + " messages unread",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(view.getContext(),"No messages found",Toast.LENGTH_SHORT).show();
        }
        return infoToShow;
    }

    public static void setMessageArray(ArrayList<Message> _messageArray){
        Log.e("ASDF","LENGTH: " + _messageArray.size());
        messageArray = _messageArray;
        setInfoToShow();
    }

    public static void setSpinner(ArrayList<String> _titlesArray,ArrayList<Integer> _idsArray){
        idsArray = _idsArray;
        titlesArray = _titlesArray;
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_item, _titlesArray);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }
}
