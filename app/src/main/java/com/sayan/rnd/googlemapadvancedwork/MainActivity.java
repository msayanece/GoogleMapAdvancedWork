package com.sayan.rnd.googlemapadvancedwork;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sayan.rnd.googlemapadvancedwork.mapsrelated.activities.MapsActivity;
import com.sayan.rnd.googlemapadvancedwork.mapsrelated.activities.MapsAnimationPlayback;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openMap(View view) {
        Intent intent = new Intent(this, MapsAnimationPlayback.class);
        startActivity(intent);
    }
}
