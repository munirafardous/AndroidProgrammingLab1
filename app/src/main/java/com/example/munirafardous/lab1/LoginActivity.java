package com.example.munirafardous.lab1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    final String ACTIVITY_NAME = "StartActivity";
    //Button login_button = (Button) findViewById(R.id.button2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Log.i(ACTIVITY_NAME, "In onCreate()");
            }
        });

    }



    protected void onResume()
    {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }
    protected void onStart()
    {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
        // final SharedPreferences prefs = getSharedPreferences("storeddata", Context.MODE_PRIVATE);
        // prefs.getString("DefaultEmail","aradhita.mohanty@gmail.com");
        //SharedPreferences.Editor writer = prefs.edit();
        Button login_button = (Button) findViewById(R.id.button2);
        login_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                EditText username = (EditText) findViewById(R.id.TE);
                String key = getResources().getString(R.string.login_key);
                String value = username.getText().toString();
                editor.putString(key, value );
                editor.commit();
                Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                startActivity(intent);

            }
        });
        // Read from the shared preferences the value of the email
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        // get the default email from R and pass it in as default string returned by sharedPref.
        String defaultEmail = getResources().getString(R.string.login);
        String email = sharedPref.getString(getResources().getString(R.string.login_key), defaultEmail);

        EditText username = (EditText) findViewById(R.id.TE);
        username.setText(email);


    }


    protected void onPause()
    {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }
    protected void onStop()
    {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }
    protected void onDestroy()
    {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }

}
