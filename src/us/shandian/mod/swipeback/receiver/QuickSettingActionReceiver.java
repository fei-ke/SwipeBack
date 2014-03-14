
package us.shandian.mod.swipeback.receiver;

import static us.shandian.mod.swipeback.receiver.QuickSettingNotification.NOTIFICATION_ID;
import static us.shandian.mod.swipeback.receiver.QuickSettingNotification.TYPE;
import static us.shandian.mod.swipeback.receiver.QuickSettingNotification.TYPE_ADD;
import static us.shandian.mod.swipeback.receiver.QuickSettingNotification.TYPE_ADD_APP;
import static us.shandian.mod.swipeback.receiver.QuickSettingNotification.TYPE_CLOSE;
import static us.shandian.mod.swipeback.receiver.QuickSettingNotification.TYPE_MORE;
import static us.shandian.mod.swipeback.receiver.QuickSettingNotification.TYPE_REMOVE;
import static us.shandian.mod.swipeback.receiver.QuickSettingNotification.TYPE_REMOVE_APP;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import us.shandian.mod.swipeback.R;
import us.shandian.mod.swipeback.provider.PerActivitySetting;
import us.shandian.mod.swipeback.provider.SettingsProvider;

import java.lang.reflect.Method;

public class QuickSettingActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        int type = intent.getIntExtra(TYPE, 0);
        final String packageName = SettingsProvider.CUR_PACKAGE_NAME;
        final String componentName = SettingsProvider.CUR_COMPONENT_NAME;
        final String activityTitle = SettingsProvider.CUR_ACTIVITY_TITLE;

        final PerActivitySetting setting = PerActivitySetting.loadFromPreference(context, packageName, componentName);
        setting.setPackageName(packageName);
        setting.setComponentName(componentName);
        setting.setTitle(activityTitle);

        String appName = "";
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            appName = packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        switch (type) {
            case TYPE_ADD: {
                setting.setEnable(true);
                setting.saveToPreference(context);

                Toast.makeText(context, context.getString(R.string.quick_setting_add_scuess, activityTitle), Toast.LENGTH_SHORT).show();
                break;
            }
            case TYPE_REMOVE: {
                setting.setEnable(false);
                setting.saveToPreference(context);
                Toast.makeText(context, context.getString(R.string.quick_setting_remove_scuess, activityTitle), Toast.LENGTH_SHORT).show();
                break;
            }
            case TYPE_ADD_APP: {
                SettingsProvider.putBoolean(context, packageName, SettingsProvider.SWIPEBACK_ENABLE, true);
                Toast.makeText(context, context.getString(R.string.quick_setting_add_app_scuess, appName), Toast.LENGTH_SHORT).show();
                break;
            }
            case TYPE_REMOVE_APP: {
                SettingsProvider.putBoolean(context, packageName, SettingsProvider.SWIPEBACK_ENABLE, false);
                Toast.makeText(context, context.getString(R.string.quick_setting_remove_app_scuess, appName), Toast.LENGTH_SHORT).show();
                break;
            }
            case TYPE_CLOSE:
                NotificationManager nm = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
                nm.cancel(NOTIFICATION_ID);
                SettingsProvider.putBoolean(context, SettingsProvider.PACKAGE_NAME, SettingsProvider.QUIICK_SETTING_ENABLE, false);
                break;
            case TYPE_MORE:
                int edge = setting.getEdge();
                if (edge == -1) {
                    edge = SettingsProvider.getInt(context, packageName, SettingsProvider.SWIPEBACK_EDGE, 0 | SettingsProvider.SWIPEBACK_EDGE_LEFT);
                }
                final View contentView = View.inflate(context, R.layout.layout_quick_setting_more, null);
                final CheckBox checkBoxLeft = (CheckBox) contentView.findViewById(R.id.checkBox_left);
                final CheckBox checkBoxRight = (CheckBox) contentView.findViewById(R.id.checkBox_right);
                final CheckBox checkBoxBottom = (CheckBox) contentView.findViewById(R.id.checkBox_bottom);

                TextView textViewAppName = (TextView) contentView.findViewById(R.id.textView_app_name);
                textViewAppName.setText(appName + "(" + packageName + ")");

                TextView textViewActivityName = (TextView) contentView.findViewById(R.id.textView_activity_name);
                textViewActivityName.setText(activityTitle + "(" + componentName + ")");

                ImageView imageViewIcon = (ImageView) contentView.findViewById(R.id.imageView_app_icon);
                if (packageInfo != null) {
                    Drawable drawable = packageInfo.applicationInfo.loadIcon(context.getPackageManager());
                    imageViewIcon.setImageDrawable(drawable);
                }

                if ((edge & SettingsProvider.SWIPEBACK_EDGE_LEFT) != 0) {
                    checkBoxLeft.setChecked(true);
                }
                if ((edge & SettingsProvider.SWIPEBACK_EDGE_RIGHT) != 0) {
                    checkBoxRight.setChecked(true);
                }
                if ((edge & SettingsProvider.SWIPEBACK_EDGE_BOTTOM) != 0) {
                    checkBoxBottom.setChecked(true);
                }

                OnClickListener listener = new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setting.setEnable(true);
                        int edge = 0;

                        if (checkBoxLeft.isChecked()) {
                            edge |= SettingsProvider.SWIPEBACK_EDGE_LEFT;
                        }
                        if (checkBoxRight.isChecked()) {
                            edge |= SettingsProvider.SWIPEBACK_EDGE_RIGHT;
                        }
                        if (checkBoxBottom.isChecked()) {
                            edge |= SettingsProvider.SWIPEBACK_EDGE_BOTTOM;
                        }
                        setting.setEdge(edge);
                        setting.saveToPreference(context);
                    }
                };
                AlertDialog dialog = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_DARK)
                        .setPositiveButton(R.string.quick_setting_ok, listener)
                        .setNegativeButton(R.string.quick_setting_cancel, null)
                        .create();
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                dialog.setView(contentView);
                dialog.show();
                break;
            default:
                break;
        }
        collapseStatusBar(context);
    }

    /**
     * Collapse status panel
     * 
     * @param context the context used to fetch status bar manager
     */
    public static void collapseStatusBar(Context context) {
        try {
            Object statusBarManager = context.getSystemService("statusbar");
            Method collapse;
            if (Build.VERSION.SDK_INT <= 16) {
                collapse = statusBarManager.getClass().getMethod("collapse");
            } else {
                collapse = statusBarManager.getClass().getMethod("collapsePanels");
            }
            collapse.invoke(statusBarManager);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }
}
