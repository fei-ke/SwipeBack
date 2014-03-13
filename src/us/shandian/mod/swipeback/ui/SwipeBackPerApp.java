
package us.shandian.mod.swipeback.ui;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView;
import android.widget.TextView;

import us.shandian.mod.swipeback.R;
import us.shandian.mod.swipeback.adapter.ApplicationAdapter;
import us.shandian.mod.swipeback.provider.SettingsProvider;
import us.shandian.mod.swipeback.receiver.QuickSettingNotification;

import java.util.ArrayList;
import java.util.List;

public class SwipeBackPerApp extends ListActivity implements OnItemClickListener
{
    private Context mContext;
    private ApplicationAdapter mAdapter;
    private ProgressDialog mDialog;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg)
        {
            if (msg.what == 0) {
                mAdapter = new ApplicationAdapter(mContext, 0, (ArrayList<ApplicationInfo>) msg.obj);
                setListAdapter(mAdapter);
                mDialog.dismiss();
            }
        }
    };

    protected String nameFilter = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Listener
        getListView().setOnItemClickListener(this);

        // Init the list
        mContext = this;
        mDialog = new ProgressDialog(this);
        mDialog.setMessage(getString(R.string.please_wait));
        mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDialog.setCancelable(false);
        mDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<ApplicationInfo> applist = getAppList();
                Message msg = mHandler.obtainMessage(0, applist);
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.perapp, menu);
        // 搜索
        final SearchView searchView = (SearchView) menu.getItem(0).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                nameFilter = query;
                mAdapter.getFilter().filter(nameFilter);
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                nameFilter = newText;
                mAdapter.getFilter().filter(nameFilter);
                return false;
            }

        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.about) {
            Intent i = new Intent();
            i.setAction(Intent.ACTION_DEFAULT);
            i.setClass(this, SwipeBackAbout.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.quick_setting) {
            QuickSettingNotification.showAddNotificationView(getApplicationContext());
        }
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<ApplicationInfo> getAppList() {

        List<ApplicationInfo> list = mContext.getPackageManager().getInstalledApplications(
                PackageManager.GET_GIDS);
        ArrayList<ApplicationInfo> applist = new ArrayList<ApplicationInfo>();
        mDialog.setMax(list.size());
        PackageManager pm = mContext.getPackageManager();
        int i = 1;
        for (ApplicationInfo info : list) {
            mDialog.setProgress(i++);
            try {
                if (null != pm.getLaunchIntentForPackage(info.packageName) && !info.packageName.equals(mContext.getApplicationInfo().packageName)) {
                    info.name = info.loadLabel(pm).toString();
                    applist.add(info);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return applist;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Bundle bundle = new Bundle();
        if (position == 0) {
            bundle.putString("us.shandian.mod.swipeback.PREFIX", SettingsProvider.PREFIX_GLOBAL);
        } else {
            bundle.putString("us.shandian.mod.swipeback.PREFIX",
                    mAdapter.getItem(position).packageName);
        }
        bundle.putString("us.shandian.mod.swipeback.TITLE",
                ((TextView) view.findViewById(R.id.per_app_name)).getText().toString());

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DEFAULT);
        intent.setClass(mContext, SwipeBackSettings.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
