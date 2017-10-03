package com.shashankraman.memorableplaces;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> list = new ArrayList<String> ();
    static final ArrayList<LatLng> locations = new ArrayList<LatLng> ();
    static ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.listView);

        ArrayList<String> latitudes = new ArrayList<String>();
        ArrayList<String> longitudes = new ArrayList<String>();
        list.clear();
        latitudes.clear();
        longitudes.clear();
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.shashankraman.memorableplaces",
                Context.MODE_PRIVATE);
        try
        {
            list = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("places",
                    ObjectSerializer.serialize(new ArrayList<String>())));
            latitudes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("latitudes",
                    ObjectSerializer.serialize(new ArrayList<String>())));
            longitudes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("longitudes",
                    ObjectSerializer.serialize(new ArrayList<String>())));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if(latitudes.size() > 0 && list.size() > 0 && longitudes.size() > 0)
        {
            if(list.size() == latitudes.size() && latitudes.size() == longitudes.size())
            {
                for(int i = 0; i < latitudes.size(); i++)
                {
                    locations.add(new LatLng(Double.parseDouble(latitudes.get(i)), Double.parseDouble(longitudes.get(i))));
                }
            }
        }
        else
        {
            list.add("Click to add place...");
            locations.add(new LatLng(0, 0));
        }
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(getApplication(), MapsActivity.class);
                intent.putExtra("placeNumber", position);
                startActivity(intent);
            }
        });
    }
}
