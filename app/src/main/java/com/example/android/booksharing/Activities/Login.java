package com.example.android.booksharing.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.booksharing.AsyncTask.LoginAsyncTask;
import com.example.android.booksharing.Fragments.ListBooks;
import com.example.android.booksharing.Objects.SQLInjection;
import com.example.android.booksharing.Objects.User;
import com.example.android.booksharing.R;

/**
 * Created by Sergio on 1/6/16.
 */
public class Login extends AppCompatActivity {

    private EditText email, password;
    public static boolean startFragment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        Button loginButton = (Button) findViewById(R.id.loginButton);
    }

    public void loginRequest(View view){
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();
        SQLInjection sql = new SQLInjection();
        if(!email.equals("") && !password.equals("")){
            if(sql.checkSQLInjection(email) && sql.checkSQLInjection(password)){    //Everything OK
                User user = new User(email,password);
                //Start AsyncTask
                new LoginAsyncTask(view.getContext()).execute(user.generateJSONObject());

            }
            else{   //SQLInjection attempt
                Toast.makeText(view.getContext(),"Invalid username or password",Toast.LENGTH_SHORT).show();
            }
        }
        else{   //Nothing inserted, not allowed
            Toast.makeText(view.getContext(),"Invalid username or password",Toast.LENGTH_SHORT).show();
        }
    }

    public void signupRequest(View view){
        Intent intent = new Intent(view.getContext(),Signup.class);
        startActivity(intent);
    }


}
