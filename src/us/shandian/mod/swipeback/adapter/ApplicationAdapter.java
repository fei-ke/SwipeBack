
package us.shandian.mod.swipeback.adapter;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import us.shandian.mod.swipeback.R;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class ApplicationAdapter extends ArrayAdapter<ApplicationInfo>
{
    private List<ApplicationInfo> mAppsList = null;
    private HashMap<String, View> mViews = new HashMap<String, View>();
    private Context mContext;
    private PackageManager mPackageManager;

    private ArrayList<ApplicationInfo> filteredAppList = new ArrayList<ApplicationInfo>();
    private Filter filter;

    public ApplicationAdapter(Context context, int textViewResourceId,
            List<ApplicationInfo> appsList) {
        super(context, textViewResourceId, new ArrayList<ApplicationInfo>(appsList));
        this.mContext = context;
        this.mAppsList = appsList;
        filteredAppList.addAll(appsList);
        filter = new AppListFilter(this);

        mPackageManager = mContext.getPackageManager();

        for (int i = 0; i < mAppsList.size(); i++) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.item_listview_perapp, null);
            ApplicationInfo data = mAppsList.get(i);
            if (null != data) {
                TextView appName = (TextView) view.findViewById(R.id.per_app_name);
                ImageView iconview = (ImageView) view.findViewById(R.id.per_app_icon);

                if (i > 0) {
                    iconview.setImageDrawable(data.loadIcon(mPackageManager));
                }
                appName.setText(data.name);
            }
            mViews.put(data.packageName, view);
        }
    }

    @Override
    public int getCount() {
        return ((null != filteredAppList) ? filteredAppList.size() : 0);
    }

    @Override
    public ApplicationInfo getItem(int position) {
        return ((null != filteredAppList) ? filteredAppList.get(position) : null);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        String key = getItem(position).packageName;
        if (null != mViews.get(key)) {
            view = mViews.get(key);
        }

        return view;
    }

    public Filter getFilter()
    {
        return filter;
    }

    private class AppListFilter extends Filter
    {

        private ApplicationAdapter adaptor;

        AppListFilter(ApplicationAdapter adaptor)
        {
            super();
            this.adaptor = adaptor;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            // NOTE: this function is *always* called from a background thread,
            // and
            // not the UI thread.

            ArrayList<ApplicationInfo> items = new ArrayList<ApplicationInfo>();
            synchronized (this)
            {
                items.addAll(mAppsList);
            }

            FilterResults result = new FilterResults();
            if (constraint != null && constraint.length() > 0)
            {
                Pattern regexp = Pattern.compile(constraint.toString(), Pattern.LITERAL | Pattern.CASE_INSENSITIVE);
                for (Iterator<ApplicationInfo> i = items.iterator(); i.hasNext();)
                {
                    ApplicationInfo app = i.next();
                    if (!regexp.matcher(app.name == null ? "" : app.name).find() && !regexp.matcher(app.packageName).find())
                    {
                        i.remove();
                    }
                }
            }

            result.values = items;
            result.count = items.size();

            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            // NOTE: this function is *always* called from the UI thread.
            filteredAppList = (ArrayList<ApplicationInfo>) results.values;
            adaptor.notifyDataSetChanged();
            adaptor.clear();
            for (int i = 0, l = filteredAppList.size(); i < l; i++)
            {
                adaptor.add(filteredAppList.get(i));
            }
            adaptor.notifyDataSetInvalidated();
        }
    }
}
