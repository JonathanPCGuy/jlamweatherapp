package com.hp.jlam.practice;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by lamjon on 6/16/2014.
 */
public class WeatherBackgroundTask extends Thread
{
    private boolean running = false;
    private static final int NOTIFICATION_ID = R.string.local_service_started;
    private NotificationManager mNM;
    private NotificationCompat.Builder notification;
    private Context context;

    // bad?!
    // what if activity goes away?
    public void SetupContextObject(Context context)
    {
        this.context = context;
        mNM = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void setRunning(boolean running) {this.running = running;}

    @Override
    public synchronized void start()
    {
        super.start();
    }


    @Override
    public void run()
    {
        int i = 0;
        while(running && i < 6)
        {


            // update notification
            if(notification == null)
            {
                PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                        new Intent(context, MainActivity.class), 0);

                this.notification = new NotificationCompat.Builder(context)

                    .setSmallIcon(R.drawable.ic_stat_sun)

                    .setContentIntent(contentIntent)
                    .setOngoing(true)
                    .setOnlyAlertOnce(true);
            }

            // update notification and broadcast
            this.notification.setContentText(Integer.toString(i))
                .setContentTitle(Integer.toString((i * 2)))
                .setWhen(System.currentTimeMillis());

            Log.d("backgroundTask", Integer.toString(i));
            mNM.notify(NOTIFICATION_ID, this.notification.build());
            i++;
            try
            {
                sleep(10000); // this in front causes the notification to not immediately appear
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        // remove notification
        mNM.cancel(NOTIFICATION_ID);
        Log.d("backgroundTask", "Thread done.");
    }

}
