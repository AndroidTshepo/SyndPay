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
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by TheDigitalAcademy on 16/01/15.
 */
public class Profile extends AppCompatActivity {

    private TextView tvName,tvSurname,tvIDnumber,tvEmail,tvContact,tvAccount,tvCardNo,tvBranchCode,tvBalance;
    private Context context = this;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String username = preferences.getString("username", "");

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);

        tvName = (TextView)findViewById(R.id.tvName);
        tvSurname = (TextView)findViewById(R.id.tvSurname);
        tvIDnumber = (TextView)findViewById(R.id.tvIDnumber);
        tvEmail = (TextView)findViewById(R.id.tvEmail);
        tvContact = (TextView)findViewById(R.id.tvContact);
        tvAccount = (TextView)findViewById(R.id.tvAccount);
        tvCardNo = (TextView)findViewById(R.id.tvCardNo);
        tvBranchCode = (TextView)findViewById(R.id.tvBranchCode);
        tvBalance = (TextView)findViewById(R.id.tvBalance);

        ParseQuery<ParseUser> userQuery =ParseUser.getQuery();
        userQuery.whereEqualTo("username", username);
        userQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {

                progressDialog.show();

                if (e == null) {
                    if (list.size() > 0) {

                        progressDialog.dismiss();
                        for (ParseUser user : list) {

                            String name = user.getString("Name");
                            String surname = user.getString("Surname");
                            String idNumber = user.getString("ID_Number");
                            String email = user.getString("email");
                            String contact = user.getString("username");
                            String accountNo = user.getString("accountNo");
                            String cardNo = user.getString("cardNo");
                            String branchCode = user.getString("branchCode");
                            int availableBalance = user.getInt("AvailableBalance");

                            tvName.setText(name);
                            tvSurname.setText(surname);
                            tvIDnumber.setText(idNumber);
                            tvEmail.setText(email);
                            tvContact.setText(contact);
                            tvAccount.setText(accountNo);
                            tvCardNo.setText(cardNo);
                            tvBranchCode.setText(branchCode);
                            tvBalance.setText(""+availableBalance);

                        }
                    }
                }
                else{

                    progressDialog.dismiss();
                    AlertDialog.Builder alert = new AlertDialog.Builder(Profile.this);
                    alert.setTitle("Info");
                    alert.setMessage("Failed "+e.getMessage());
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