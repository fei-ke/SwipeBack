
package us.shandian.mod.swipeback.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import us.shandian.mod.swipeback.R;

public class QuickSettingNotification {
    private static final String ACTION = "us.shandian.mod.swipeback.QuickSettingActionReceiver";
    public static final String TYPE = "type";
    public static final int TYPE_ADD = 1;
    public static final int TYPE_REMOVE = 2;
    public static final int TYPE_ADD_APP = 3;
    public static final int TYPE_REMOVE_APP = 4;
    public static final int TYPE_MORE = 5;

    public static void showAddNotificationView(Context context)
    {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.layout_quick_setting);

        Intent intent = new Intent(ACTION);
        intent.putExtra(TYPE, TYPE_ADD);
        PendingIntent intentAdd = PendingIntent.getBroadcast(context, TYPE_ADD, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.button_add, intentAdd);

        intent = new Intent(ACTION);
        intent.putExtra(TYPE, TYPE_REMOVE);
        PendingIntent intentRemove = PendingIntent.getBroadcast(context, TYPE_REMOVE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.button_remove, intentRemove);

        intent = new Intent(ACTION);
        intent.putExtra(TYPE, TYPE_ADD_APP);
        PendingIntent intentAddApp = PendingIntent.getBroadcast(context, TYPE_ADD_APP, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.button_add_app, intentAddApp);

        intent = new Intent(ACTION);
        intent.putExtra(TYPE, TYPE_REMOVE_APP);
        PendingIntent intentRemoveApp = PendingIntent.getBroadcast(context, TYPE_REMOVE_APP, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.button_remove_app, intentRemoveApp);

        intent = new Intent(ACTION);
        intent.putExtra(TYPE, TYPE_MORE);
        PendingIntent intentMore = PendingIntent.getBroadcast(context, TYPE_MORE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.button_more, intentMore);

        Notification notification = new Notification.Builder(context)
                .setTicker("在通知栏进行操作")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContent(views)
                .build();
        NotificationManager nm = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
        nm.notify(0x11, notification);
    }
}
