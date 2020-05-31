package com.jpp.and.thirukkural;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.jpp.and.thirukkural.db.DataLoadHelper;
import com.jpp.and.thirukkural.model.Chapter;
import com.jpp.and.thirukkural.model.Couplet;
import com.jpp.and.thirukkural.model.Part;
import com.jpp.and.thirukkural.model.Section;

import java.util.Objects;
import java.util.Random;

/**
 * Implementation of App Widget functionality.
 */
public class ThirukkuralWidget extends AppWidgetProvider {

    static void setupWidget(Context context, AppWidgetManager appWidgetManager,
                            int appWidgetId, int [] allWidgetIDs) {
        Random random = new Random();
        int coupletID = random.nextInt(1330)+1;
        DataLoadHelper dlh = DataLoadHelper.getInstance();
        Couplet couplet = dlh.getCoupletById(coupletID, false);
        Chapter chapter = dlh.getChapterById(couplet.getChapterId());
        Part part = dlh.getPartById(chapter.getPartId());
        Section section = dlh.getSectionById(chapter.getSectionId());

        String coupletNum = context.getString(R.string.couplet)+"\n"+couplet.get_id();
        String detail = section.get_id()+"."+section.getTitle()+"  "+part.get_id()+"."+part.getTitle()+"  "+chapter.get_id()+"."+chapter.getTitle();

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.thirukkural_widget);
        views.setTextViewText(R.id.couplet_number, coupletNum);
        views.setTextViewText(R.id.couplet_detail_text, detail);
        views.setTextViewText(R.id.couplet_text, couplet.getCouplet());

        //Launch CoupletSwipeActivity on click on the widget
        Intent intent = new Intent(context, CoupletSwipeActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle extras = new Bundle();
        extras.putInt(Couplet.COUPLET_ID, coupletID);
        intent.putExtras(extras);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.couplet_text, pendingIntent);

        //Self refresh
        Intent selfSyncIntent = new Intent(context, ThirukkuralWidget.class);
        selfSyncIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        selfSyncIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        PendingIntent selfSyncPendingIntent = PendingIntent.getBroadcast(context, appWidgetId, selfSyncIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.refresh_btn, selfSyncPendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            setupWidget(context, appWidgetManager, appWidgetId, appWidgetIds);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -100);
        if(Objects.equals(intent.getAction(), AppWidgetManager.ACTION_APPWIDGET_UPDATE) && appWidgetId != -100){
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
            setupWidget(context, appWidgetManager, appWidgetId, appWidgetIds);
//            this.onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

