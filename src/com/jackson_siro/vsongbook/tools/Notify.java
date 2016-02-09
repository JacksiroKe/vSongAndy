package com.jackson_siro.vsongbook.tools;

import com.jackson_siro.vsongbook.*;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;

public class Notify
{
  int icon;
  Intent intent;
  Context mContext;
  String notificationMsg;
  String notificationTitle;
  String ringTonePreference;
  Uri ringToneUri;
  
  NotificationCompat.Builder notification;
	PendingIntent pIntent;
	NotificationManager manager;
	Intent resultIntent;
	TaskStackBuilder stackBuilder;
  
	
	/*
  public Notify(Context paramContext)
  {
    this.mContext = paramContext;
  }
  //new CreateNotification(paramContext).createNotification(1);
  public void createNotification(int paramInt)
  {
    int i;
    SharedPreferences localSharedPreferences;
    String str;
    if (paramInt == 1)
    {
      i = paramInt;
      
      this.icon = R.drawable.ic_notify;
      this.notificationTitle = "VALID MPesa Received.";
      this.notificationMsg = "Thank you note sent.";
      str = "notification";
      this.intent = new Intent(this.mContext, Settings_III.class);
      Intent localIntent3 = this.intent;
      
      //localIntent3.putExtra("contacts_name", paramString1);
      Intent localIntent4 = this.intent;
      
      //localIntent4.putExtra("contacts_number", paramString2);
      //this.ringTonePreference = localSharedPreferences.getString("notifications_new_message_ringtone", "DEFAULT_SOUND");
    }
    for (;;)
    {
      this.intent.putExtra("key_ID", str);
      this.intent.setFlags(67108864);
      this.intent.setFlags(268435456);
      PendingIntent localPendingIntent = PendingIntent.getActivity(this.mContext, i, this.intent, 1073741824);
      this.ringToneUri = Uri.parse(this.ringTonePreference);
      Notification localNotification = new NotificationCompat.Builder(this.mContext).setContentTitle(this.notificationTitle).setContentText(this.notificationMsg).setSound(this.ringToneUri).setSmallIcon(this.icon).setContentIntent(localPendingIntent).build();
      Context localContext = this.mContext;
      NotificationManager localNotificationManager = (NotificationManager)localContext.getSystemService("notification");
      localNotification.flags = (0x10 | localNotification.flags);
      localNotificationManager.notify(paramInt, localNotification);
      if (paramInt != 1) {
        this.mContext.startActivity(this.intent);
      }
      return;
      i = paramInt;
      break;
      label338:
      this.icon = 2130837596;
      this.notificationTitle = "WARNING! Pesa con alert.";
      this.notificationMsg = ("Fake PESA message from");
      this.intent = new Intent(this.mContext, XxxCritical.class);
      Intent localIntent1 = this.intent;
      
      //localIntent1.putExtra("contacts_number", paramString2);
      Intent localIntent2 = this.intent;
      
      localIntent2.putExtra("contacts_note", "Sends fake mpesa messages".toUpperCase());
      this.ringTonePreference = localSharedPreferences.getString("notifications_fraud_message_ringtone", "DEFAULT_SOUND");
      str = "warning";
    }
  }
  */
}
