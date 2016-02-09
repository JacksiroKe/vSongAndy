package com.jackson_siro.vsongbook.tools ;

import com.jackson_siro.vsongbook.R;

import android.app.Activity;
import android .app .Notification ;
import android .app .NotificationManager ;
import android .app .PendingIntent ;
import android .content .Intent ;
import android .os. Bundle;
import android .view.Menu;
import android .view.MenuItem ;
import android .view.View ;
import android .widget.Button;

public class Notifycation extends Activity {
	Button b1 ;
	/*
	@Override
	protected void onCreate (Bundle savedInstanceState ) {
	super .onCreate( savedInstanceState);
	setContentView (R .layout. activity_main );
	b1 =( Button)findViewById ( R.id .button);
	b1 .setOnClickListener (new View .OnClickListener () {
	@Override
	public void onClick (View v ) {
		Notify ("You've received new message" );
	}
	});
	}
	
	private void Notify ( String notificationTitle, String notificationMessage){
		NotificationManager notificationManager = (NotificationManager )getSystemService (NOTIFICATION_SERVICE );
		@SuppressWarnings ("deprecation" )
		Notification notification = new Notification(R.drawable.ic_notify, "New Message" ,
		System.currentTimeMillis ());
		Intent notificationIntent = new Intent(this , NotificationView .class);
		PendingIntent pendingIntent =
		PendingIntent.getActivity (this , 0, notificationIntent , 0);
		notification .setLatestEventInfo ( Notifycation.this ,
		notificationTitle , notificationMessage , pendingIntent );
		notificationManager. notify( 9999, notification );
	}
	
	/*
	@Override
	public boolean onCreateOptionsMenu ( Menu menu ) {
	
		getMenuInflater (). inflate (R .menu.menu_main , menu );
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected (MenuItem item )
	{
	
		int id = item . getItemId();
		if (id == R.id.action_settings ) {
		return true ;
	}
	return super.onOptionsItemSelected( item );
	}*/
	
}
