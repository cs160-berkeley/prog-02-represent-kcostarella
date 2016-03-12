package com.cs160.kcostarella.represent;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.wearable.view.GridViewPager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Random;


public class CongressWear extends Activity implements SensorEventListener {

    private static GridViewPager pager;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String data = getIntent().getExtras().getString("DATA");

        parseData(data);

        setContentView(R.layout.activity_congress_wear);
        pager = (GridViewPager) findViewById(R.id.pager);

        pager.setAdapter(new NewGridPagerAdapter(this, getFragmentManager()));
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor
                (Sensor.TYPE_ACCELEROMETER);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public RepresentGridPagerAdapter.Page[][] pages;

    void parseData(String data) {

        String[] rows = data.split("\n");
        String[] reps;
        String[] vote;

        pages = new RepresentGridPagerAdapter.Page[2][];

        reps = rows[0].split(",");
        vote = rows[1].split(",");


        RepresentGridPagerAdapter.Page[] repPage = new RepresentGridPagerAdapter.Page[reps.length];
        for (int i = 0; i < reps.length; i++) {
            RepresentGridPagerAdapter.Page p = new RepresentGridPagerAdapter.Page();
            String party = reps[i].split(" ")[2];
            if (party.equals("D")) {
                p.iconRes = R.color.blue;
            } else if (party.equals("R")) {
                p.iconRes = R.color.red;
            } else {
                p.iconRes = R.color.grey;
            }
            p.titleRes = reps[i];
            p.code = i;
            repPage[i] = p;
        }

        RepresentGridPagerAdapter.Page votePage = new RepresentGridPagerAdapter.Page();
        votePage.titleRes = "2012 in... ";
        votePage.bodyRes = vote[0].substring(0,1).toUpperCase() + vote[0].substring(1,vote[0].length()) + "\nObama Vote: " + vote[1] + "\nRomney Vote: " + vote[2];

        pages[0] = repPage;
        pages[1] = new RepresentGridPagerAdapter.Page[]{votePage};

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "CongressWear Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.cs160.kcostarella.represent/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "CongressWear Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.cs160.kcostarella.represent/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    class NewGridPagerAdapter extends RepresentGridPagerAdapter {

        public NewGridPagerAdapter(Context ctx, FragmentManager fm) {
            super(ctx, fm);
        }

        @Override
        public Page[][] getPages() {
           return pages;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private long lastTime = -1;
    final double minLat = -90.00;
    final double maxLat = 90.00;
    final double minLon = 0.00;
    final double maxLon = 180.00;

    @Override
    public final void onSensorChanged(SensorEvent event) {
        // alpha is calculated as t / (t + dT)
        // with t, the low-pass filter's time-constant
        // and dT, the event delivery rate

        if (false && lastTime < event.timestamp) {
            float[] acceleration = event.values;
            float speed = (acceleration[0] + acceleration[1] + acceleration[2]);

            if (speed > 30.0f) {
                Random r = new Random();
                int random = r.nextInt(100) + 1;
                double frac = 1.0f / (float) random;
                Log.d("T", "random is: " + random);
                double latitude = minLat + (double) (Math.random() * ((maxLat - minLat) + 1));
                double longitude = minLon + (double) (Math.random() * ((maxLon - minLon) + 1));
                String out_s = String.format("Lat:%.2f\nLong:%.2f", latitude, longitude);
                Toast toast = Toast.makeText(getApplicationContext(), out_s, Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int change) {
        Toast toast = Toast.makeText(getApplicationContext(), "Accuracy has Changed.", Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

}
