package com.jackson_siro.vsongbook.tools;

import com.jackson_siro.vsongbook.*;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat.Builder;

public class Notifire {
    int icon;
    Intent intent;
    Context mContext;
    String notificationMsg;
    String notificationTitle;
    String ringTonePreference;
    Uri ringToneUri;

    public Notifire(Context mContext) {
        this.mContext = mContext;
    }

    public void createNotification(String vsbKod, String vSongBookCode, int notetype) {
        int serial;
        String key_ID;
        
        serial = Integer.valueOf(vSongBookCode.trim()).intValue();
        
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        Intent intent;
        if (notetype == 1) {
            this.icon = R.drawable.ic_notify;
            this.notificationTitle = "VALID MPesa Received.";
            this.notificationMsg = "Thank you note sent.";
            key_ID = "notification";
            this.intent = new Intent(this.mContext, Settings_III.class);
            intent = this.intent;
            
            intent.putExtra("contacts_name", vsbKod);
            intent = this.intent;
            
            intent.putExtra("contacts_number", vSongBookCode);
            this.ringTonePreference = settings.getString("notifications_new_message_ringtone", "DEFAULT_SOUND");
        } else {
            this.icon = R.drawable.ic_notify;
            this.notificationTitle = "WARNING! Pesa con alert.";
            this.notificationMsg = "Fake PESA message from " + vSongBookCode;
            this.intent = new Intent(this.mContext, XxxCritical.class);
            intent = this.intent;
            
            intent.putExtra("contacts_number", vSongBookCode);
            intent = this.intent;
            
            intent.putExtra("contacts_note", "Sends fake mpesa messages".toUpperCase());
            this.ringTonePreference = settings.getString("notifications_fraud_message_ringtone", "DEFAULT_SOUND");
            key_ID = "warning";
        }
        this.intent.putExtra("key_ID", key_ID);
        this.intent.setFlags(67108864);
        this.intent.setFlags(268435456);
        PendingIntent pIntent = PendingIntent.getActivity(this.mContext, serial, this.intent, 1073741824);
        this.ringToneUri = Uri.parse(this.ringTonePreference);
        Notification noti = new Builder(this.mContext).setContentTitle(this.notificationTitle).setContentText(this.notificationMsg).setSound(this.ringToneUri).setSmallIcon(this.icon).setContentIntent(pIntent).build();
        Context context = this.mContext;
        Context context2 = this.mContext;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        noti.flags |= 16;
        notificationManager.notify(serial, noti);
        if (notetype != 1) {
            this.mContext.startActivity(this.intent);
        }
    }
}
