package com.syncdpay.academy_intern.syncdpay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.Parse;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parse.initialize(this, "ZYbizSgM91NBv2QWoWcZ2UpWLTqAhdefpUDAqbkN", "SHr8gPtKFnOQjz2OvpZSg1YQJU6AxLNRgDTdhnJ8");
        setContentView(R.layout.activity_splash);

        //Setting up the splash screen and giving it a timer to sleep and an intended activity
        Thread startTimer = new Thread(){
            public void run(){
                try {
                    sleep(3000);
                    //After setting up the sleep interval intialise the intern to i
                    Intent i = new Intent(Splash.this, Login.class);
                    startActivity(i);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        startTimer.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
