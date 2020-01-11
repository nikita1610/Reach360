package com.reach360.reach360;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.annotation.SuppressLint;

import android.app.Dialog;

import android.content.Intent;

import android.graphics.drawable.Drawable;

import android.net.Uri;

import android.os.Build;


import android.provider.Settings;

import android.support.v7.app.ActionBarActivity;

import android.view.Menu;

import android.view.MenuItem;

import android.view.View;

import android.view.View.OnClickListener;

import android.view.Window;

import android.widget.Button;

import android.widget.GridView;

import android.widget.ProgressBar;

import android.widget.SeekBar;

import android.widget.SeekBar.OnSeekBarChangeListener;

import android.widget.TextView;

import android.widget.Toast;

import com.google.android.gms.ads.AdListener;

import com.google.android.gms.ads.AdRequest;

import com.google.android.gms.ads.AdView;

import com.google.android.gms.ads.InterstitialAd;

import static android.R.attr.checked;
import static android.service.notification.Condition.SCHEME;

public class ShakeActivity extends ActionBarActivity {

    private InterstitialAd interstitial;

    GridView gridApps;

    ProgressBar pBar;

    Prefs prefs;

    public static boolean isServiceRunning = false;

    private static final String SCHEME = "package";

    private static final String APP_PKG_NAME_21 = "com.android.settings.ApplicationPkgName";

    private static final String APP_PKG_NAME_22 = "pkg";

    private static final String APP_DETAILS_PACKAGE_NAME = "com.android.settings";

    private static final String APP_DETAILS_CLASS_NAME = "com.android.settings.InstalledAppDetails";

    @Override

    protected void onCreate(Bundle savedInstanceState) {

// TODO Auto-generated method stub

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shake);

        AdView adView = (AdView) this.findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();

        adView.loadAd(adRequest);

        interstitial = new InterstitialAd(this);

        interstitial.setAdUnitId("AD_UNIT_ID");

        runOnUiThread(new Runnable() {

            @Override

            public void run() {

// TODO Auto-generated method stub

                AdRequest interstitialAdRequest = new AdRequest.Builder()

                        .build();

                interstitial.loadAd(interstitialAdRequest);

            }

        });

        interstitial.setAdListener(new AdListener() {

            public void onAdLoaded() {

                displayInterstitial();

            }

        });

        gridApps = (GridView) findViewById(R.id.app_list);

        pBar = (ProgressBar) findViewById(R.id.pBarLoadApps);

        prefs = new Prefs(this);

        new LoadApps(this, gridApps, pBar).execute();

    }

    public void displayInterstitial() {

        if (interstitial.isLoaded()) {

            interstitial.show();

        }

    }

    @Override

    public void onBackPressed() {

// TODO Auto-generated method stub

        super.onBackPressed();

    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

// Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.main, menu);

        return true;

    }

    @SuppressLint("InlinedApi")

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {

// Handle action bar item clicks here. The action bar will

// automatically handle clicks on the Home/Up button, so long

// as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        if (id == R.id.action_sensitivity) {

            sensitivityDialog();

            return true;

        }

        if (id == R.id.action_stop) {

            if (isServiceRunning == true) {

                stopService(new Intent(this, ShakeService.class));

                finish();

            } else {

                finish();

            }

            return true;

        }

        if (id == R.id.action_notification) {

            Intent intent = new Intent();

            final int apiLevel = Build.VERSION.SDK_INT;

            if (apiLevel >= 9) { // above 2.3

                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);

                Uri uri = Uri.fromParts(SCHEME, getPackageName(), null);

                intent.setData(uri);

            } else { // below 2.3

                final String appPkgName = (apiLevel == 8 ? APP_PKG_NAME_22

                        : APP_PKG_NAME_21);

                intent.setAction(Intent.ACTION_VIEW);

                intent.setClassName(APP_DETAILS_PACKAGE_NAME,

                        APP_DETAILS_CLASS_NAME);

                intent.putExtra(appPkgName, getPackageName());

            }

            startActivity(intent);

            Toast.makeText(this,

                    "Check 2 times Show notifications and keep this checked",

                    Toast.LENGTH_LONG).show();

        }

        if (id == R.id.action_rate) {

            Intent myIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id=&#8221"
                    + getPackageName().toString()));

            startActivity(myIntent);

        }

        return super.onOptionsItemSelected(item);

    }

    public void sensitivityDialog() {

        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.sensitivity_dialog);

        final TextView tv = (TextView) dialog.findViewById(R.id.tvSensitivity);

        final SeekBar sb = (SeekBar) dialog.findViewById(R.id.sbSensitivity);

        Button bDone = (Button) dialog.findViewById(R.id.bDone);

        tv.setText("Sensitivity – "+ prefs.getStringPrefs(Prefs.sensitivity, "8"));

        sb.setProgress(Integer.parseInt(prefs.getStringPrefs(Prefs.sensitivity,"8")));

        sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override

            public void onStopTrackingTouch(SeekBar seekBar) {

// TODO Auto-generated method stub

                if (seekBar.getProgress() < 5) {

                    seekBar.setProgress(5);

                    tv.setText("Sensitivity – " + 5);

                }

                prefs.setStringPrefs(Prefs.sensitivity,

                        " " + seekBar.getProgress());

                Drawable thumb = getResources().getDrawable(R.drawable.thumb);

                sb.setThumb(thumb);

            }

            @Override

            public void onStartTrackingTouch(SeekBar seekBar) {

// TODO Auto-generated method stub

                Drawable thumb = getResources().getDrawable(

                        R.drawable.thumb);

                sb.setThumb(thumb);

            }

            @Override

            public void onProgressChanged(SeekBar seekBar, int progress,

                                          boolean fromUser) {

// TODO Auto-generated method stub

                tv.setText("Sensitivity – " + progress);

            }

        });

        bDone.setOnClickListener(new OnClickListener() {

            @Override

            public void onClick(View v) {

// TODO Auto-generated method stub

                dialog.cancel();

            }

        });

        dialog.show();

    }

}