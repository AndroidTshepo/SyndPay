package com.syncdpay.academy_intern.syncdpay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class Register extends AppCompatActivity
{
    protected EditText username,name,surname,id,email,alt_no,edtPassword;
    protected Button next;
    protected TextView cancel_reg;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //Enabling parse on the register page
//        Parse.enableLocalDatastore(this);
//        Parse.initialize(this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        //declare variables and locate elements on the registration layout
        username = (EditText)findViewById(R.id.et_username_reg);
        name = (EditText)findViewById(R.id.et_name_reg);
        surname = (EditText)findViewById(R.id.et_surname_reg);
        id = (EditText)findViewById(R.id.et_id_reg);
        email = (EditText)findViewById(R.id.et_email_reg);
        alt_no = (EditText)findViewById(R.id.et_mobile_number_alt_reg);
        next = (Button)findViewById(R.id.btn_next_reg);
        cancel_reg = (TextView)findViewById(R.id.tv_cancel_reg);
        edtPassword = (EditText)findViewById(R.id.edtPassword);

        cancel_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),Login.class);
                startActivity(intent);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                //Get the user details from textboxes and then convert them into string
                String Username = username.getText().toString().trim();
                String Name = name.getText().toString().trim();
                String Surname = surname.getText().toString().trim();
                String Id = id.getText().toString().trim();
                String Email = email.getText().toString().trim();
                String Alternative_Number = alt_no.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                Intent intent = new Intent(getBaseContext(), Register2.class);


                //Using Bundle to pass data from this Activity to Register2 Activity
                Bundle bundle = new Bundle();
                bundle.putString("username",Username);
                bundle.putString("password",password);
                bundle.putString("Name",Name);
                bundle.putString("Surname",Surname);
                bundle.putString("Id",Id);
                bundle.putString("Email",Email);
                bundle.putString("Alternative_Number",Alternative_Number);
                intent.putExtras(bundle);
                startActivity(intent);




                //New Code

//                ParseObject post = new ParseObject("Post");
//                post.put("Name", Name);
//                post.put("Surname", Surname);
//                post.put("ID_Number", Id);
//                post.put("Primary_Number", Primary_Number);
//                post.put("Alternative_Number", Alternative_Number);
//                setProgressBarIndeterminateVisibility(true);
//
//                post.saveInBackground(new SaveCallback() {
//                    @Override
//                    public void done(ParseException e) {
//                        setProgressBarIndeterminateVisibility(false);
//                        if(e==null)
//                        {
//                            //
//                            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
//
//                        }
//                        else {
//                            // The save failed.
//                            Toast.makeText(getApplicationContext(), "Failed to Save", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });




//                //Here we store users in parse
//                ParseUser user = new ParseUser();
//                //The Parse Defined data typea
//                user.setUsername(Username);
//                user.setEmail(Email);
////                My own defined data types
//                user.put("Name", Name);
//                user.put("Surname", Surname);
//                user.put("ID_Number", Id);
//                user.put("Primary_Number", Primary_Number);
//                user.put("Alternative_Number", Alternative_Number);
//
//                //Saving with a callback
//                user.signUpInBackground(new SignUpCallback() {
//                    @Override
//                    public void done(ParseException e) {
//                        if (e == null) {
//                            //user singed up successfully
//                            Toast.makeText(Register.this, "Successfully Signed Up", Toast.LENGTH_LONG).show();
////                            Intent intent = new Intent(getBaseContext(), Register2.class);
////                            startActivity(intent);
//
//                            Log.d("MyApp", "Anonymous login failed.");
//
//                        } else {
//                            //There was an error signing up the user
//                            Toast.makeText(Register.this, "Error Signing Up", Toast.LENGTH_LONG).show();
//                        }
//
//
//                    }
//                });







            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
