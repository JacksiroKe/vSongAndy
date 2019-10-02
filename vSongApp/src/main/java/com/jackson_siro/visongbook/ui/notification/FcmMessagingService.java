package com.jackson_siro.visongbook.ui.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import com.jackson_siro.visongbook.R;
import com.jackson_siro.visongbook.ui.DdHomeView;

public class FcmMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String title = remoteMessage.getData().get("title");
        String photos = remoteMessage.getData().get("photos");
        Intent intent = new Intent(this, DdHomeView.class);
        intent.getIntExtra(title, 0);
        intent.getIntExtra(photos, 0);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.activity_notification);
        remoteViews.setImageViewUri(R.id.image_view_notif_item, Uri.parse(photos));
        remoteViews.setTextViewText(R.id.text_View_title_notif_item, title);

        final NotificationCompat.Builder compat = new NotificationCompat.Builder(this);
        compat.setContentTitle(title);
        compat.setCustomContentView(remoteViews);
        compat.setSmallIcon(R.mipmap.ic_launcher_round);
        compat.setAutoCancel(true);
        compat.setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        ImageRequest imageRequest = new ImageRequest(photos, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                compat.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(response));
                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm.notify(0, compat.build());
            }
        }, 0, 0, null, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MySingleton.getmInstance(this).addToRequest(imageRequest);
        manager.notify(0, compat.build());

    }
}
