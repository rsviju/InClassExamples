package com.example.etorunski.inclassexamples;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Vibrator;
import android.app.Activity;
import android.os.Bundle;
import android.provider.MediaStore;
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

        //if the bundle is not null (if this isn't the first time this activity starts)
        if(savedInstanceState!= null)
        {
            int passedMessage = savedInstanceState.getInt("GreetingMessage");

            passedMessage++;
        }



        //This commented out code shows that I can dynamically load a layout by code.
        // if(Math.random() < 0.5)
            setContentView(R.layout.activity_main);
      //  else  setContentView(R.layout.new_layout);



//Getting IDs
        Button b = (Button) findViewById(R.id.b1);
        final EditText number = (EditText)findViewById(R.id.editText);

        //Get a reference to the vibration motor. It vibrates for 500 ms in the button click
        final Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        Button b2 = (Button) findViewById(R.id.b2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //6 is my request code:
                MainActivity.this.startActivityForResult( new Intent(MainActivity.this, SecondActivity.class ) , 6 );
            }
        });


        //Send an email:
        Button b3 = (Button) findViewById(R.id.b3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Send an email:
                Intent sendEmailIntent = new Intent(Intent.ACTION_SENDTO );
                sendEmailIntent.setData(Uri.parse("mailto:")); // only email apps should handle this

                //A string array of all "To" recipient email addresses.
                String toAddresses [] = {"torunse@algonquincollege.com", "billg@microsoft.com", "andy_Rubin@google.com"};
                sendEmailIntent.putExtra(Intent.EXTRA_EMAIL,  toAddresses);

                //A string array of all "CC" recipient email addresses.
                String ccAddresses [] = {"copy1@algonquincollege.com", "copy2@microsoft.com"};
                sendEmailIntent.putExtra(Intent.EXTRA_CC,  ccAddresses);

                //A string array of all "BCC" recipient email addresses.
                String bccAddresses [] = {"bcopy1@algonquincollege.com", "bcopy2@microsoft.com"};
                sendEmailIntent.putExtra(Intent.EXTRA_BCC, bccAddresses);

                //set the subject:
                sendEmailIntent.putExtra(Intent.EXTRA_SUBJECT, "Good demo");

                //set the body:
                sendEmailIntent.putExtra(Intent.EXTRA_TEXT, "this is the body of the email");

                //Start the email activity with the data I've prepared. When it returns, request code will be 3:
                MainActivity.this.startActivityForResult( sendEmailIntent , 3 );
            }
        });



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
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //This function will call onSensorChanged whenever the sensor values have changed
    //    mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        String s = "";
        if(requestCode == 6)
        {
            Log.i("Activity result", "request code is 6, meaning the SecondActivity has finished");
            Log.w("The fox said:", s = data.getStringExtra("MyAnswer"));
        }
        else if(requestCode == 3)
        {
            Log.i("Activity result", "request code is 3, meaning the email Activity has finished");
        }
        s.length();
    }

}
