package com.hp.jlam.practice.receivers;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.hp.jlam.practice.ExtraConstants;
import com.hp.jlam.practice.R;
import com.hp.jlam.practice.Utilities;
import com.hp.jlam.practice.WeatherPrefs;
import com.hp.jlam.practice.ui.activities.MainActivity;

import java.util.Calendar;

public class UpdateWeatherNotificationReceiver extends BroadcastReceiver
{
    private static final int NOTIFICATION_ID = R.string.local_service_started;

    // does this stick around?
    @Override
    public void onReceive(Context context, Intent intent)
    {

        NotificationManager mNM = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notification;
        String title;
        String text;

        // in the future we can use an on boot alarm to setup all of this
        if(intent.getBooleanExtra(ExtraConstants.UPDATE_SUCCESS, false))
        {
            // update the notification
            //String location = intent.getStringExtra(ExtraConstants.LOCATION_LOCATION);
            //String country = intent.getStringExtra(ExtraConstants.LOCATION_COUNTRY);
            String locationFullName = intent.getStringExtra(ExtraConstants.LOCATION_FULL_NAME);
            Double currentTemp = intent.getDoubleExtra(ExtraConstants.LOCATION_CURRENT_TEMP, -9999);

            title = Utilities.GetFormattedTempString(currentTemp, WeatherPrefs.GetTempDisplayUnit(context));
            text = locationFullName;
        }
        else
        {
            title = "Error while updating weather.";
            text = "Will retry in the future";
            // we should actually not do display this but rather not replace the current display
            // however what if this is the first time?
            // or maybe offer a refresh option?
        }

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), 0);

        notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_sun)
                .setContentIntent(contentIntent)
                        //.setOngoing(true)
                .setOnlyAlertOnce(true);

        String contentText = text;

        // update notification and broadcast
        notification.setContentText(contentText)
                .setContentTitle(title)
                .setWhen(System.currentTimeMillis());

        mNM.notify(NOTIFICATION_ID, notification.build());


        SetUpdateAlarm(context);
    }

    private void SetUpdateAlarm(Context context)
    {
        Log.d("UpdateWeatherNotificationReceiver", "In SetUpdateAlarm");
        Intent updateWeatherIntent = new Intent(context, StartWeatherUpdateReceiver.class);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, updateWeatherIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        // perhaps in the future the alarm will need to pass in via the intent the location
        // to update.
        Calendar futureTime = Calendar.getInstance();
        int updateInterval =  WeatherPrefs.GetWeatherUpdateInterval(context);
        if(updateInterval == -1)
        {

            Log.d("SetUpdateAlarm", "Warning, -1 received, will not update notifications!");
            alarmManager.cancel(pendingIntent);
            // need to also clear out notifications?
        }
        else
        {
            futureTime.add(Calendar.MINUTE, updateInterval);
            // should use a TAG
            Log.d("SetUpdateAlarm", String.format("%d minute alarm set", updateInterval));
            // maybe i could do repeating? no repeating is still restricted to certain values
            alarmManager.set(AlarmManager.RTC, futureTime.getTimeInMillis(),  pendingIntent);
        }
    }
}
