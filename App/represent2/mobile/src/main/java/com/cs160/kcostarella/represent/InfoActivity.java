package com.cs160.kcostarella.represent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Bundle extra = getIntent().getExtras();
        //int image_id = extra.getInt("ARG");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView i = (ImageView) findViewById(R.id.image);
        i.setImageResource(R.drawable.common_google_signin_btn_icon_dark);

        TextView body = (TextView) findViewById(R.id.text);

        String content = "";
        content += parseJson(extra.getString("REP_JSON"));
        content += "\n\n";
        content += "COMMITTEES\n\n";
        content += ParseBillJson(extra.getString("BILL_JSON"));
        content += "\nBILLS\n\n";
        content += ParseCommiteeJson(extra.getString("COMMITEE_JSON"));

        body.setText(content);
    }


    public String ParseBillJson(String jsonString) {
        String data = "";
        try {
            JSONObject jsonRootObject = new JSONObject(jsonString);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.optJSONArray("results");
            //Probably start Watch here...
            //Iterate the jsonArray and fill relevant tables
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String s = jsonObject.optString("short_title");
                if (!(s.equals("") || s.equals("null"))) {
                    data += s.toUpperCase();
                    data += "\n";
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }


    public String ParseCommiteeJson(String jsonString) {
        String data = "";

        try {
            JSONObject jsonRootObject = new JSONObject(jsonString);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.optJSONArray("results");
            //Probably start Watch here...
            //Iterate the jsonArray and fill relevant tables
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String s = jsonObject.optString("name");
                if (!(s.equals("") || s.equals("null"))) {
                    data += s.toUpperCase();
                    data += "\n";
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    public String parseJson(String jsonString) {
        String data = "";
        try {
            JSONObject jsonRootObject = new JSONObject(jsonString);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.optJSONArray("results");
            //Probably start Watch here...
            //Iterate the jsonArray and fill relevant tables
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.optString("title");
                String name = jsonObject.optString("first_name") + " " + jsonObject.optString("last_name");

                String party = jsonObject.optString("party");
                String start  = jsonObject.optString("term_start");
                String end  = jsonObject.optString("term_end");

                if (party.equalsIgnoreCase("d")) {
                    party = "Democrat";
                } else if (party.equalsIgnoreCase("r")) {
                    party = "Republican";
                } else {
                    party = "Independent";
                }

                data =  title + " " +  name + "\n" + party + "\n" + "Term Started: " + start + "\nTerm Ends: " + end + "\n";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

}
