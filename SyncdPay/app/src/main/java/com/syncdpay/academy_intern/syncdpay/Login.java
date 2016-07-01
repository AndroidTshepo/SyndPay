package com.syncdpay.academy_intern.syncdpay;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import java.util.List;

public class Login extends AppCompatActivity
{
    protected Button login;
    protected EditText edtUsername,edtPassword;
    protected TextView register,tvForgotPassword,add_no;
    private String getSimNumber, username;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private Context context = this;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);

//        declare variables and locate elements on the login layout
        login = (Button)findViewById(R.id.btn_login);
        edtUsername = (EditText)findViewById(R.id.et_username);
        edtPassword = (EditText)findViewById(R.id.edtPassword);
        register = (TextView)findViewById(R.id.tv_register);
        tvForgotPassword = (TextView)findViewById(R.id.tvForgotPassword);
        add_no = (TextView)findViewById(R.id.tv_add_no);

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),GetOTP.class);
                startActivity(intent);

            }
        });

//        navigate to the add new number activity
        add_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),AddNumber.class);
                startActivity(intent);
            }
        });

        //navigate to the registration activity
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),Register.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();

                //we use android's telephone manager to get the sim number for Line1
                TelephonyManager telemamanger = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String getSimSerialNumber = telemamanger.getSimSerialNumber();
                getSimNumber = telemamanger.getLine1Number();

                username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();

                System.out.println("Sim no " + getSimNumber);
                System.out.println("Login no " + username);

                ParseUser.logInInBackground(username, password, new LogInCallback() {
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            // Hooray! The user is logged in.

                            ParseQuery<ParseUser> userQuery =ParseUser.getQuery();
                            userQuery.whereEqualTo("username", username);
                            userQuery.findInBackground(new FindCallback<ParseUser>() {
                                @Override
                                public void done(List<ParseUser> list, ParseException e) {

                                    if(e==null)
                                    {
                                        progressDialog.dismiss();
                                        if(list.size()>0){


                                            for (ParseUser user : list) {

                                                String id = user.getString("ID_Number");
                                                String userName = user.getString("username");
                                                int availableBalance = user.getInt("AvailableBalance");
                                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                                                SharedPreferences.Editor editor = preferences.edit();
                                                editor.putString("IDNumber",id);
                                                editor.putString("username",userName);
                                                editor.putInt("AvailableBalance",availableBalance);
                                                editor.apply();

                                                Intent intent = new Intent(getBaseContext(),Dashboard.class);
                                                startActivity(intent);
                                            }
                                        }
                                    }
                                }
                            });

                        } else {
                            // Signup failed. Look at the ParseException to see what happened.
                            progressDialog.dismiss();
                            AlertDialog.Builder alert = new AlertDialog.Builder(Login.this);
                            alert.setTitle("Info");
                            alert.setMessage("Login failed "+e.getMessage());
                            alert.setPositiveButton("OK", null);
                            alert.show();
                        }
                    }
                });
            }
        });
    }
}