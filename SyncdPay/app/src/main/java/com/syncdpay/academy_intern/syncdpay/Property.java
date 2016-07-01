package com.syncdpay.academy_intern.syncdpay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class Property extends AppCompatActivity
{
    private TextView tv_view_homes,tv_view_cars,tv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);

        tv_view_cars = (TextView)findViewById(R.id.tv_view_cars);
        tv_view_homes = (TextView)findViewById(R.id.tv_view_homes);
        tv_back = (TextView)findViewById(R.id.tv_back_property);

        tv_view_homes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getBaseContext(),Homes.class);
                startActivity(intent);
            }
        });

        tv_view_cars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),Cars.class);
                startActivity(intent);
            }
        });
    }

}
