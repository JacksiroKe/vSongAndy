package com.jackson_siro.vsongbook.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import java.util.Map;

public class IncomingSms extends BroadcastReceiver {
    
    String message;
    String sender;
    SharedPreferences settings;
    final SmsManager sms;

    public IncomingSms() {
        this.sms = SmsManager.getDefault();
    }

    public void onReceive(Context context, Intent intent) {
        
        this.settings = PreferenceManager.getDefaultSharedPreferences(context);
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            try {
                SmsMessage messages = SmsMessage.createFromPdu((byte[]) ((Object[]) bundle.get("pdus"))[0]);
                this.message = messages.getDisplayMessageBody();
                this.sender = messages.getDisplayOriginatingAddress();
                if (this.sender.equalsIgnoreCase("MPESA")) {
                    new Notifire(context).createNotification("", "", 1);
                     
                } else if (!this.message.contains("JACKSON SIRO") || !this.message.contains("Confirmed")) {
                } else {
                    if (this.message.contains("sent") || this.message.contains("received")) {
                        new Notifire(context).createNotification("", this.sender, 2);
                        
                    }
                }
            } catch (Exception e) {
                Log.e("IncomingMessage", "Exception smsReceiver" + e);
            }
        }
    }
}
