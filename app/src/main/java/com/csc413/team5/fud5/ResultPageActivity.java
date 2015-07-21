package com.csc413.team5.fud5;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

//imports for images
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
//TODO:remove these imports when Selector is implemented
import com.csc413.team5.restaurantapiwrapper.*;


import com.csc413.team5.fud5.R;
import org.json.JSONException;
import java.io.IOException;

public class ResultPageActivity extends AppCompatActivity {
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_result_page);
        GetResultTask task = new GetResultTask();
        task.execute();
        setUpMapIfNeeded();
    }

    //TODO:remove when Selector implemented
    private class GetResultTask extends AsyncTask<String, Void, Restaurant> {

        protected void onPostExecute(Restaurant result) {
            //display restaurant info goes here.
            //restaurant image loading needs to go in yet another asynctask
            LoadImageTask task = new LoadImageTask();
            task.execute(result);
        }
        @Override
        protected Restaurant doInBackground(String... params)  {
            // Construct a YelpApiKey from Resource strings
            String consumerKey = getApplicationContext().getResources()
                    .getString(R.string.yelp_consumer_key);
            String consumerSecret = getApplicationContext().getResources()
                    .getString(R.string.yelp_consumer_secret);
            String tokenKey = getApplicationContext().getResources()
                    .getString(R.string.yelp_token);
            String tokenSecret = getApplicationContext().getResources()
                    .getString(R.string.yelp_token_secret);
            YelpApiKey yelpKey = new YelpApiKey(consumerKey, consumerSecret, tokenKey, tokenSecret);

            RestaurantApiClient rClient = new RestaurantApiClient.Builder(yelpKey)
                    .id("new-tsing-tao-restaurant-san-francisco").build();
            try {
                return rClient.getRestaurantByYelpID();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        }
    }

    //END remove

    private class LoadImageTask extends AsyncTask<Restaurant, Void, Bitmap> {
        protected void onPostExecute(Bitmap result) {
            ImageView background = (ImageView) findViewById(R.id.imgBackground);
            background.setImageBitmap(result);
        }
        @Override
        protected Bitmap doInBackground(Restaurant... params)  {

            try {
                String imageUrl = params[0].getImageUrl().toString();
                imageUrl =  imageUrl.replace("ms.jpg","o.jpg"); //this gets original image size
                URL url = new URL(imageUrl);
                InputStream is = url.openConnection().getInputStream();
                return BitmapFactory.decodeStream(is);
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
    }

/*
        setContentView(R.layout.activity_result_page);
        Typeface buttonFont = Typeface.createFromAsset(getAssets(), "Chunkfive.otf");
        Button greenButton = (Button) findViewById(R.id.greenButton);
        greenButton.setTypeface(buttonFont);
        //TextView myTextView = (TextView)findViewById(R.id.greenButton);
       // myTextView.setTypeface(buttonFont);
*/

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {

        LatLng sfsu = new LatLng(37.782458, -122.392828); //test latitude longitude

        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sfsu, 13));//sets the view

        mMap.addMarker(new MarkerOptions()
                .position(sfsu)
                .title("New Location"));


    }

    protected void onResume() {
        super.onResume();
    }
}
