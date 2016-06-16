package com.example.android.booksharing.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.booksharing.AsyncTask.CheckNotificationsAsyncTask;
import com.example.android.booksharing.AsyncTask.SearchAsyncTask;
import com.example.android.booksharing.Fragments.BookInfo;
import com.example.android.booksharing.Fragments.ListBooks;
import com.example.android.booksharing.Fragments.Messages;
import com.example.android.booksharing.Fragments.MessagesList;
import com.example.android.booksharing.Fragments.PublishBook;
import com.example.android.booksharing.Objects.Publication;
import com.example.android.booksharing.Objects.SQLInjection;
import com.example.android.booksharing.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int INTERNET_REQUEST = 0;
    private String username;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        if( intent != null ){
            username = intent.getStringExtra("username");
        }

        //Check INTERNET permission
        checkInternetPermission();

        CheckNotificationsAsyncTask notificationsAsyncTask =
                new CheckNotificationsAsyncTask(getApplicationContext(),new CheckNotifications(){

                    @Override
                    public void onTaskDone(boolean notifications) {
                        changeNotificationIconColor(notifications);
                    }
                });
        notificationsAsyncTask.execute(username);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment fragment = new ListBooks();
        //Setting arguments
        Bundle arguments = new Bundle();
        arguments.putString("username", username);
        fragment.setArguments(arguments);
        ft.replace(R.id.fragment_container, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void checkInternetPermission(){
        if ((int) Build.VERSION.SDK_INT < 23){
            Toast.makeText(this,"Build version: " + Build.VERSION.SDK_INT,Toast.LENGTH_SHORT);
        }
        else{
            boolean go = _checkPermission();
            if(!go){
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET},
                        INTERNET_REQUEST);
                Log.i("SEND REQUEST","SEND REQUEST");
            }
        }
    }

    private boolean _checkPermission(){
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET);
        Log.i("PERMISSION RETURN: ", "" + permissionCheck);

        return false;
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.i("INTERNET PERMISSION", "CODE: " + requestCode);
        switch (requestCode) {
            case INTERNET_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //normal workflow
                    Toast.makeText(this,"NORMAL WORKFLOW",Toast.LENGTH_SHORT);
                } else {
                    Toast.makeText(getApplicationContext(),"WE NEED THE PERMISSION", Toast.LENGTH_SHORT);

                }
                return;
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem notificationItem = menu.findItem(R.id.notifications);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("Search user books, titles...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query) {
                SQLInjection sql = new SQLInjection();
                if(sql.checkSQLInjection(query)){
                    SearchAsyncTask asynctask = new SearchAsyncTask(getApplicationContext(),new SearchCallBack() {
                        public void onTaskDone(String type, String json) {
                            callBackMethod(json,type);
                        }
                    });

                    asynctask.execute(query,username);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Search not allowed",Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        return true;
    }

    private void changeNotificationIconColor(boolean notifications){
        Drawable drawable = toolbar.getMenu().findItem(R.id.notifications).getIcon();
        if (drawable != null) {
            drawable.mutate();
            if(notifications){
                drawable.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            }
            else{
                drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            }

        }
    }

    private void launchMessagesListFragment(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment fragment = new MessagesList();
        //Setting arguments
        Bundle arguments = new Bundle();
        arguments.putString("username", username);
        fragment.setArguments(arguments);
        ft.replace(R.id.fragment_container, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void callBackMethod(String json, String type){
        if(type.equals("book")){
            JSONArray jsonArray;
            JSONObject jsonObject;
            try {
                jsonArray = new JSONArray(json);
                FragmentTransaction ft =  getFragmentManager().beginTransaction();
                Fragment fragment;
                //Setting arguments
                Bundle arguments = new Bundle();
                arguments.putString("username",username);
                if(jsonArray.length() > 1){ //Several books with the same name
                    fragment = new ListBooks();
                    arguments.putString("json",jsonArray.toString());
                    arguments.putString("type",type);
                }
                else{       //Single book
                    fragment = new BookInfo();
                    jsonObject = jsonArray.getJSONObject(0);
                    arguments.putString("publication",jsonObject.toString());
                }
                fragment.setArguments(arguments);
                //Starting fragment transaction
                ft.replace(R.id.fragment_container, fragment);
                ft.addToBackStack(null);
                ft.commit();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        else{
            FragmentTransaction ft =  getFragmentManager().beginTransaction();
            Fragment fragment = new ListBooks();
            //Setting arguments
            Bundle arguments = new Bundle();
            arguments.putString("username",username);
            arguments.putString("json", json);
            arguments.putString("type",type);
            fragment.setArguments(arguments);
            //Starting fragment transaction
            ft.replace(R.id.fragment_container, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    public interface SearchCallBack {
        public void onTaskDone(String type, String jsonArray);
    }

    public interface CheckNotifications{
        public void onTaskDone(boolean notifications);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_search){
            return true;
        }
        else if(id == R.id.notifications){
            changeNotificationIconColor(false);
            launchMessagesListFragment();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_list) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment fragment = new ListBooks();
            //Setting arguments
            Bundle arguments = new Bundle();
            arguments.putString("username", username);
            fragment.setArguments(arguments);
            ft.replace(R.id.fragment_container, fragment);
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.nav_publish) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment fragment = new PublishBook();
            //Setting arguments
            Bundle arguments = new Bundle();
            arguments.putString("username", username);
            fragment.setArguments(arguments);
            ft.replace(R.id.fragment_container, fragment);
            ft.addToBackStack(null);
            ft.commit();

        } else if(id == R.id.nav_books){
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment fragment = new ListBooks();
            //Setting arguments
            Bundle arguments = new Bundle();
            arguments.putString("username", username);
            arguments.putString("type","yourBooks");
            fragment.setArguments(arguments);
            ft.replace(R.id.fragment_container, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
        else if (id == R.id.nav_messages) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment fragment = new MessagesList();
            //Setting arguments
            Bundle arguments = new Bundle();
            arguments.putString("username", username);
            fragment.setArguments(arguments);
            ft.replace(R.id.fragment_container, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
