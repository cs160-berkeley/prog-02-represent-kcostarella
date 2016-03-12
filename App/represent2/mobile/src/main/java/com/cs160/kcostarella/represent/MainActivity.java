package com.cs160.kcostarella.represent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    public final String sunlight_url = "https://congress.api.sunlightfoundation.com/legislators/locate?";
    public final String sunlight_apiKey = "&apikey=a178ddb86c0245a687c357001769b5a6";
    public final String TWITTER_KEY = "7GpV9FPwAxUDOCPCx3uONHToR";
    public final String TWITTER_SECRET = "Wan94Bn3yqs42lo8DT0IjbbUmdEuyvMPOa9RuQhImHzPIAYJ9J";
    public final String googleLocator_latlong = "https://maps.googleapis.com/maps/api/geocode/json?latlng="; //need comma between valus
    public final String googleAPIKEY = "&key=AIzaSyDe0HweVKQdg9RR4AUPJG3g6T8eLmE6lGA";
    public final String getGoogleLocator_zip = "https://maps.googleapis.com/maps/api/geocode/json?address=";
    //there's not much interesting happening. when the buttons are pressed, they start
    //the PhoneToWatchService with the cat name passed in.

    private Button button;
    private Button location;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    String mLatitudeText;
    String mLongitudeText;
    private EditText zip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);
        final Context ctx = this;
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


        button = (Button) findViewById(R.id.button);
        location = (Button) findViewById(R.id.button2);
        zip = (EditText) findViewById(R.id.editText);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);

                if (zip.getText().toString().length() != 5) {
                    new AlertDialog.Builder(ctx)
                            .setTitle("Invalid ZipCode")
                            .setMessage("5 Numbers, c'mon...")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else {

                    String URL = sunlight_url + "zip=" + zip.getText().toString() + sunlight_apiKey;
                    String URL2 = getGoogleLocator_zip + zip.getText().toString() + googleAPIKEY;
                    new RetrieveSunlightTask().execute(URL, URL2);

                }
            }

        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //connects to the client
                mGoogleApiClient.connect();

                if (mLastLocation != null) {
                    String URL = sunlight_url + "latitude=" + mLatitudeText + "&longitude=" + mLongitudeText + sunlight_apiKey;
                    String URL2 = googleLocator_latlong + mLatitudeText + "," + mLongitudeText + googleAPIKEY;

                    new RetrieveSunlightTask().execute(URL, URL2);
                }
            }
        });
    }


    class RetrieveSunlightTask extends AsyncTask<String, Void, String[]> {

        private Exception exception;
        protected void onPreExecute() {

        }

        protected String[] doInBackground(String... urls) {
            // Do some validation here

            try {
                URL url = new URL(urls[0]);
                URL url2 = new URL(urls[1]);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                HttpURLConnection urlConnection2 = (HttpURLConnection) url2.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();

                    BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(urlConnection2.getInputStream()));
                    StringBuilder stringBuilder2 = new StringBuilder();
                    while ((line = bufferedReader2.readLine()) != null) {
                        stringBuilder2.append(line).append("\n");
                    }
                    bufferedReader2.close();

                    return new String[] {stringBuilder.toString(), stringBuilder2.toString()};


                }
                finally{
                    urlConnection.disconnect();
                    urlConnection2.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String[] response) {
            if (response[0] == null || response[1] == null) {
                response[0] = response[1] = "THERE WAS AN ERROR";
            }
            String jsonString = response[0];
            String googleString = response[1];
            Intent changeActivity = new Intent(getBaseContext(), CongressActivity.class);
            changeActivity.putExtra("ARG", "zip");
            changeActivity.putExtra("JSON", jsonString);
            changeActivity.putExtra("GOOGLE_JSON",googleString);
            startActivity(changeActivity);

        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

    }
    protected void onStop() {
            mGoogleApiClient.disconnect();
            super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) throws SecurityException {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitudeText = String.valueOf(mLastLocation.getLatitude());
            mLongitudeText = String.valueOf(mLastLocation.getLongitude());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
