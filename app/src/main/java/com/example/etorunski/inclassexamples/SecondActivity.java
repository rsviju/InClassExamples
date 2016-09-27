package com.example.etorunski.inclassexamples;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by etorunski on 2016-09-27.
 */

public class SecondActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_layout);

        SharedPreferences prefs = getSharedPreferences("storeddata", Context.MODE_PRIVATE);
        int num = prefs.getInt("Number", 0);

        SharedPreferences.Editor writer = prefs.edit();

Log.e("Num times run: ", "" + num);

        writer.putInt("Number", num+1 );
    }
}
