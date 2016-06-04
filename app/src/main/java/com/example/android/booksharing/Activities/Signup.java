package com.example.android.booksharing.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.booksharing.AsyncTask.LoginAsyncTask;
import com.example.android.booksharing.AsyncTask.SignupAsyncTask;
import com.example.android.booksharing.Objects.SQLInjection;
import com.example.android.booksharing.Objects.User;
import com.example.android.booksharing.R;

/**
 * Created by Sergio on 1/6/16.
 */
public class Signup extends AppCompatActivity {

    private EditText email, password, repassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.repassword);

    }

    public void signupRequest(View view){
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();
        String repassword = this.repassword.getText().toString();
        SQLInjection sql = new SQLInjection();
        if(!email.equals("") && !password.equals("")){
            if(sql.checkSQLInjection(email) && sql.checkSQLInjection(password) &&
                    sql.checkSQLInjection(repassword) && password.equals(repassword)){    //Everything OK
                User user = new User(email,password);
                //Start AsyncTask
                new SignupAsyncTask(view.getContext()).execute(user.getUsername(),user.getHash());

            }
            else{   //SQLInjection attempt
                Toast.makeText(view.getContext(),"Invalid username or password",Toast.LENGTH_SHORT).show();
            }
        }
        else{   //Nothing inserted, not allowed
            Toast.makeText(view.getContext(),"Invalid username or password",Toast.LENGTH_SHORT).show();
        }
    }
}
