package com.syncdpay.academy_intern.syncdpay;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by TheDigitalAcademy on 16/01/15.
 */
public class PayStore extends AppCompatActivity {


    private Context context = this;
    private EditText edtAmount;
    private Button btnPayStore;
    String amount,username;
    int AvailableBalance,OutstandingBalance,balance;
    private ProgressDialog progressDialog;

    private Spinner spinnerStores;
    private ArrayAdapter<String> storesAdapter;
    private ArrayList<String> listStores;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_store);

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);

        listStores = new ArrayList<String>();

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String idNumber = preferences.getString("IDNumber", "");
        username = preferences.getString("username","");

        edtAmount = (EditText)findViewById(R.id.edtAmount);
        btnPayStore = (Button)findViewById(R.id.btnPayStore);
        spinnerStores = (Spinner) findViewById(R.id.spStores);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Stores");
        query.whereEqualTo("IDNumber", idNumber);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {

                    for (ParseObject parseObject : scoreList) {

                        String stores = parseObject.getString("StoreName");
                        listStores.add(stores);


                        amount = edtAmount.getText().toString();
                        AvailableBalance = preferences.getInt("AvailableBalance", 0);
                        OutstandingBalance = parseObject.getInt("OutstandingBalance");


                        String[] store = new String[]{"", stores};



                        btnPayStore.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                progressDialog.show();
                                int tempBalance = AvailableBalance - OutstandingBalance;

                                ParseUser parseUser = ParseUser.getCurrentUser();
                                parseUser.put("AvailableBalance", 100);
                                parseUser.saveInBackground(new SaveCallback() {

                                    @Override
                                    public void done(ParseException e) {
                                        // TODO Auto-generated method stub
                                        if (e != null) {

                                            progressDialog.dismiss();
                                            AlertDialog.Builder alert = new AlertDialog.Builder(PayStore.this);
                                            alert.setTitle("Info");
                                            alert.setMessage("Payment failed " + e.getMessage());
                                            alert.setPositiveButton("OK", null);
                                            alert.show();

                                        } else {
                                            //updated successfully
                                            progressDialog.dismiss();

                                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                            builder.setTitle("Info");
                                            builder.setMessage("Payment successful");
                                            builder.setCancelable(true);
                                            builder.setPositiveButton(
                                                    "OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            edtAmount.setText("");
                                                        }
                                                    });
                                            AlertDialog alert = builder.create();
                                            alert.show();
                                        }
                                    }
                                });


                                spinnerStores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                                        String item = adapterView.getItemAtPosition(position).toString();
                                        //Toast.makeText(context,item,Toast.LENGTH_LONG).show();


                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });


                            }
                        });






                    }

                    storesAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, listStores);
                    spinnerStores.setAdapter(storesAdapter);



                } else {

                    progressDialog.dismiss();
                    AlertDialog.Builder alert = new AlertDialog.Builder(PayStore.this);
                    alert.setTitle("Info");
                    alert.setMessage("Failed " + e.getMessage());
                    alert.setPositiveButton("OK", null);
                    alert.show();

                }
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