package com.reach360.reach360;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class LoadApps extends AsyncTask<Void, Void, Void> {

    Context context;
    GridView gridApps;
    ProgressBar pBar;
    Prefs prefs;

    private PackageManager manager;
    private List<CustomComparator> apps;

    public LoadApps(Context context, GridView gridApps, ProgressBar pBar) {
        this.context = context;
        this.gridApps = gridApps;
        this.pBar = pBar;
        prefs = new Prefs(context);
    }

    @Override
    protected Void doInBackground(Void... params) {
// TODO Auto-generated method stub
        manager = context.getPackageManager();
        apps = new ArrayList<CustomComparator>();

        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> availableActivities = manager.queryIntentActivities(
                i, 0);
        for (ResolveInfo ri : availableActivities) {
            CustomComparator app = new CustomComparator();
            app.setLabel(ri.loadLabel(manager).toString());
            app.setName(ri.activityInfo.packageName);
            app.setIcon(ri.activityInfo.loadIcon(manager));
            apps.add(app);
        }

        Collections.sort(apps);
        return null;
    }

    @SuppressLint("InflateParams")
    @Override
    protected void onPostExecute(Void result) {
// TODO Auto-generated method stub
        super.onPostExecute(result);

        Toast.makeText(context, "Select an app to open on shake.",
                Toast.LENGTH_SHORT).show();

        ArrayAdapter<CustomComparator> adapter = new ArrayAdapter<CustomComparator>(
                context, R.layout.app_list_item, apps) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = ((Activity) context).getLayoutInflater()
                            .inflate(R.layout.app_list_item, null);
                }

                ImageView appIcon = (ImageView) convertView
                        .findViewById(R.id.item_app_icon);
                appIcon.setImageDrawable(apps.get(position).getIcon());

                TextView appLabel = (TextView) convertView
                        .findViewById(R.id.item_app_label);
                appLabel.setText(apps.get(position).getLabel());

                return convertView;
            }
        };

        gridApps.setAdapter(adapter);
        gridApps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int position,
                                    long id) {
                prefs.setStringPrefs(Prefs.appPackage, apps.get(position)
                        .getName());
                context.startService(new Intent(context, ShakeService.class));
                Toast.makeText(context, "Service Started", Toast.LENGTH_LONG)
                        .show();
            }
        });
        pBar.setVisibility(View.GONE);
    }

}