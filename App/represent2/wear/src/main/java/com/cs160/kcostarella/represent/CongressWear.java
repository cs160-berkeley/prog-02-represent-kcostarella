package com.cs160.kcostarella.represent;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.view.GridViewPager;
import android.util.Log;
import android.widget.Toast;

import java.util.Random;

public class CongressWear extends Activity implements SensorEventListener {

    private static GridViewPager pager;
    private SensorManager mSensorManager;
    private Sensor mSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congress_wear);
        pager = (GridViewPager) findViewById(R.id.pager);
        pager.setAdapter(new RepresentGridPagerAdapter(this, getFragmentManager()));
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
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
        if (lastTime < event.timestamp) {
            float[] acceleration = event.values;
            float speed = (acceleration[0] + acceleration[1] + acceleration[2]);

            if (speed > 30.0f) {
                Random r = new Random();
                int random = r.nextInt(100) + 1;
                double frac = 1.0f/(float)random;
                Log.d("T","random is: " + random);
                double latitude = minLat + (double)(Math.random() * ((maxLat - minLat) + 1));
                double longitude = minLon + (double)(Math.random() * ((maxLon - minLon) + 1));
                String out_s = String.format("Lat:%.2f\nLong:%.2f", latitude, longitude);
                Toast toast = Toast.makeText(getApplicationContext(),out_s, Toast.LENGTH_LONG);
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
