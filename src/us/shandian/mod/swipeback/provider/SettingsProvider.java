
package us.shandian.mod.swipeback.provider;

import android.content.Context;
import android.content.SharedPreferences;

import de.robv.android.xposed.XSharedPreferences;

public class SettingsProvider
{
    public static final String PACKAGE_NAME = "us.shandian.mod.swipeback";

    public static final String PREFS = "preferences";

    public static final String PREFIX_GLOBAL = "global";

    public static final String KEY_PACKAGE_NAME = "package_name";
    public static final String KEY_COMPONENT_NAME = "component_name";
    public static final String KEY_ACTIVITY_TITLE = "activity_title";
    public static String CUR_PACKAGE_NAME = "";
    public static String CUR_COMPONENT_NAME = "";
    public static String CUR_ACTIVITY_TITLE = "";

    public static final String SWIPEBACK_ENABLE = "swipeback_enable";
    public static final String SWIPEBACK_EDGE = "swipeback_edge";
    public static final String SWIPEBACK_EDGE_SIZE = "swipeback_edge_size";
    public static final String SWIPEBACK_RECYCLE_SURFACE = "swipeback_recycle_surface";
    public static final String SWIPEBACK_SENSITIVITY = "swipeback_sensitivity";

    public static final String QUIICK_SETTING_ENABLE = "quiick_setting_enable";
    public static final String BRIDGE_CONTENT_PROVIDER_AUTHORITIES = "us.shandian.mod.swipeback.bridge";

    public static final int SWIPEBACK_EDGE_LEFT = 1;
    public static final int SWIPEBACK_EDGE_RIGHT = 2;
    public static final int SWIPEBACK_EDGE_BOTTOM = 4;

    private static XSharedPreferences mPrefs;

    public static void initZygote() {
        mPrefs = new XSharedPreferences(PACKAGE_NAME, PREFS);
        mPrefs.makeWorldReadable();
    }

    public static int getInt(Context context, String prefix, String key, int defValue) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_WORLD_READABLE);
        if (!prefix.equals(PREFIX_GLOBAL) && !prefs.contains(prefix + ":" + key)) {
            return getInt(context, PREFIX_GLOBAL, key, defValue);
        } else {
            return prefs.getInt(prefix + ":" + key, defValue);
        }
    }

    public static int getInt(String prefix, String key, int defValue) {
        if (!prefix.equals(PREFIX_GLOBAL) && !mPrefs.contains(prefix + ":" + key)) {
            return getInt(PREFIX_GLOBAL, key, defValue);
        } else {
            return mPrefs.getInt(prefix + ":" + key, defValue);
        }
    }

    public static float getFloat(Context context, String prefix, String key, float defValue) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_WORLD_READABLE);
        if (!prefix.equals(PREFIX_GLOBAL) && !prefs.contains(prefix + ":" + key)) {
            return getFloat(context, PREFIX_GLOBAL, key, defValue);
        } else {
            return prefs.getFloat(prefix + ":" + key, defValue);
        }
    }

    public static float getFloat(String prefix, String key, float defValue) {
        if (!prefix.equals(PREFIX_GLOBAL) && !mPrefs.contains(prefix + ":" + key)) {
            return getFloat(PREFIX_GLOBAL, key, defValue);
        } else {
            return mPrefs.getFloat(prefix + ":" + key, defValue);
        }
    }

    public static boolean getBoolean(Context context, String prefix, String key, boolean defValue) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_WORLD_READABLE);
        if (!prefix.equals(PREFIX_GLOBAL) && !prefs.contains(prefix + ":" + key)) {
            return getBoolean(context, PREFIX_GLOBAL, key, defValue);
        } else {
            return prefs.getBoolean(prefix + ":" + key, defValue);
        }
    }

    public static boolean getBoolean(String prefix, String key, boolean defValue) {
        if (!prefix.equals(PREFIX_GLOBAL) && !mPrefs.contains(prefix + ":" + key)) {
            return getBoolean(PREFIX_GLOBAL, key, defValue);
        } else {
            return mPrefs.getBoolean(prefix + ":" + key, defValue);
        }
    }
    public static String getString(Context context, String prefix, String key, String defValue) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_WORLD_READABLE);
        if (!prefix.equals(PREFIX_GLOBAL) && !prefs.contains(prefix + ":" + key)) {
            return getString(context, PREFIX_GLOBAL, key, defValue);
        } else {
            return prefs.getString(prefix + ":" + key, defValue);
        }
    }
    
    public static String getString(String prefix, String key, String defValue) {
        if (!prefix.equals(PREFIX_GLOBAL) && !mPrefs.contains(prefix + ":" + key)) {
            return getString(PREFIX_GLOBAL, key, defValue);
        } else {
            return mPrefs.getString(prefix + ":" + key, defValue);
        }
    }

    public static void putInt(Context context, String prefix, String key, int value) {
        context.getSharedPreferences(PREFS, Context.MODE_WORLD_READABLE).edit()
                .putInt(prefix + ":" + key, value).commit();
    }


    public static void putFloat(Context context, String prefix, String key, float value) {
        context.getSharedPreferences(PREFS, Context.MODE_WORLD_READABLE).edit()
                .putFloat(prefix + ":" + key, value).commit();
    }

    public static void putBoolean(Context context, String prefix, String key, boolean value) {
        context.getSharedPreferences(PREFS, Context.MODE_WORLD_READABLE).edit()
                .putBoolean(prefix + ":" + key, value).commit();
    }
    
    public static void putString(Context context, String prefix, String key, String value) {
        context.getSharedPreferences(PREFS, Context.MODE_WORLD_READABLE).edit()
        .putString(key, value).commit();
    }

    public static void remove(Context context, String prefix, String key) {
        context.getSharedPreferences(PREFS, Context.MODE_WORLD_READABLE).edit()
                .remove(prefix + ":" + key).commit();
    }
    public static void reload() {
        mPrefs.reload();
    }
}
