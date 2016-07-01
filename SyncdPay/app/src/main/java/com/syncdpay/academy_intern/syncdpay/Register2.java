package com.syncdpay.academy_intern.syncdpay;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class Register2 extends AppCompatActivity
{
    private EditText acc_no,cvv,branch_code,card_no;
    private Button btnDone;
    private TextView cancel_reg,back;
    private Context context = this;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);

        acc_no = (EditText)findViewById(R.id.et_acc_no_reg);
        cvv = (EditText)findViewById(R.id.et_cvv);
        branch_code = (EditText)findViewById(R.id.et_branch_code_reg);
        card_no = (EditText)findViewById(R.id.et_card_no_reg);

        cancel_reg = (TextView)findViewById(R.id.tv_cancel_reg);
        back = (TextView)findViewById(R.id.tv_back);
        btnDone = (Button)findViewById(R.id.btn_finish_reg);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),Register.class);
                startActivity(intent);
            }
        });

        cancel_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),Login.class);
                startActivity(intent);
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                Bundle bundle = getIntent().getExtras();

                String Username = bundle.get("username").toString();
                String password = bundle.get("password").toString();
                String Name = bundle.get("Name").toString();
                String Surname = bundle.get("Surname").toString();
                String Id = bundle.get("Id").toString();
                String Email = bundle.get("Email").toString();
                String Alternative_Number = bundle.get("Alternative_Number").toString();

                String accountNo = acc_no.getText().toString();
                String cardVerificationValue = cvv.getText().toString();
                String branchCode = branch_code.getText().toString();
                String cardNo = card_no.getText().toString();

                //New Code


                ParseUser post = new ParseUser();


                post.setUsername(Username);
                post.setPassword(password);
                post.setEmail(Email);

                post.put("Name", Name);
                post.put("Surname", Surname);
                post.put("ID_Number", Id);
                post.put("Alternative_Number", Alternative_Number);
                post.put("accountNo", accountNo);
                post.put("cardVerificationValue", cardVerificationValue);
                post.put("branchCode", branchCode);
                post.put("cardNo", cardNo);

                post.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {

                        if(e==null)
                        {
                            progressDialog.dismiss();

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Info");
                            builder.setMessage("Account Created");
                            builder.setCancelable(true);
                            builder.setPositiveButton(
                                    "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent intent = new Intent(getBaseContext(), Login.class);
                                            startActivity(intent);
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();


//                            AlertDialog.Builder alert = new AlertDialog.Builder(Register2.this);
//                            alert.setTitle("Info");
//                            alert.setMessage("Saved");
//                            alert.setPositiveButton("OK", null);
//                            alert.show();

                        }
                        else {
                            // The save failed.
                            progressDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Info");
                            builder.setMessage("Failed, try again "+e.getMessage());
                            builder.setCancelable(true);
                            builder.setPositiveButton(
                                    "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent intent = new Intent(getBaseContext(), Register.class);
                                            startActivity(intent);
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }

                    }
                });
                setProgressBarIndeterminateVisibility(true);



                //Intent intent = new Intent(getBaseContext(),Login.class);
               // startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register2, menu);
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
