package com.jackson_siro.vsongbook.tools;

import com.jackson_siro.vsongbook.*;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NotifyManager extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
}
/*	
public void createNotification(View view) {
	
	//Intent intent = new Intent( this, NotificationReceiverActivity.class );
	//PendingIntent pIntent = PendingIntent.getActivity( this,
	//(int ) System.currentTimeMillis(), intent, 0);
	
	Notification noti = new Notification.Builder( this )
	.setContentTitle( "New mail from " + "test@gmail.com" )
	.setContentText( "Subject" ).setSmallIcon(R.drawable.ic_notify)
	        .setContentIntent(pIntent)
	        .addAction(R.drawable.ic_notify, "Call" , pIntent)
	.addAction(R.drawable.ic_notify, "More" , pIntent)
	.addAction(R.drawable.ic_notify, "And more" , pIntent)
	.build();
	    NotificationManager notificationManager =
	(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	// hide the notification after its selected
	noti.flags |= Notification.FLAG_AUTO_CANCEL;
	notificationManager.notify(0, noti);
	}
} */
}
