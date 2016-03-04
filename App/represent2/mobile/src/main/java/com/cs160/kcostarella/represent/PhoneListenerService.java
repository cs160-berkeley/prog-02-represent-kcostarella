package com.cs160.kcostarella.represent;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by joleary and noon on 2/19/16 at very late in the night. (early in the morning?)
 */
public class PhoneListenerService extends WearableListenerService {

    //   WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.
    private static final String INFO = "/info";

    private final int[] lookup = new int[] {R.drawable.barbara_lee,R.drawable.barbara_boxer,R.drawable.dianne_feinstein};
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath() +  " with code " + new String(messageEvent.getData()));
        if( messageEvent.getPath().equalsIgnoreCase(INFO) ) {
            String opt = new String(messageEvent.getData());
            int id = lookup[Integer.parseInt(opt)];

            Intent changeActivity = new Intent(getApplicationContext(), InfoActivity.class);
            changeActivity.putExtra("ARG",id);
            changeActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(changeActivity);


            // so you may notice this crashes the phone because it's
            //''sending message to a Handler on a dead thread''... that's okay. but don't do this.
            // replace sending a toast with, like, starting a new activity or something.
            // who said skeleton code is untouchable? #breakCSconceptions

        } else {
            super.onMessageReceived( messageEvent );
        }



    }
}
