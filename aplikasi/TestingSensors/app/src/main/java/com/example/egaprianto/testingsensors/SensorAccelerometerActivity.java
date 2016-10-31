package com.example.egaprianto.testingsensors;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class SensorAccelerometerActivity extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mAccelSensor;
    private TextView textViewXData;
    private TextView textViewYData;
    private TextView textViewZData;
    private TextView textViewDebug;
    private TextView textViewAccuracy;

    private float lastX;
    private float lastY;
    private float lastZ;

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelero_sensor);
        textViewXData = (TextView) findViewById(R.id.textViewXData);
        textViewYData = (TextView) findViewById(R.id.textViewYData);
        textViewZData = (TextView) findViewById(R.id.textViewZData);
        textViewDebug = (TextView) findViewById(R.id.textViewDebug);
        textViewAccuracy = (TextView) findViewById(R.id.textViewAccuracy);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            List<Sensor> accelerometerSensors = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
            mAccelSensor = null;
            if (accelerometerSensors != null) {
                System.out.print("ADA SENSORNYA");
                for (int i = 0; i < accelerometerSensors.size(); i++) {
                    mAccelSensor = accelerometerSensors.get(i);
                }
            }
        }
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
        textViewAccuracy.setText("Accuracy ="+accuracy+" Coba sout aja sensornya ="+sensor);
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        textViewXData.setText("x = " + x);
        textViewYData.setText("y = " + y);
        textViewZData.setText("z = " + z);
        String tambahanAja = "";
        if (lastX!=x){
            tambahanAja += " beda";
        }else{
            tambahanAja += " sama";
        }
        if (lastY!=y){
            tambahanAja += " beda";
        }else{
            tambahanAja += " sama";
        }
        if (lastZ!=z){
            tambahanAja += " beda";
        }else{
            tambahanAja += " sama";
        }

        textViewDebug.setText("Debugging = " + mAccelSensor.getMinDelay()+tambahanAja);
        // Do something with this sensor value.
        lastX = x;
        lastY = y;
        lastZ = z;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }


}