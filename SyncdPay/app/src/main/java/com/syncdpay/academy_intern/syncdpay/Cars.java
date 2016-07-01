package com.syncdpay.academy_intern.syncdpay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Cars extends AppCompatActivity
{
    private ListView lv_cars;
    private Context context= this;
    private ProgressDialog pd_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cars);

        lv_cars = (ListView)findViewById(R.id.lv_cars);

        pd_dialog = new ProgressDialog(this);
        pd_dialog.setTitle("Processing Auctions");
        pd_dialog.setIndeterminate(true);
        pd_dialog.setCancelable(false);
        pd_dialog.setMessage("Loading. Please wait...");

        //make use of the universal image loader to retrieve and display the relevant images
        //enable image caching because its disabled by default
        DisplayImageOptions defaultOtions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOtions)
                .build();
        ImageLoader.getInstance().init(config);

        new JSONTask().execute("http://www.jeffasenmusic.co.za/Tebza/mycarsAPI.json");
    }

    public class JSONTask extends AsyncTask<String,String,List<CarModel> >
    {
        //we show our progress dialog before the AsyncTask begins, cause when AsyncTask begins that means the app is done loading data
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pd_dialog.show();
        }

        @Override
        protected List<CarModel> doInBackground(String... params)
        {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line ="";

                while((line = reader.readLine()) != null)
                {
                    buffer.append(line);
                }

                String finalJson = buffer.toString();

                JSONObject parentObject = new JSONObject(finalJson);

                JSONArray parentArray = parentObject.getJSONArray("cars");

                List<CarModel> carModelList = new ArrayList<>();

                for(int x = 0; x < parentArray.length(); x++)
                {
                    JSONObject finalObject = parentArray.getJSONObject(x);
                    CarModel carModel = new CarModel();

                    carModel.setLocation(finalObject.getString("propicon_value_2"));
                    carModel.setHighest_offer(finalObject.getString("propprice_value_3_numbers/_source"));
                    carModel.setValue(finalObject.getString("propprice_value_1"));
                    carModel.setImage(finalObject.getString("lazyload_image"));
                    carModel.setKilos(finalObject.getString("propicon_value_1_numbers/_source"));
                    carModel.setMake(finalObject.getString("lazyload_image/_title"));
                    carModel.setModel(finalObject.getString("propstreet_value"));
                    carModel.setYear(finalObject.getString("proptitle_value_numbers/_source"));
                    carModel.setTransmission(finalObject.getString("propicon_value_3"));
                    carModel.setTrade_value(finalObject.getString("propprice_value_2"));

                    carModelList.add(carModel);
                    //finalBufferedData.append(make + " - " + model + "\n");
                }



                return carModelList;

                //return buffer.toString();

            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally
            {
                if(connection != null)
                {
                    connection.disconnect();
                }
                try
                {
                    if (reader != null)
                    {
                        reader.close();
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(List<CarModel> result)
        {
            super.onPostExecute(result);
            pd_dialog.dismiss();
            CarAdapter adapter = new CarAdapter(context,R.layout.row_2,result);
            lv_cars.setAdapter(adapter);
        }
    }

    public class CarAdapter extends ArrayAdapter
    {
        private List<CarModel> carModelList;
        private int resource;
        private LayoutInflater inflater;

        public CarAdapter(Context context, int resource, List<CarModel> objects)
        {
            super(context, resource, objects);
            carModelList = objects;
            this.resource = resource;
            inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(resource, null);

                holder.iv_homeIcon = (ImageView) convertView.findViewById(R.id.iv_icon);

                holder.tv_make = (TextView) convertView.findViewById(R.id.tv_make_1);
                holder.tv_value = (TextView) convertView.findViewById(R.id.tv_value);
                holder.tv_location = (TextView) convertView.findViewById(R.id.tv_location);
                holder.tv_kilos = (TextView) convertView.findViewById(R.id.tv_kilos);

                holder.tv_make_2 = (TextView) convertView.findViewById(R.id.tv_make_2);
                holder.tv_model = (TextView) convertView.findViewById(R.id.tv_model);
                holder.tv_year = (TextView) convertView.findViewById(R.id.tv_year);
                holder.tv_transmission = (TextView) convertView.findViewById(R.id.tv_transmission);
                holder.tv_kilos_2 = (TextView) convertView.findViewById(R.id.tv_kilos_2);
                holder.tv_location_2 = (TextView) convertView.findViewById(R.id.tv_location_2);
                holder.tv_value_2 = (TextView) convertView.findViewById(R.id.tv_value_2);
                holder.tv_highest_offer = (TextView) convertView.findViewById(R.id.tv_highest_offer);
                holder.tv_trade_value = (TextView) convertView.findViewById(R.id.tv_trade_value);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            final ProgressBar pg_img_prog_bar;

            pg_img_prog_bar = (ProgressBar) convertView.findViewById(R.id.pg_img_prg_bar);

            ImageLoader.getInstance().displayImage(carModelList.get(position).getImage(), holder.iv_homeIcon, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    pg_img_prog_bar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    pg_img_prog_bar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    pg_img_prog_bar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    pg_img_prog_bar.setVisibility(View.GONE);
                }
            });

            holder.tv_location.setText("LOCATION " + carModelList.get(position).getLocation());
            holder.tv_location_2.setText("LOCATION " + carModelList.get(position).getLocation());

            holder.tv_value.setText("VALUE " + carModelList.get(position).getValue());
            holder.tv_value_2.setText("VALUE " + carModelList.get(position).getValue());


            holder.tv_make.setText("MAKE " + carModelList.get(position).getMake());
            holder.tv_make_2.setText("MAKE " + carModelList.get(position).getMake());

            holder.tv_kilos.setText("KILOMETERS " + carModelList.get(position).getKilos() + " KM");
            holder.tv_kilos_2.setText("KILOMETERS " + carModelList.get(position).getKilos() + " KM");

            holder.tv_model.setText("MODEL " + carModelList.get(position).getModel());
            holder.tv_year.setText("YEAR " + carModelList.get(position).getYear());
            holder.tv_transmission.setText("TRANSMISSION " + carModelList.get(position).getTransmission());
            holder.tv_highest_offer.setText("HIGHEST OFFER " + "R" + carModelList.get(position).getHighest_offer());
            holder.tv_trade_value.setText("TRADE VALUE " + carModelList.get(position).getTrade_value());

            return convertView;
        }
        class ViewHolder
        {
            private ImageView iv_homeIcon;
            private TextView tv_make;
            private TextView tv_value;
            private TextView tv_location;
            private TextView tv_kilos;


            private TextView tv_make_2;
            private TextView tv_model;
            private TextView tv_year;
            private TextView tv_transmission;
            private TextView tv_kilos_2;
            private TextView tv_location_2;
            private TextView tv_value_2;
            private TextView tv_highest_offer;
            private TextView tv_trade_value;
        }
    }
}
