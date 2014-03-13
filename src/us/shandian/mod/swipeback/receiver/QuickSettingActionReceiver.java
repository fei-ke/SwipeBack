
package us.shandian.mod.swipeback.receiver;

import static us.shandian.mod.swipeback.receiver.QuickSettingNotification.TYPE;
import static us.shandian.mod.swipeback.receiver.QuickSettingNotification.TYPE_ADD;
import static us.shandian.mod.swipeback.receiver.QuickSettingNotification.TYPE_ADD_APP;
import static us.shandian.mod.swipeback.receiver.QuickSettingNotification.TYPE_MORE;
import static us.shandian.mod.swipeback.receiver.QuickSettingNotification.TYPE_REMOVE;
import static us.shandian.mod.swipeback.receiver.QuickSettingNotification.TYPE_REMOVE_APP;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.Toast;

import us.shandian.mod.swipeback.R;
import us.shandian.mod.swipeback.provider.PerActivitySetting;
import us.shandian.mod.swipeback.provider.SettingsProvider;

import java.lang.reflect.Method;

public class QuickSettingActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        int type = intent.getIntExtra(TYPE, 0);
        switch (type) {
            case TYPE_ADD: {
                PerActivitySetting setting = new PerActivitySetting();

                setting.setPackageName(SettingsProvider.CUR_PACKAGE_NAME);
                setting.setComponentName(SettingsProvider.CUR_COMPONENT_NAME);
                setting.setEnable(true);
                setting.saveToPreference(context);

                Toast.makeText(context, SettingsProvider.CUR_ACTIVITY_TITLE + " 成功", 0).show();
                break;
            }
            case TYPE_REMOVE: {
                PerActivitySetting setting = new PerActivitySetting();

                setting.setPackageName(SettingsProvider.CUR_PACKAGE_NAME);
                setting.setComponentName(SettingsProvider.CUR_COMPONENT_NAME);
                setting.setEnable(false);
                setting.saveToPreference(context);

                Toast.makeText(context, SettingsProvider.CUR_ACTIVITY_TITLE + " 成功", 0).show();
                break;
            }
            case TYPE_ADD_APP: {
                String appName = "";
                try {
                    PackageInfo packageInfo = context.getPackageManager().getPackageInfo(SettingsProvider.CUR_PACKAGE_NAME, 0);
                    appName = packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
                } catch (NameNotFoundException e) {
                    e.printStackTrace();
                }

                SettingsProvider.putBoolean(context, SettingsProvider.CUR_PACKAGE_NAME, SettingsProvider.SWIPEBACK_ENABLE, true);
                Toast.makeText(context, appName + "(" + SettingsProvider.CUR_PACKAGE_NAME + ") 成功", 0).show();
                break;
            }
            case TYPE_REMOVE_APP: {
                String appName = "";
                try {
                    PackageInfo packageInfo = context.getPackageManager().getPackageInfo(SettingsProvider.CUR_PACKAGE_NAME, 0);
                    appName = packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
                } catch (NameNotFoundException e) {
                    e.printStackTrace();
                }
                SettingsProvider.putBoolean(context, SettingsProvider.CUR_PACKAGE_NAME, SettingsProvider.SWIPEBACK_ENABLE, false);
                Toast.makeText(context, appName + "(" + SettingsProvider.CUR_PACKAGE_NAME + ") 成功", 0).show();
                break;
            }

            case TYPE_MORE:
                final View contentView = View.inflate(context, R.layout.layout_quick_setting_more, null);
                OnClickListener listener = new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PerActivitySetting setting = new PerActivitySetting();
                        setting.setEnable(true);
                        setting.setPackageName(SettingsProvider.CUR_PACKAGE_NAME);
                        setting.setComponentName(SettingsProvider.CUR_COMPONENT_NAME);
                        setting.setTitle(SettingsProvider.CUR_ACTIVITY_TITLE);
                        int edge = 0;

                        CheckBox checkBox = (CheckBox) contentView.findViewById(R.id.checkBox_left);
                        if (checkBox.isChecked()) {
                            edge |= SettingsProvider.SWIPEBACK_EDGE_LEFT;
                        }
                        checkBox = (CheckBox) contentView.findViewById(R.id.checkBox_right);
                        if (checkBox.isChecked()) {
                            edge |= SettingsProvider.SWIPEBACK_EDGE_RIGHT;
                        }
                        checkBox = (CheckBox) contentView.findViewById(R.id.checkBox_bottom);
                        if (checkBox.isChecked()) {
                            edge |= SettingsProvider.SWIPEBACK_EDGE_BOTTOM;
                        }
                        setting.setEdge(edge);
                        setting.saveToPreference(context);
                    }
                };
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setIcon(R.drawable.ic_launcher)
                        .setTitle(SettingsProvider.CUR_ACTIVITY_TITLE)
                        .setMessage(SettingsProvider.CUR_COMPONENT_NAME)
                        .setPositiveButton("确定", listener)
                        .setNegativeButton("取消", null)
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
