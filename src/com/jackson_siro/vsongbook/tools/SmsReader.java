package com.jackson_siro.vsongbook.tools;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;
import java.util.Calendar;

import com.jackson_siro.vsongbook.Aaa;
import com.jackson_siro.vsongbook.R;
import com.jackson_siro.vsongbook.Settings_II;
import com.jackson_siro.vsongbook.XxxCritical;

public class SmsReader extends BroadcastReceiver
{
  
	NotificationCompat.Builder notification;
	PendingIntent pIntent;
	NotificationManager manager;
	Intent resultIntent;
	TaskStackBuilder stackBuilder;
	
  final SmsManager sms = SmsManager.getDefault();
  
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    Bundle localBundle = paramIntent.getExtras();
    if (localBundle != null) {}
    for (;;)
    {
      int i;
      String str1;
      String str2;
      try
      {
        Object[] arrayOfObject = (Object[])localBundle.get("pdus");
        i = 0;
        if (i >= arrayOfObject.length) {
          return;
        }
        SmsMessage localSmsMessage = SmsMessage.createFromPdu((byte[])arrayOfObject[i]);
        str1 = localSmsMessage.getDisplayOriginatingAddress();
        str2 = localSmsMessage.getDisplayMessageBody();
        Log.i("SmsReceiver", "senderNum: " + str1 + "; message: " + str2);
       
        if (str2.contains("jackson siro"))
        {
        	SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(paramContext.getApplicationContext()).edit();
            editor.putBoolean("vsb_is_paid", true);
			editor.commit();
			Toast.makeText(paramContext, "Gbu.vSongBook is now Premium!", 1).show();
        }
        else if (str2.contains("Jackson Siro"))
        {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(paramContext.getApplicationContext()).edit();
            editor.putBoolean("vsb_is_paid", true);
			editor.commit();
			Toast.makeText(paramContext, "Gbu.vSongBook is now Premium!", 1).show();
        }
      }
      catch (Exception localException)
      {
        Log.e("SmsReceiver", "Exception smsReceiver" + localException);
        return;
      }
      if (str2.contains("JACKSON SIRO"))
      {
    	  SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(paramContext.getApplicationContext()).edit();
          editor.putBoolean("vsb_is_paid", true);
			editor.commit();
			Toast.makeText(paramContext, "Gbu.vSongBook is now Premium!", 1).show();
        
        return;
      }
      //label478:      i++;
    }
  }
  /*
  protected void startNotification(int notify) {
		notification = new NotificationCompat.Builder(SmsReader.this);
		notification.setSmallIcon(R.drawable.ic_launcher);
		stackBuilder = TaskStackBuilder.create(Aaa.this);
			
		if (notify==1){
			notification.setContentTitle("vSongBook has Terminated!");
			notification.setContentText("Touch to learn more about this.");
			notification.setTicker("vSongBook trial mode has expired!");
			stackBuilder.addParentStack(XxxCritical.class);
			resultIntent = new Intent(Aaa.this, XxxCritical.class);
		}
		
		if (notify==2){
			notification.setContentTitle("vSongBook is now Premium");
			notification.setContentText("Touch to learn more about this.");
			notification.setTicker("vSongBook has been unlocked!");
			stackBuilder.addParentStack(Settings_II.class);
			resultIntent = new Intent(Aaa.this, Settings_II.class);
		}
		stackBuilder.addNextIntent(resultIntent);
		pIntent =  stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setContentIntent(pIntent);
		manager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(0, notification.build());			
	} */
}
