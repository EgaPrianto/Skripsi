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

public class QuaternionTheta extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mAccelSensor;
    private TextView textViewXData;
    private TextView textViewYData;
    private TextView textViewZData;
    private TextView textViewThetaData;
    private TextView textViewXSinData;
    private TextView textViewYSinData;
    private TextView textViewZSinData;
    private TextView textViewCosData;
    private TextView textViewDebug;
    private TextView textViewAccuracy;

    private float lastX;
    private float lastY;
    private float lastZ;

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quaternion_theta_sensor);
        textViewXData = (TextView) findViewById(R.id.textViewXData);
        textViewYData = (TextView) findViewById(R.id.textViewYData);
        textViewZData = (TextView) findViewById(R.id.textViewZData);
        textViewThetaData = (TextView) findViewById(R.id.textViewThetaData);
        textViewXSinData = (TextView) findViewById(R.id.textViewXSinData);
        textViewYSinData = (TextView) findViewById(R.id.textViewYSinData);
        textViewZSinData = (TextView) findViewById(R.id.textViewZSinData);
        textViewCosData = (TextView) findViewById(R.id.textViewCosData);
        textViewDebug = (TextView) findViewById(R.id.textViewDebug);
        textViewAccuracy = (TextView) findViewById(R.id.textViewAccuracy);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            List<Sensor> accelerometerSensors = mSensorManager.getSensorList(Sensor.TYPE_ROTATION_VECTOR);
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
        float xsin = event.values[0];
        float ysin = event.values[1];
        float zsin = event.values[2];
        float cos = event.values[3];
        double theta = (Math.acos(event.values[3]))*2;
        double x = event.values[0]/ Math.sin(theta/2);
        double y = event.values[1]/ Math.sin(theta/2);
        double z = event.values[2]/ Math.sin(theta/2);
        textViewXData.setText(String.format("x = %.6f",  x));
        textViewYData.setText(String.format("y = %.6f" , y));
        textViewZData.setText(String.format("z = %.6f" , z));
        textViewThetaData.setText(String.format("theta = %.6f" , theta));
        textViewXSinData.setText(String.format("x sin = %.6f" , xsin));
        textViewYSinData.setText(String.format("y sin = %.6f" , ysin));
        textViewZSinData.setText(String.format("z sin = %.6f" , zsin));
        textViewCosData.setText(String.format("cos = %.6f" , cos));
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