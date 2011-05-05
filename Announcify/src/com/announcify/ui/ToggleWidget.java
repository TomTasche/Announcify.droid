package com.announcify.ui;

import org.mailboxer.saymyname.R;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.announcify.api.background.sql.model.PluginModel;

public class ToggleWidget extends AppWidgetProvider {
    public static String WIDGET_ACTION = "com.announcify.WIDGET_ACTION";

    private RemoteViews remoteViews;

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

        final Intent active = new Intent(WIDGET_ACTION);

        final PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
        remoteViews.setOnClickPendingIntent(R.id.widget_button, actionPendingIntent);

        if (isActive(context)) {
            remoteViews.setImageViewResource(R.id.widget_button, R.drawable.launcher_icon);
        } else {
            remoteViews.setImageViewResource(R.id.widget_button, R.drawable.widget_off);
        }

        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

        // v1.5 fix that doesn't call onDelete Action
        final String action = intent.getAction();
        if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {
            final int appWidgetId = intent.getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

            if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                onDeleted(context, new int[] { appWidgetId });
            }
        } else if (intent.getAction().equals(WIDGET_ACTION)) {
            final boolean activated = isActive(context);

            if (!activated) {
                remoteViews.setImageViewResource(R.id.widget_button, R.drawable.launcher_icon);
                Toast.makeText(context, "Announcify activated", Toast.LENGTH_SHORT).show();
            } else {
                remoteViews.setImageViewResource(R.id.widget_button, R.drawable.widget_off);
                Toast.makeText(context, "Announcify deactivated", Toast.LENGTH_SHORT).show();
            }

            toggleAnnouncify(context);

            final AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
            final ComponentName componentName = new ComponentName(context, ToggleWidget.class);
            widgetManager.updateAppWidget(componentName, remoteViews);
        }

        super.onReceive(context, intent);
    }

    private void toggleAnnouncify(final Context context) {
        final PluginModel model = new PluginModel(context);
        model.togglePlugin(model.getId("Announcify++"));
    }

    private boolean isActive(final Context context) {
        final PluginModel model = new PluginModel(context);
        return model.getActive(model.getId("Announcify++"));
    }
}
