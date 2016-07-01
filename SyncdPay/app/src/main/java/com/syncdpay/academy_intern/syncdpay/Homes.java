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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
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

public class Homes extends AppCompatActivity
{

    private ListView lv_homes;
    private ProgressDialog pd_progress_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homes);

        lv_homes = (ListView)findViewById(R.id.lv_homes);
        // Button btn_view = (Button)findViewById(R.id.btn_view_auct_homes);

        pd_progress_dialog = new ProgressDialog(this);
        //indeterminate is for when we dont know for how long the progress bar will run
        pd_progress_dialog.setIndeterminate(true);
        //cant cancel it
        pd_progress_dialog.setCancelable(false);
        pd_progress_dialog.setMessage("Retrieving data. Please wait...");

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

        new JSONTask().execute("https://api.myjson.com/bins/2cvtl");

        /*btn_view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //when button is clicked call the JSONTask and its method execute
                new JSONTask().execute("https://api.myjson.com/bins/2cvtl");
                //json mycarsAPI url (https://api.myjson.com/bins/4qlp9)
            }
        });*/
    }

    private class JSONTask extends AsyncTask<String,String,List<HomeModel> >
    {
        //we show our progress dialog before the AsyncTask begins, cause when AsyncTask begins that means the app is done loading data
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pd_progress_dialog.show();
        }

        @Override
        protected List<HomeModel> doInBackground(String... params)
        {
            //the cream between two orio's (the connection)
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try
            {
                //our URL where our .JSON file is located goes here
                URL url = new URL(params[0]);

                //open connection to the server
                connection = (HttpURLConnection) url.openConnection();

                //connect to the server
                connection.connect();

                //receive the data stream and store it in the stream object
                InputStream stream = connection.getInputStream();

                //this class is to read our data(data which will be stored in our stream object)
                reader = new BufferedReader(new InputStreamReader(stream));

                //instead of String we use an object of type stingbuffer or stringbuilder(builder is not thread safe, not synchronised. But builder is faster)
                StringBuffer buffer = new StringBuffer();

                //test weather theres a stream of data coming in
                String line = "";
                while ((line = reader.readLine()) != null)
                {
                    buffer.append(line);
                }

                String finalJson = buffer.toString();

                //first JSONobject in json file(parent object){}
                JSONObject parentObject = new JSONObject(finalJson);

                //the JSONarray in our json object
                JSONArray parentArray = parentObject.getJSONArray("homes");

                //watch out for this
                List<HomeModel> homeModelList = new ArrayList<>();

                for (int x = 0; x < parentArray.length(); x++)
                {
                    JSONObject finalObject = parentArray.getJSONObject(x);
                    HomeModel homeModel = new HomeModel();
                    //extract exactly what you want from object
                    homeModel.setLocation(finalObject.getString("propstreet_value"));
                    homeModel.setKey_features(finalObject.getString("lazyload_image/_title"));
                    homeModel.setValue(finalObject.getString("r_number/_source"));
                    homeModel.setImage(finalObject.getString ("lazyload_image"));
                    //finalBufferedData.append("LOCATION:" + location + " - " + "\n" + "KEY_FEATURES:" + "" + key_feature + " - " + "\n" + "PRICE:" + value + "\n");
                    homeModelList.add(homeModel);
                }

                return homeModelList;

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
                //close connection
                connection.disconnect();


                try
                {
                        /*if reader has something then stop reading
                         if reader is null that means it has its initial value so dont close
                         */
                    if(reader != null)
                    {
                        //close reader after
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
        protected void onPostExecute(List<HomeModel> result)
        {
            super.onPostExecute(result);
            pd_progress_dialog.dismiss();
            HomeAdapter adapter = new HomeAdapter(getApplicationContext(),R.layout.row,result);
            lv_homes.setAdapter(adapter);
        }


        public class HomeAdapter extends ArrayAdapter
        {

            private List<HomeModel> homeModelList;
            private int resource;
            private LayoutInflater inflater;

            public HomeAdapter(Context context, int resource, List<HomeModel> objects)
            {
                super(context, resource, objects);
                homeModelList = objects;
                this.resource = resource;
                inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                //viewHolder pattern to minimise cpu usage
                ViewHolder viewHolder = null;
                //if we're getting our items for the first time
                if(convertView == null)
                {
                    viewHolder = new ViewHolder();
                    convertView = inflater.inflate(resource,null);
                    //all our ID'd and views will be fetched once, and tag them into our holder
                    viewHolder.iv_homeIcon = (ImageView)convertView.findViewById(R.id.iv_icon);
                    viewHolder.tv_value = (TextView)convertView.findViewById(R.id.tv_value);
                    viewHolder.tv_location = (TextView)convertView.findViewById(R.id.tv_location);
                    viewHolder.tv_key_features = (TextView)convertView.findViewById(R.id.tv_key_features);
                    viewHolder.tv_value_2 = (TextView)convertView.findViewById(R.id.tv_value_2);
                    viewHolder.tv_location_2 = (TextView)convertView.findViewById(R.id.tv_location_2);
                    viewHolder.tv_key_feat_2 = (TextView)convertView.findViewById(R.id.tv_key_feat_2);
                    convertView.setTag(viewHolder);
                }
                //else if we're NOT getting the items for the first time
                else
                {
                    //initialise our viewHolder
                    viewHolder = (ViewHolder)convertView.getTag();
                }


                final ProgressBar pb_progressBar;

                pb_progressBar = (ProgressBar)convertView.findViewById(R.id.pg_progressBar);



                ImageLoader.getInstance().displayImage(homeModelList.get(position).getImage(), viewHolder.iv_homeIcon, new ImageLoadingListener()
                {
                    @Override
                    public void onLoadingStarted(String imageUri, View view)
                    {
                        pb_progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason)
                    {
                        pb_progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
                    {
                        pb_progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view)
                    {
                        pb_progressBar.setVisibility(View.GONE);
                    }
                });

                viewHolder.tv_value.setText("Property value: " + "R"+String.valueOf(homeModelList.get(position).getValue()));
                viewHolder. tv_value_2.setText("Property value: " + "R"+String.valueOf(homeModelList.get(position).getValue()));
                viewHolder. tv_location.setText("Location: " + homeModelList.get(position).getLocation());
                viewHolder. tv_location_2.setText("Location: " + homeModelList.get(position).getLocation());
                viewHolder. tv_key_features.setText("Key features: " + homeModelList.get(position).getKey_features());
                viewHolder. tv_key_feat_2.setText("Key features: " + homeModelList.get(position).getKey_features());
                return convertView;
            }
        }

        class ViewHolder
        {
            private ImageView iv_homeIcon;
            private TextView tv_value;
            private TextView tv_location;
            private TextView tv_key_features;
            private TextView tv_value_2;
            private TextView tv_location_2;
            private TextView tv_key_feat_2;
            private RatingBar rb_homes;
        }

    }

}
