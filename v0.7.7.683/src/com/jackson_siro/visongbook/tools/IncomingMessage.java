package com.jackson_siro.visongbook.tools;

import com.jackson_siro.visongbook.AppSplash;
import com.jackson_siro.visongbook.R;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class IncomingMessage extends BroadcastReceiver
{
  
	NotificationCompat.Builder notification;
	PendingIntent pIntent;
	NotificationManager manager;
	Intent resultIntent;
	TaskStackBuilder stackBuilder;
    String notificationMsg;
    String notificationTitle;
    Uri ringToneUri;
	
  final SmsManager sms = SmsManager.getDefault();

  public void onReceive(Context paramContext, Intent paramIntent)
  {
	  SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(paramContext.getApplicationContext()).edit();
	  
	  if (!PreferenceManager.getDefaultSharedPreferences(paramContext.getApplicationContext()).getBoolean("js_vsb_is_paid", false))
		{
	     
		  Bundle localBundle = paramIntent.getExtras();
		    if (localBundle != null) {}
		    for (;;)
		    {
		      int i;
		      String sender;
		      String message;
		      try {
		        Object[] arrayOfObject = (Object[])localBundle.get("pdus");
		        i = 0;
		        if (i >= arrayOfObject.length) {
		          return;
		        }
		        SmsMessage localSmsMessage = SmsMessage.createFromPdu((byte[])arrayOfObject[i]);
		        sender = localSmsMessage.getDisplayOriginatingAddress();
		        message = localSmsMessage.getDisplayMessageBody();
		        Log.i("SmsReceiver", "senderNum: " + sender + "; message: " + message);
		        
		        if (sender.equalsIgnoreCase("MPESA")) {
			        if (message.contains("JACKSON SIRO"))
			        {
			      	  	editor.putBoolean("js_vsb_is_paid", true);
			  			editor.commit();
			  			//Toast.makeText(paramContext, "vSongBook Has been Activated!", Toast.LENGTH_LONG).show();
			  			//new CreateNotification(paramContext).createNotification(1);
			  			notificationTitle = "vSongBook has been Activated!";
			            notificationMsg = "God bless you! vSongBook has been activated by M-PESA. Enjoy";
		
						NewNotification(paramContext, notificationTitle, notificationMsg);
			        }
		         } 
		           else if (sender.equalsIgnoreCase("AirtelMoney")) {
		 	        if (message.contains("JACKSON SIRO"))  {
		 	      	  	editor.putBoolean("js_vsb_is_paid", true);
		 	  			editor.commit();
		 	  			//Toast.makeText(paramContext, "vSongBook Has been Activated!", Toast.LENGTH_LONG).show();
		 	  			notificationTitle = "vSongBook has been Activated!";
		 	            notificationMsg = "God bless you! vSongBook has been activated by AirtelMoney. Enjoy";
		
						NewNotification(paramContext, notificationTitle, notificationMsg);
		 	        }
		          } else if (sender.contains("711474787")) {
		  	        if (message.contains("Jackson Siro")) {
		  	      	  	editor.putBoolean("js_vsb_is_paid", true);
		  	  			editor.commit();
		  	  			//Toast.makeText(paramContext, "vSongBook Has been Activated!", Toast.LENGTH_LONG).show();
			  	  		notificationTitle = "vSongBook has been Activated!";
			            notificationMsg = "God bless you! vSongBook has been activated by the Developer. Enjoy";
		
						NewNotification(paramContext, notificationTitle, notificationMsg);
		  	        }
		           }  else if (sender.contains("731973180")) {
		     	        if (message.contains("Jackson Siro")) {
		      	      	  	editor.putBoolean("js_vsb_is_paid", true);
		      	  			editor.commit();
		      	  			//Toast.makeText(paramContext, "vSongBook Has been Activated!", Toast.LENGTH_LONG).show();
		      	  		notificationTitle = "vSongBook has been Activated!";
		                notificationMsg = "God bless you! vSongBook has been activated by the Developer. Enjoy";
		
						NewNotification(paramContext, notificationTitle, notificationMsg);
		      	        }
		               }
		        
		      } catch (Exception localException) {
		        Log.e("SmsReceiver", "Exception smsReceiver" + localException);
		        return;
		      }
      
     }
    }
  }
  
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public void NewNotification(Context context, String strtitle, String strmessage) {
		/*Intent intent = new Intent(context, Aaa.class);
		intent.putExtra("title", strtitle);
		intent.putExtra("text", strmessage);
		PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		// Create Notification using NotificationCompat.Builder
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
				.setSmallIcon(R.drawable.ic_launcher)
				.setTicker(strmessage)
				.setContentTitle(strtitle)
				.setContentText(strmessage)
				.setContentIntent(pIntent)
				.setAutoCancel(true);

		// Create Notification Manager
		NotificationManager notificationmanager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// Build Notification with Notification Manager
		notificationmanager.notify(0, builder.build()); */
		
		notification = new NotificationCompat.Builder(context);
		notification.setContentTitle(strtitle);
		notification.setContentText(strmessage);
		notification.setTicker(strmessage);
		notification.setSmallIcon(R.drawable.ic_notify);
		stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(AppSplash.class);
		//Intent which is opened when notification is clicked
		resultIntent = new Intent(context, AppSplash.class);
		stackBuilder.addNextIntent(resultIntent);
		pIntent =  stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setContentIntent(pIntent);
		manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(0, notification.build());
        
	}
  
}
