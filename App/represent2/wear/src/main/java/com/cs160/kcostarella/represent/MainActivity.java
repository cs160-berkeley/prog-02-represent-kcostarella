package com.cs160.kcostarella.represent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.textView);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();



        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
                startService(sendIntent);
                Intent changeView = new Intent(getBaseContext(), MainActivity.class);
                changeView.putExtra("SHUFFLE",false);
                startActivity(changeView);
            }
        });
    }
}
