package com.example.egaprianto.testingsensors;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView mTextView; // Member variable for text view in the layout
    private SensorManager mSensorManager;
    private Sensor mSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mTextView = (TextView) findViewById(R.id.text_message);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
//            mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//            List<Sensor> accelerometerSensors = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
//            mSensor = null;
//            if (accelerometerSensors != null) {
//                for (int i = 0; i < accelerometerSensors.size(); i++) {
//                    if (accelerometerSensors.get(i).getVendor().contains("Google Inc.")) {
//                        // Use the version 3 gravity sensor.
//                        mSensor = accelerometerSensors.get(i);
//                    }
//                }
//            }
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();  // Always call the superclass

        // Stop method tracing that the activity started during onCreate()
        android.os.Debug.stopMethodTracing();
    }

    public void onClickLightSensorButton(View view){
        Intent intent = new Intent(this, SensorAccelerometerActivity.class);
        startActivity(intent);
    }

    public void onRotVecSensorButton(View view){
        Intent intent = new Intent(this, QuaternionTheta.class);
        startActivity(intent);
    }

    public void recordAllRAWSensorData(View view){
        Intent intent = new Intent(this, RecordAllRAWSensorData.class);
        startActivity(intent);
    }

    public void onGyroSensorClicked(View view){
        Intent intent = new Intent(this, SensorGyroscopeActivity.class);
        startActivity(intent);
    }
    public void onMagSensorClicked(View view){
        Intent intent = new Intent(this, SensorGeomagnetoticRotationActivity.class);
        startActivity(intent);
    }
}
