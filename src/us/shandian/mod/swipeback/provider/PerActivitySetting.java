
package us.shandian.mod.swipeback.provider;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.Serializable;

import de.robv.android.xposed.XSharedPreferences;

public class PerActivitySetting implements Serializable {
    boolean isEnable = true;
    String packageName = "";
    String componentName = "";
    String title = "";
    int edge = -1;

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getEdge() {
        return edge;
    }

    public void setEdge(int edge) {
        this.edge = edge;
    }

    @Override
    public String toString() {
        return componentName + "|" + isEnable + "|" + title + "|" + edge;
    }

    public static PerActivitySetting loadFromPreference(String packageName,
            String componentName) {
        XSharedPreferences prefs = new XSharedPreferences(SettingsProvider.PACKAGE_NAME,
                packageName);
        prefs.makeWorldReadable();
        String content = prefs.getString(componentName, null);
        return loadFromText(content);

    }

    public static PerActivitySetting loadFromText(String value) {
        if (value != null) {
            PerActivitySetting setting = new PerActivitySetting();
            String[] values = value.split("\\|");
            setting.componentName = values[0];
            setting.isEnable = Boolean.getBoolean(values[1]);
            setting.title = values[2];
            setting.edge = Integer.valueOf(values[3]);
            return setting;
        }
        return null;
    }

    public boolean saveToPreference(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(packageName,
                Context.MODE_WORLD_READABLE);
        if (this.isEnable && edge == -1) {
            return preferences.edit().remove(packageName).commit();
        }
        return preferences.edit().putString(componentName, this.toString()).commit();
    }
}
