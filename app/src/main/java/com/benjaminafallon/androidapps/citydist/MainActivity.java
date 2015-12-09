package com.benjaminafallon.androidapps.citydist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] values = new String[] { "New York", "Los Angeles", "Chicago" };

        final ArrayList<City> list = new ArrayList<>();
        list.add(new City("New York", 0.0));
        list.add(new City("Los Angeles", 1.0));
        list.add(new City("Chicago", 2.0));


        ArrayAdapter itemsAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, list) {
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

    }

}
