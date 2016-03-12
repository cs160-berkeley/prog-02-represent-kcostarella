package com.cs160.kcostarella.represent;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import io.fabric.sdk.android.Fabric;


public class CongressActivity extends AppCompatActivity {
    public static final String sunlight_url1 = "https://congress.api.sunlightfoundation.com/bills?sponsor_id=";
    public static final String sunlight_url2= "https://congress.api.sunlightfoundation.com/committees?member_ids=";
    public static final String sunlight_url3 = "https://congress.api.sunlightfoundation.com/legislators?bioguide_id=";
    public static final String sunlight_apiKey = "&apikey=a178ddb86c0245a687c357001769b5a6";
    public final String TWITTER_KEY = "7GpV9FPwAxUDOCPCx3uONHToR";
    public final String TWITTER_SECRET = "Wan94Bn3yqs42lo8DT0IjbbUmdEuyvMPOa9RuQhImHzPIAYJ9J";
    public static  RetrieveSunlightTask task;


    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    public Bundle extra;
    public String lat;
    public String lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congress);
        extra = getIntent().getExtras();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        task = new RetrieveSunlightTask();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        String json = getIntent().getExtras().getString("JSON");
        String googleJson = getIntent().getExtras().getString("GOOGLE_JSON");
        parseJson(json, googleJson);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_congress, menu);
        return true;
    }


    static Rep[] Reps;

    static public void LaunchInfoView(int i) {
        String id = Reps[i].id;
        task.create().execute(sunlight_url1 + id + sunlight_apiKey, sunlight_url2 + id + sunlight_apiKey, sunlight_url3 + id + sunlight_apiKey);
    }



    public void parseJson(String jsonString, String googleJson) {
        try {

            //PARSE REP DATA
            JSONObject jsonRootObject = new JSONObject(jsonString);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.optJSONArray("results");
            //Probably start Watch here...
            //Iterate the jsonArray and fill relevant tables
            Reps = new Rep[3];
            String data = "";
            int offset = 0;
            for (int i = 0; i < jsonArray.length(); i++) {

                Rep r = new Rep();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                r.title = jsonObject.optString("title");
                r.name = jsonObject.optString("first_name") + " " + jsonObject.optString("last_name");
                r.twitterID = jsonObject.optString("twitter_id");
                r.party = jsonObject.optString("party");
                r.website = jsonObject.optString("website");
                r.email = jsonObject.optString("oc_email");
                r.term_start = jsonObject.optString("term_start");
                r.term_end = jsonObject.optString("term_end");
                r.id = jsonObject.optString("bioguide_id");

                Boolean bad = r.title.equalsIgnoreCase("rep");
                Boolean ok = true;
                for (int j = 0; j < i; j++) {
                    if (bad && Reps[j].title.equalsIgnoreCase("rep")) {
                        ok = false;
                    }
                }

                if (ok) {
                    Reps[i + offset] = r;
                    data += r.name + " " + r.party + ",";
                } else {
                    offset -= 1;
                }
            }


            if (data.length() != 0) {
                data = data.substring(0, data.length() - 1);
            }
            data += "\n";

            String county = "";

            //GET COUNTY FROM GOOGLE JSON
            JSONObject googleRoot = new JSONObject(googleJson);
            //get result
            JSONArray resultsArray = googleRoot.optJSONArray("results");
            //get first result
            JSONObject first = resultsArray.getJSONObject(0);
            //get address component of first result, get its array
            JSONArray addressArray = first.optJSONArray("address_components");

            JSONObject o;
            for (int i = 0; i < addressArray.length(); i++) {
                o = addressArray.getJSONObject(i);
                if (o.getJSONArray("types").getString(0).equals("administrative_area_level_2")) {
                    county = o.optString("short_name").split(" ")[0].toLowerCase();
                    break;
                }
            }

            if (county == "") {
                Log.d("T","County was not set, check Google Map Parse");
                return;
            }

            JSONArray voteArray = new JSONArray(getJSONString(this));
            Boolean success = false;

            for (int i = 0; i < voteArray.length(); i++) {
                JSONObject voteCounty = voteArray.getJSONObject(i);
                if (voteCounty.optString("county-name").toLowerCase().equals(county)) {
                    data += county + "," + voteCounty.optString("obama-percentage") + "," +  voteCounty.optString("romney-percentage");
                    success = true;
                    break;
                }
            }


            if (!success) {
                Log.d("T","Couldnt find county: " + county + " in JSON string, maybe try again");
                return;

            }

            startWatch(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getJSONString(Context context)
    {
        String str = "";
        try
        {
            AssetManager assetManager = context.getAssets();
            InputStream in = assetManager.open("election-county-2012.json");
            InputStreamReader isr = new InputStreamReader(in);
            char [] inputBuffer = new char[100];

            int charRead;
            while((charRead = isr.read(inputBuffer))>0)
            {
                String readString = String.copyValueOf(inputBuffer,0,charRead);
                str += readString;
            }
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }

        return str;
    }





    public Boolean CallTwitterAPI() {
        return true;
    }



    public void startWatch(String initialData) {
        Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
        sendIntent.putExtra("ARG", "start");
        String data = initialData;
        sendIntent.putExtra("OPT", data);
        startService(sendIntent);
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
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Congress Page", // TODO: Define a title for the content shown.
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
                "Congress Page", // TODO: Define a title for the content shown.
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class CongressFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static Boolean running = false;
        private static final String ARG_TITLE = "title";
        private static final String ARG_IMAGE_NUMBER = "image_number";
        private static final String ARG_NAME = "name";
        private static final String ARG_PARTY = "party";
        private static final String ARG_EMAIL = "email";
        private static final String ARG_WEBSITE = "website";
        private static final String ARG_ID = "id";

        public final String sunlight_url1 = "https://congress.api.sunlightfoundation.com/bills?sponsor_id=";
        public final String sunlight_url2= "https://congress.api.sunlightfoundation.com/committees?member_ids=";
        public final String sunlight_url3 = "https://congress.api.sunlightfoundation.com/legislators?bioguide_id=";
        public static RetrieveSunlightTask task;

        public final String sunlight_apiKey = "&apikey=a178ddb86c0245a687c357001769b5a6";




        public CongressFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static CongressFragment newInstance(int sectionNumber, RetrieveSunlightTask doTask) {
            if (task == null) {
                task = doTask;
            }
            if (sectionNumber < Reps.length) {
                CongressFragment fragment = new CongressFragment();
                Bundle args = new Bundle();
                args.putString(ARG_TITLE, Reps[sectionNumber].title);
                args.putInt(ARG_IMAGE_NUMBER,R.drawable.common_google_signin_btn_icon_dark );
                args.putString(ARG_NAME, Reps[sectionNumber].name);
                args.putString(ARG_PARTY, Reps[sectionNumber].party);
                args.putString(ARG_EMAIL, Reps[sectionNumber].email);
                args.putString(ARG_WEBSITE, Reps[sectionNumber].website);
                args.putString(ARG_ID, Reps[sectionNumber].id);
                fragment.setArguments(args);
                return fragment;
            }
            CongressFragment fragment = new CongressFragment();
            Bundle args = new Bundle();
            args.putString(ARG_TITLE, "Blah");
            args.putInt(ARG_IMAGE_NUMBER, R.drawable.common_google_signin_btn_icon_dark);
            args.putString(ARG_NAME, "Blah");
            args.putString(ARG_PARTY, "Bleeh");
            args.putString(ARG_EMAIL, "bloooh");
            args.putString(ARG_WEBSITE, "blech");
            fragment.setArguments(args);
            return fragment;

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_congress, container, false);
            TextView name = (TextView) rootView.findViewById(R.id.section_label);
            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);

            Button email = (Button) rootView.findViewById(R.id.email);

            email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto",getArguments().getString(ARG_EMAIL), null));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Hello " + getArguments().getString(ARG_NAME));
                    intent.putExtra(Intent.EXTRA_TEXT, "your message here");
                    startActivity(Intent.createChooser(intent, "Choose an Email client :"));

                }
            });

            Button website = (Button) rootView.findViewById(R.id.website);
            website.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String url = getArguments().getString(ARG_WEBSITE);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });

            FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = getArguments().getString(ARG_ID);
                    task.create().execute(sunlight_url1 + id + sunlight_apiKey, sunlight_url2 + id + sunlight_apiKey, sunlight_url3 + id + sunlight_apiKey);
                }
            });

            TextView tweet = (TextView) rootView.findViewById(R.id.tweet);

            name.setText(getArguments().getString(ARG_TITLE) + " " + getArguments().getString(ARG_NAME));

            imageView.setImageResource(getArguments().getInt(ARG_IMAGE_NUMBER));
            return rootView;
        }
    }


     class RetrieveSunlightTask extends AsyncTask<String, Void, String[]> {

        private Exception exception;
         public RetrieveSunlightTask create () {
             return new RetrieveSunlightTask();
         }
        protected void onPreExecute() {

        }



        protected String[] doInBackground(String... urls) {
            // Do some validation here

            try {
                URL url = new URL(urls[0]);
                URL url2 = new URL(urls[1]);
                URL url3 = new URL(urls[2]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                HttpURLConnection urlConnection2 = (HttpURLConnection) url2.openConnection();
                HttpURLConnection urlConnection3 = (HttpURLConnection) url3.openConnection();


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

                    BufferedReader bufferedReader3 = new BufferedReader(new InputStreamReader(urlConnection3.getInputStream()));
                    StringBuilder stringBuilder3 = new StringBuilder();

                    while ((line = bufferedReader3.readLine()) != null) {
                        stringBuilder3.append(line).append("\n");
                    }
                    bufferedReader3.close();


                    return new String[] {stringBuilder.toString(), stringBuilder2.toString(), stringBuilder3.toString()};
                }
                finally{
                    urlConnection.disconnect();
                    urlConnection2.disconnect();
                    urlConnection3.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String[] response) {
            if (response[0] == null || response[1] == null || response[2] == null) {
                response[0] = response[1] = "THERE WAS AN ERROR";
            }
            String billString = response[0];
            String comiteeString = response[1];
            String repInfo = response[2];
            Intent changeActivity = new Intent(getBaseContext(), InfoActivity.class);
            changeActivity.putExtra("BILL_JSON", billString);
            changeActivity.putExtra("COMMITEE_JSON", comiteeString);
            changeActivity.putExtra("REP_JSON", repInfo);
            startActivity(changeActivity);

        }
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        Context context;
        public RetrieveSunlightTask task = new RetrieveSunlightTask();
        public SectionsPagerAdapter(FragmentManager fm, Context ctx) {
            super(fm);
            context = ctx;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return CongressFragment.newInstance(position, task);
        }

        @Override
        public int getCount() {
            // Show Reps.length total pages.
            return Reps.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}
