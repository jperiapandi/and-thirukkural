package com.jpp.and.thirukkural.worker;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.jpp.and.thirukkural.Constants;
import com.jpp.and.thirukkural.CoupletSwipeActivity;
import com.jpp.and.thirukkural.R;
import com.jpp.and.thirukkural.db.DataLoadHelper;
import com.jpp.and.thirukkural.model.Chapter;
import com.jpp.and.thirukkural.model.Couplet;
import com.jpp.and.thirukkural.model.Part;
import com.jpp.and.thirukkural.model.Section;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class PeriodicNotificationWorker extends Worker {
    private static String TAG = "PeriodicNotificationWorker";

    public PeriodicNotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        int h = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        //Display Notifications by 6 AM and 7 PM
        if (h == 5 || h == 18) {
            long time = new Date().getTime();
            Context context = getApplicationContext();

            Random random = new Random();
            int coupletID = random.nextInt(1330) + 1;
            DataLoadHelper dlh = DataLoadHelper.getInstance();
            Couplet couplet = dlh.getCoupletById(coupletID, false);
            Chapter chapter = dlh.getChapterById(couplet.getChapterId());
            Part part = dlh.getPartById(chapter.getPartId());
            Section section = dlh.getSectionById(chapter.getSectionId());

            //Care an intent to open Couplet Activity
            Intent intent = new Intent(context, CoupletSwipeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            Bundle extras = new Bundle();
            extras.putInt(Couplet.COUPLET_ID, coupletID);
            extras.putBoolean(Constants.IS_FROM_NOTIFICATION, true);
            intent.putExtras(extras);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

            String hDisplay = "6:00 AM";
            if (h == 18) {
                hDisplay = "7:00 PM";
            }
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, Constants.CHANNEL_ID)
                    .setSmallIcon(R.drawable.valluvar_icon)
                    .setContentTitle(context.getString(R.string.couplet) + " - " + coupletID + " " + hDisplay)
                    .setContentText(couplet.getCouplet())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManagerCompat notiMgr = NotificationManagerCompat.from(context);

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return Result.failure();
            }
            notiMgr.notify(Constants.COUPLET_NOTIFICATION_ID, notificationBuilder.build());
            return Result.success();
        } else {
            return Result.failure();
        }
    }
}
