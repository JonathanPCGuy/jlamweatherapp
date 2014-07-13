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

import com.hp.jlam.practice.ui.activities.MainActivity;

/**
 * Created by lamjon on 6/10/2014.
 * Based on service tutorial: http://developer.android.com/reference/android/app/Service.html
 */
public class WeatherService extends Service
{
    private int NOTIFICATION = R.string.local_service_started;
    private NotificationManager mNM;

    // should be a condition variable, change it later
    private Boolean mQuit = false;

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
        //showNotification();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.i("WeatherService", "Received start id " + startId + ":" + intent);
        // to do: insert timers to update weather info
        // or should that go in the main app?
        mQuit = false;

        // this thread will run for a while then quit
        // will simulate triggering a background task from the app
        // need to figure out how to push updates to the notification bar

        backgroundTask = new WeatherBackgroundTask();
        backgroundTask.setRunning(true);
        backgroundTask.SetupContextObject(this); // is this really bad?!
        backgroundTask.start();

        return START_STICKY;
    }

    private WeatherBackgroundTask backgroundTask;

    @Override
    public void onDestroy()
    {
        //mNM.cancel(NOTIFICATION);
        if(backgroundTask.isAlive())
        {
            backgroundTask.setRunning(false);
            try
            {
                backgroundTask.join();
            }
            catch(InterruptedException e)
            {
                Log.d("onDestroy WeatherService", e.getMessage());
            }
        }
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

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
            .setContentText(text)
            .setContentTitle(title)
            .setSmallIcon(R.drawable.ic_stat_sun)
            .setWhen(System.currentTimeMillis())
            .setContentIntent(contentIntent);

        // ok, it looks like it's better to create an alarm, then trigger a task every x seconds
        // instead of creating an actual ongoing background task that runs constantly
        // still, let's go ahead and do one temporarily
        // so we can learn



        notification.setOngoing(true);
        mNM.notify(NOTIFICATION, notification.build());
    }
}
