package com.example.etorunski.inclassexamples;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by etorunski on 2016-09-27.
 */

public class SecondActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_layout);

        Button finishButton = (Button)findViewById(R.id.finishButton);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("MyAnswer",((EditText) findViewById(R.id.answerText)).getText().toString());

                //55 is my result code:
                setResult(55 , intent);
                finish();
            }
        });




        //open the file
        SharedPreferences prefs = getSharedPreferences("storeddata", Context.MODE_PRIVATE);

        //Read what's saved under the string "Number", If nothing, return 0
        int num = prefs.getInt("Number", 0);

        //Get an object to write to the file:
        SharedPreferences.Editor writer = prefs.edit();

        //Write put a number:
        writer.putInt("Number", num+1 );

        //Write the file:
        writer.commit();

        //Show a message at the "Debug" logging level
        Log.d("Num times run: ", "" + num);

    }
}
