package com.example.android.booksharing.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.booksharing.R;

/**
 * Created by Sergio on 1/6/16.
 */
public class Signup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        EditText email = (EditText) findViewById(R.id.email);
        EditText password = (EditText) findViewById(R.id.password);
        EditText repassword = (EditText) findViewById(R.id.repassword);

    }

    public void signupRequest(View view){
        //Create hash for user password

        //Start AsyncTask

        //Check results from AsyncTask

        //Launch item if resuts OK
        Intent intent = new Intent(view.getContext(),MainActivity.class);
        startActivity(intent);
    }
}
