package com.example.etorunski.inclassexamples;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;


public class MainActivity extends Activity implements SensorEventListener {


 //Sensor manager has a list of all the sensors
    private SensorManager mSensorManager;


 //want to get access to one sensor:
    private Sensor mSensor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //This line makes the app fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //This commented out code shows that I can dynamically load a layout by code.
        // if(Math.random() < 0.5)
            setContentView(R.layout.activity_main);
      //  else  setContentView(R.layout.new_layout);



//Getting IDs
        Button b = (Button) findViewById(R.id.b1);
        final EditText number = (EditText)findViewById(R.id.editText);

        //Get a reference to the vibration motor. It vibrates for 500 ms in the button click
        final Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        final ToggleButton tb = (ToggleButton) findViewById(R.id.toggleButton);
//End of getting IDs




//Set button to vibrate phone:
        b.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try {
                    // Vibrate for 500 milliseconds
                    vb.vibrate(500);
                    Log.i("Hi" + R.id.b1, "There:" + number.getText());
                }
                catch(Exception e)
                {
                    e.getMessage();
                }
            }
        });


//Camera flash:
        //This next part deals with camera flash stuff, with the toggle button
        boolean hasFlash = getPackageManager().hasSystemFeature( PackageManager.FEATURE_CAMERA_FLASH);
        tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try
                {
                    if (isChecked) {
                        Camera camera = Camera.open();
                        SurfaceTexture dummy = new SurfaceTexture(0);
                        camera.setPreviewTexture(dummy);
                        Camera.Parameters parameters = camera.getParameters();
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        camera.setParameters(parameters);
                        camera.startPreview();
                    } else {
                        Camera camera2 = Camera.open();
                        Camera.Parameters parameters2 = camera2.getParameters();
                        parameters2.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        camera2.setParameters(parameters2);
                        camera2.stopPreview();
                    }
                }
                catch (Exception e)
                {
                    Log.d("Crash!!!", e.getMessage());
                }
        }
    });
//end camera flash


// Reading sensors
        //Get a reference to the phone's "Sensor Catalog"
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //See if the Sensor type is on your phone: Change what comes after Sensor. to get other sensors
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        //This function will call onSensorChanged whenever the sensor values have changed
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
//end reading sensors
    }




//SensorListener interface:
    //On sensor changed will pass in an array of float values. Depending on the sensor type,
    //This will be an array of size 1 for the light sensor, or size 3 for the accelerometer,
    //orientation sensor, etc.
    public void onSensorChanged (SensorEvent event){
        float info [] = event.values;

        if(info.length > 1) {
            //If the accelerometer measures a force of 4*g in any direction:
            if (info[0] > 4.0 || info[1] > 4.0 || info[2] > 13.0)
                Log.i("Sensor values:", String.format("%f, %f, %f", info[0], info[1], info[2]));
        }
        else{
            Log.i("Sensor values:", "Value:" + info[0] );
        }
    }

    //I don't care about this function, only the sensor changed function
    public void onAccuracyChanged (Sensor sensor, int accuracy) {  }
    //end of required interface functions
//End of sensor listener interface

}
