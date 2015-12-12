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
    ArrayList<City> list;
    ArrayAdapter itemsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //String[] values = new String[] { "New York", "Los Angeles", "Chicago" };

        list = new ArrayList<>();

        //US_CITIES = api request here

        //for (int i = 0; i < US_CITIES.length(); i++) {

        //}


        list.add(new City("New York", 0.0));
        list.add(new City("Los Angeles", 1.0));
        list.add(new City("Chicago", 2.0));


        itemsAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, list) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(list.get(position).getName());
                text2.setText(String.valueOf(list.get(position).getDistance()));
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
        //String stateName = addresses.get(0).getAddressLine(1);
        //String countryName = addresses.get(0).getAddressLine(2);

        TextView placeTextView = (TextView) findViewById(R.id.placeTextView);
        placeTextView.setText(cityName);


        //New York City
        Location.distanceBetween(myLat, myLong, 40.7127, -74.0059, results);
        double distanceNYC = results[0];
        //Los Angeles
        Location.distanceBetween(myLat, myLong, 34.0500, -118.2500, results);
        double distanceLA = results[0];
        //Chicago
        Location.distanceBetween(myLat, myLong, 41.8369, -87.6847, results);
        double distanceCHI = results[0];

        //gives distance in miles
        distanceNYC = distanceNYC / 1609.344;
        distanceLA = distanceLA / 1609.344;
        distanceCHI = distanceCHI / 1609.344;

        list.get(0).setDistance(distanceNYC);
        list.get(1).setDistance(distanceLA);
        list.get(2).setDistance(distanceCHI);

        itemsAdapter.notifyDataSetChanged();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
