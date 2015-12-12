package com.benjaminafallon.androidapps.citydist;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    GoogleApiClient mGoogleApiClient = null;
    Location mLastLocation;
    TextView textViewLong;
    TextView textViewLat;
    ArrayList<City> top10Cities;
    ArrayAdapter itemsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        top10Cities = new ArrayList<>();

        top10Cities.add(new City("New York", 40.7127837, -74.00594130000002));
        top10Cities.add(new City("Los Angeles", 34.0522342, -118.2436849));
        top10Cities.add(new City("Chicago", 41.8781136, -87.62979819999998));
        top10Cities.add(new City("Houston", 29.7604267, -95.3698028));
        top10Cities.add(new City("Philadelphia", 39.9525839, -75.16522150000003));
        top10Cities.add(new City("Phoenix", 33.4483771, -112.07403729999999));
        top10Cities.add(new City("San Antonio", 29.4241219, -98.49362819999999));
        top10Cities.add(new City("San Diego", 32.715738, -117.16108380000003));
        top10Cities.add(new City("Dallas", 32.7766642, -96.79698789999998));
        top10Cities.add(new City("San Jose", 37.3382082, -121.88632860000001));

        itemsAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, top10Cities) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(top10Cities.get(position).getName());
                text2.setText(String.valueOf(top10Cities.get(position).getDistance()));
                return view;
            }
        };

        ListView listView = (ListView) findViewById(R.id.mainListView);
        listView.setAdapter(itemsAdapter);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        double myLat = 0.0;
        double myLong = 0.0;
        float[] results = new float[1];
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            myLat = mLastLocation.getLatitude();
            myLong = mLastLocation.getLongitude();
        }

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(myLat, myLong, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String cityName = addresses.get(0).getAddressLine(0);

        TextView placeTextView = (TextView) findViewById(R.id.placeTextView);
        placeTextView.setText(cityName);

        for (int i = 0; i < top10Cities.size(); i++) {
           top10Cities.get(i).findAndSetDistance(myLat, myLong);
        }

        itemsAdapter.notifyDataSetChanged();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
