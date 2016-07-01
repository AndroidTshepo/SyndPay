package com.syncdpay.academy_intern.syncdpay;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class AddNumber extends AppCompatActivity {

    private EditText edtNewNumber;
    private Button btnDone;
    private TextView tvBack;
    private Context context = this;
    private  ParseObject numbers;
    private String number;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_number);

        edtNewNumber = (EditText)findViewById(R.id.edtNewNumber);
        btnDone = (Button)findViewById(R.id.btnDone);
        tvBack = (TextView)findViewById(R.id.tvBack);

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);


        tvBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),Login.class);
                startActivity(intent);
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                progressDialog.show();

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                String idNumber = preferences.getString("IDNumber","");

                number = edtNewNumber.getText().toString();

                numbers = new ParseObject("Numbers");
                numbers.put("cellNumber", number);
                numbers.put("IDNumber",idNumber);



                numbers.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        setProgressBarIndeterminateVisibility(false);
                        if (e == null) {
                            progressDialog.dismiss();


                            ParseUser user = ParseUser.getCurrentUser();
                            ParseRelation relation = user.getRelation("Numbers");
                            relation.add(numbers);
                            user.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        progressDialog.dismiss();

                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setTitle("Info");
                                        builder.setMessage("Number Added");
                                        builder.setCancelable(true);
                                        builder.setPositiveButton(
                                                "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        edtNewNumber.setText("");
                                                    }
                                                });
                                        AlertDialog alert = builder.create();
                                        alert.show();

                                    } else {
                                        progressDialog.dismiss();
                                        AlertDialog.Builder alert = new AlertDialog.Builder(AddNumber.this);
                                        alert.setTitle("Info");
                                        alert.setMessage("Failed to add number " + e.getMessage());
                                        alert.setPositiveButton("OK", null);
                                        alert.show();

                                    }
                                }
                            });

                        } else {
                            // The save failed.
                            progressDialog.dismiss();
                            AlertDialog.Builder alert = new AlertDialog.Builder(AddNumber.this);
                            alert.setTitle("Info");
                            alert.setMessage("Failed to add number " + e.getMessage());
                            alert.setPositiveButton("OK", null);
                            alert.show();
                        }
                    }
                });

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_number, menu);
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
