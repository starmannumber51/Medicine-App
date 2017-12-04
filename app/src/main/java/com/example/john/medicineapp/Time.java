package com.example.john.medicineapp;

import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Time extends Service {

    MedicineList myList;

    public Time() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        scheduler(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    public void scheduler(Intent intent){


        boolean notify = intent.getBooleanExtra("notify1", true);
        boolean notify2 = intent.getBooleanExtra("notify2", true);

        myList = (MedicineList) intent.getSerializableExtra("List");

        NotificationManager mgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        NotificationCompat.Builder remind = new NotificationCompat.Builder(this);
        remind.setSmallIcon(R.mipmap.ic_launcher);


        int hour;
        int minute;
        int mNotificationID = 001;
        boolean sent = false;
        ArrayList<String> taken = new ArrayList<String>(0);

        Calendar rightNow = Calendar.getInstance();
        hour = rightNow.get(Calendar.HOUR_OF_DAY);
        minute = rightNow.get(Calendar.MINUTE);
        ResultReceiver receiver = intent.getParcelableExtra("listener");
        Bundle bundle = new Bundle();
        Log.d("Notify1", String.valueOf(notify));
        Log.d("Notify2", String.valueOf(notify2));
        if (!notify && !notify2) {
            for (int i = 0; i < myList.Medlist.size(); i++) {
                if (hour == myList.Medlist.get(i).getHour()) {
                    notify2 = true;
                    notify = true;

                    if (myList.Medlist.get(i).getNumberOfDoses() > 0) {
                        myList.Medlist.get(i).setNumberOfDoses(myList.Medlist.get(i).getNumberOfDoses() - 1);
                        mBuilder.setContentTitle("Time to take " + myList.Medlist.get(i).getName());
                        mBuilder.setContentText("Time to take your " + myList.Medlist.get(i).getName() + "! You have " + myList.Medlist.get(i).getNumberOfDoses()
                                + " Doses left!");
                        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
                        mBuilder.setContentIntent(pi);
                        mgr.notify(mNotificationID, mBuilder.build());
                        mNotificationID++;
                        taken.add(myList.Medlist.get(i).getName());
                        sent = true;
                    }
                    if (myList.Medlist.get(i).getNumberOfDoses() < 6) {
                        remind.setContentTitle("You are low on " + myList.Medlist.get(i).getName() + ".");
                        remind.setContentText("You are low on " + myList.Medlist.get(i).getName() + "! please remember to get some more.");
                        PendingIntent hi = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
                        remind.setContentIntent(hi);
                        mgr.notify(mNotificationID, remind.build());
                        mNotificationID++;
                    }

                }

            }

        }
        if (minute == 0) notify = false;
        if (minute == 59) notify2 = false;
        Log.d("Notify1", String.valueOf(notify));
        Log.d("Notify2", String.valueOf(notify2));
        bundle.putStringArrayList("taken", taken);
        bundle.putBoolean("notify", notify);
        bundle.putBoolean("notify2", notify2);
        if (sent) receiver.send(Activity.RESULT_OK, bundle);
        else receiver.send(Activity.RESULT_CANCELED, bundle);
    }

}
