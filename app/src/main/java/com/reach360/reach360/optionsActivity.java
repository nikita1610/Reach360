package com.reach360.reach360;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.reach360.reach360.BlurLayout;

import java.util.List;
import java.util.Locale;

public class optionsActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private Button btnLogout;
    private Session session;
    private Context mContext;
    private BlurLayout mSampleLayout, mSampleLayout2, mSampleLayout3, mSampleLayout4;
    private final int REQUEST_CODE=1;
    private FloatingActionButton fab;
    SQLiteDatabase mydatabase;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    public double currentLatitude;
    public double currentLongitude;
    public TextView textView,textView1;
    public boolean available;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_options);
        //Location Api Google
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
        session = new Session(this);
        if(!session.loggedin()){
            logout();
        }
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = "112";
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" +number));
                startActivity(intent);
            }
        });
        btnLogout = (Button)findViewById(R.id.logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        BlurLayout.setGlobalDefaultDuration(450);
        mSampleLayout = (BlurLayout)findViewById(R.id.blur_layout);
        View hover = LayoutInflater.from(mContext).inflate(R.layout.hover_sample, null);
        hover.findViewById(R.id.heart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Tada)
                        .duration(550)
                        .playOn(v);
            }
        });
        hover.findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Swing)
                        .duration(550)
                        .playOn(v);
            }
        });
        mSampleLayout.setHoverView(hover);
        mSampleLayout.setBlurDuration(550);
        mSampleLayout.addChildAppearAnimator(hover, R.id.heart, Techniques.FlipInX, 550, 0);
        mSampleLayout.addChildAppearAnimator(hover, R.id.share, Techniques.FlipInX, 550, 250);
        mSampleLayout.addChildAppearAnimator(hover, R.id.more, Techniques.FlipInX, 550, 500);

        mSampleLayout.addChildDisappearAnimator(hover, R.id.heart, Techniques.FlipOutX, 550, 500);
        mSampleLayout.addChildDisappearAnimator(hover, R.id.share, Techniques.FlipOutX, 550, 250);
        mSampleLayout.addChildDisappearAnimator(hover, R.id.more, Techniques.FlipOutX, 550, 0);

        mSampleLayout.addChildAppearAnimator(hover, R.id.description, Techniques.FadeInUp);
        mSampleLayout.addChildDisappearAnimator(hover, R.id.description, Techniques.FadeOutDown);

        //sample 2

        mSampleLayout2 = (BlurLayout)findViewById(R.id.blur_layout2);
        View hover2 = LayoutInflater.from(mContext).inflate(R.layout.hover_sample2, null);
        hover2.findViewById(R.id.avatar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Pretty Cool, Right?", Toast.LENGTH_SHORT).show();
            }
        });
        mSampleLayout2.setHoverView(hover2);

        mSampleLayout2.addChildAppearAnimator(hover2, R.id.description, Techniques.FadeInUp);
        mSampleLayout2.addChildDisappearAnimator(hover2, R.id.description, Techniques.FadeOutDown);
        mSampleLayout2.addChildAppearAnimator(hover2, R.id.avatar, Techniques.DropOut, 1200);
        mSampleLayout2.addChildDisappearAnimator(hover2, R.id.avatar, Techniques.FadeOutUp);
        mSampleLayout2.setBlurDuration(1000);

        hover2.findViewById(R.id.avatar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ShakeActivity.class));
            }
        });

        //sample3
        mSampleLayout3 = (BlurLayout)findViewById(R.id.blur_layout3);
        View hover3 = LayoutInflater.from(mContext).inflate(R.layout.hover_sample3, null);
        mSampleLayout3.setHoverView(hover3);
        mSampleLayout3.addChildAppearAnimator(hover3, R.id.eye, Techniques.Landing);
        mSampleLayout3.addChildDisappearAnimator(hover3, R.id.eye, Techniques.TakingOff);
        mSampleLayout3.enableZoomBackground(true);
        mSampleLayout3.setBlurDuration(1200);


        //sample 4

        mSampleLayout4 = (BlurLayout)findViewById(R.id.blur_layout4);
        View hover4 = LayoutInflater.from(mContext).inflate(R.layout.hover_sample4,null);
        mSampleLayout4.setHoverView(hover4);
        mSampleLayout4.addChildAppearAnimator(hover4, R.id.mail, Techniques.SlideInLeft);
        mSampleLayout4.addChildAppearAnimator(hover4, R.id.cat, Techniques.SlideInRight);

        mSampleLayout4.addChildDisappearAnimator(hover4, R.id.mail, Techniques.SlideOutLeft);
        mSampleLayout4.addChildDisappearAnimator(hover4, R.id.cat, Techniques.SlideOutRight);

        mSampleLayout4.addChildAppearAnimator(hover4, R.id.mail, Techniques.BounceIn);
        mSampleLayout4.addChildDisappearAnimator(hover4, R.id.cat, Techniques.FadeOutUp);

        hover4.findViewById(R.id.content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("content://contacts");
                Intent intent = new Intent(Intent.ACTION_PICK, uri);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        hover4.findViewById(R.id.cat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getWebPage = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/daimajia"));
                startActivity(getWebPage);
            }
        });

        hover4.findViewById(R.id.mail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double lat,longi;
                while(available == false){

                }
                lat = currentLatitude;
                longi = currentLongitude;
                String strAdd = "";
                Geocoder geocoder = new Geocoder(optionsActivity.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(lat, longi, 1);
                    if (addresses != null) {
                        Address returnedAddress = addresses.get(0);
                        StringBuilder strReturnedAddress = new StringBuilder("");

                        for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                            strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                        }
                        strAdd = strReturnedAddress.toString();
                        Log.w("ADDRESS", "ADDRESS");
                    } else {
                        Log.w("ADDRESS", "No Address returned!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.w("ADDRESS", "Cannoot get Address!");
                }
                mydatabase=openOrCreateDatabase("contactsDB",MODE_PRIVATE,null);
                Cursor resultSet=mydatabase.rawQuery("SELECT * from contacts",null);
                resultSet.moveToFirst();
                String num=resultSet.getString(0);
                /*SmsManager.getDefault().sendTextMessage(num, null, "I need help", null,null);*/
                Uri uri1 = Uri.parse("smsto:" + num);
                Intent intent1 = new Intent(Intent.ACTION_SENDTO, uri1);
                intent1.putExtra("sms_body", "I need help and my location co-ordinates are Latitude  "+lat+"  Longitude "+longi+" and address is "+strAdd);
                startActivity(intent1);

                while(!resultSet.isLast())
                {
                    resultSet.moveToNext();
                    String number=resultSet.getString(0);
                    Uri uri2 = Uri.parse("smsto:" + number);
                    Intent intent2 = new Intent(Intent.ACTION_SENDTO, uri2);

                    //Toast.makeText(getApplicationContext(),lat+" "+longi,Toast.LENGTH_LONG).show();
                    intent2.putExtra("sms_body", "I need help my location co-ordinates are Latitude "+lat+"  Longitude "+longi+"  and address is "+strAdd);
                    startActivity(intent2);

                }
            }
        });



    }
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = intent.getData();
                String[] projection = { ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME };

                Cursor cursor = getContentResolver().query(uri, projection,
                        null, null, null);
                cursor.moveToFirst();

                int numberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(numberColumnIndex);

                int nameColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                String name = cursor.getString(nameColumnIndex);

                mydatabase=openOrCreateDatabase("contactsDB",MODE_PRIVATE,null);
                //  Toast.makeText(MainActivity.this,"Database Created", Toast.LENGTH_LONG).show();
                mydatabase.execSQL("CREATE TABLE IF NOT EXISTS contacts(number VARCHAR);");
                String query="INSERT INTO contacts(number) VALUES(' "+number+"');";
                mydatabase.execSQL(query);
                Toast.makeText(optionsActivity.this,"Data saved successfully",Toast.LENGTH_SHORT).show();
                //  Toast.makeText(MainActivity.this,"Saved sucessfully",Toast.LENGTH_LONG).show();

            }
        }
    };





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void logout(){
        session.setLoggedin(false);
        finish();
        startActivity(new Intent(optionsActivity.this,LoginActivity.class));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            //If everything went fine lets get latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
            available = true;
            //varible set to enable
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
         /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
    }
    /// Location Override Functions
    @Override
    protected void onResume() {
        super.onResume();
        //Now lets connect to the API
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }


    }
}

