package com.aware.plugin.layout;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.aware.utils.IContextCard;

public class ContextCard implements IContextCard {

    //Constructor used to instantiate this card
    public ContextCard() {
    }

    private TextView hello = null;

    @Override
    public View getContextCard(Context context) {
        //Load card layout
        View card = LayoutInflater.from(context).inflate(R.layout.card, null);
        hello = card.findViewById(R.id.hello);

        //Register the broadcast receiver that will update the UI from the background service (Plugin)
        IntentFilter filter = new IntentFilter("LAYOUT_DATA");
        context.registerReceiver(layoutObserver, filter);

        //Return the card to AWARE/apps
        return card;
    }

    //This broadcast receiver is auto-unregistered because it's not static.
    private LayoutObserver layoutObserver = new LayoutObserver();

    public class LayoutObserver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("LAYOUT_DATA")) {
                ContentValues data = intent.getParcelableExtra("data");

                String dataValue = data.toString();

                if(dataValue.length() > 500) {
                    hello.setText(dataValue.substring(0, 500));
                } else {
                    hello.setText(dataValue);
                }

            }
        }
    }
}
