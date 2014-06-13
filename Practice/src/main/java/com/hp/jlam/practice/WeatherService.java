package com.hp.jlam.practice;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by lamjon on 6/10/2014.
 * Based on service tutorial: http://developer.android.com/reference/android/app/Service.html
 */
public class WeatherService extends Service
{
    private int NOTIFICATION = R.string.local_service_started;
    private NotificationManager mNM;

    public class LocalBinder extends Binder
    {
        WeatherService getService()
        {
            return WeatherService.this;
        }
    }

    @Override
    public void onCreate()
    {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        // display the (persistent?) notification
        showNotification();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.i("WeatherService", "Received start id " + startId + ":" + intent);

        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        mNM.cancel(NOTIFICATION);

        Toast.makeText(this, R.string.local_service_stopped, Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    // show notification while service is running
    // hello world first - just get a persistent notification running before
    // we try to implement the background task
    private void showNotification()
    {
        CharSequence text = getText(R.string.local_service_started);
        CharSequence title = getText(R.string.local_service_name);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                .setContentText(text)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_stat_sun)
                .setWhen(System.currentTimeMillis());

        // if user selects notification, launch the app
        // todo: figure out how to do this
        /*PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, Ma))*/

        notification.setOngoing(true);
        mNM.notify(NOTIFICATION, notification.build());
    }
}
