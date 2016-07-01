package com.syncdpay.academy_intern.syncdpay;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by TheDigitalAcademy on 16/01/14.
 */
public class AddStore extends AppCompatActivity {

    private Button btnAddStore;
    private EditText edtStoreName, edtAccountNumber;
    private  ParseObject store;
    private String storeName,accountNumber;
    private Context context = this;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_store);

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);

        btnAddStore = (Button)findViewById(R.id.btnAddStore);

        edtStoreName = (EditText)findViewById(R.id.edtStoreName);
        edtAccountNumber = (EditText)findViewById(R.id.edtAccountNumber);


        btnAddStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                String idNumber = preferences.getString("IDNumber","");

                storeName = edtStoreName.getText().toString();
                accountNumber = edtAccountNumber.getText().toString();

                store = new ParseObject("Stores");
                store.put("StoreName", storeName);
                store.put("AccountNumber", accountNumber);
                store.put("IDNumber",idNumber);



                store.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        setProgressBarIndeterminateVisibility(false);
                        if(e==null)
                        {
                            progressDialog.dismiss();


                            ParseUser user = ParseUser.getCurrentUser();
                            ParseRelation relation = user.getRelation("Stores");
                            relation.add(store);
                            user.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e==null)
                                    {
                                        progressDialog.dismiss();

                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setTitle("Info");
                                        builder.setMessage("Store Added");
                                        builder.setCancelable(true);
                                        builder.setPositiveButton(
                                                "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        edtStoreName.setText("");
                                                        edtAccountNumber.setText("");
                                                    }
                                                });
                                        AlertDialog alert = builder.create();
                                        alert.show();

                                    }
                                    else
                                    {
                                        progressDialog.dismiss();
                                        AlertDialog.Builder alert = new AlertDialog.Builder(AddStore.this);
                                        alert.setTitle("Info");
                                        alert.setMessage("Failed to add store " + e.getMessage());
                                        alert.setPositiveButton("OK", null);
                                        alert.show();

                                    }
                                }
                            });

                        }
                        else {
                            // The save failed.
                            progressDialog.dismiss();
                            AlertDialog.Builder alert = new AlertDialog.Builder(AddStore.this);
                            alert.setTitle("Info");
                            alert.setMessage("Failed to add store " + e.getMessage());
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
        getMenuInflater().inflate(R.menu.menu_stores, menu);
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