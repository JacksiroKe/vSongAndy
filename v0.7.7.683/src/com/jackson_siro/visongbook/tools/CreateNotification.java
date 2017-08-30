package com.jackson_siro.visongbook.tools;

import com.jackson_siro.visongbook.*;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat.Builder;

public class CreateNotification {
    int icon;
    Intent intent;
    Context mContext;
    String notificationMsg;
    String notificationTitle;
    String ringTonePreference;
    Uri ringToneUri;

    public CreateNotification(Context mContext) {
        this.mContext = mContext;
    }

    public void createNotification(int icontype) {
        
        if (icontype == 1) {
            icon = R.drawable.ic_launcher;
            notificationTitle = "vSongBook has been Activated!";
            notificationMsg = "God bless you! vSongBook has been activated by M-PESA. Enjoy";
            
        } else if (icontype == 2) {
        	icon = R.drawable.ic_launcher;
        	notificationTitle = "vSongBook has been Activated!";
            notificationMsg = "God bless you! vSongBook has been activated by AirtelMoney. Enjoy";
            
        } else if (icontype == 3) {
        	icon = R.drawable.ic_launcher;
        	notificationTitle = "vSongBook has been Activated!";
            notificationMsg = "God bless you! vSongBook has been activated by Dev Jack Siro. Enjoy";
            
        } else {
        	icon = R.drawable.ic_launcher;
        	notificationTitle = "vSongBook has been Activated!";
            notificationMsg = "God bless you! vSongBook has been activated by the Developer";
            
        }
        //ringTonePreference = settings.getString("notifications_fraud_message_ringtone", "DEFAULT_SOUND");
        intent = new Intent(this.mContext, AppSplash.class);
        PendingIntent pIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
        this.ringToneUri = Uri.parse(this.ringTonePreference);
        Notification noti = new Builder(this.mContext)
        		.setContentTitle(notificationTitle).setContentText(notificationMsg)
        		.setSound(ringToneUri)
        		.setSmallIcon(icon).setContentIntent(pIntent).build();
        Context context = mContext;
        
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        noti.flags |= 16;
        notificationManager.notify(0, noti);
        if (icontype != 1) {
            this.mContext.startActivity(this.intent);
        }
        
    }
}
