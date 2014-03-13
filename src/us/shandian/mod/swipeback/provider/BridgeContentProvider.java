
package us.shandian.mod.swipeback.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

/**
 * 主要提供当前正在前台的应用PackageName和Activity ComponentName
 * 
 * @author fei-ke
 */
public class BridgeContentProvider extends ContentProvider {

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SettingsProvider.CUR_PACKAGE_NAME=values.getAsString(SettingsProvider.KEY_PACKAGE_NAME);
        SettingsProvider.CUR_COMPONENT_NAME=values.getAsString(SettingsProvider.KEY_COMPONENT_NAME);
        SettingsProvider.CUR_ACTIVITY_TITLE=values.getAsString(SettingsProvider.KEY_ACTIVITY_TITLE);
        return 0;
    }

}
