package ega.ftis.unpar.com.sensorrecorder;

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

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelero_sensor);
        textViewXData = (TextView) findViewById(R.id.textViewXData);
        textViewYData = (TextView) findViewById(R.id.textViewYData);
        textViewZData = (TextView) findViewById(R.id.textViewZData);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            List<Sensor> accelerometerSensors = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
            mAccelSensor = null;
            if (accelerometerSensors != null) {
                for (int i = 0; i < accelerometerSensors.size(); i++) {
                    mAccelSensor = accelerometerSensors.get(i);
                }
            }
        }
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        textViewXData.setText("x = " + x);
        textViewYData.setText("y = " + y);
        textViewZData.setText("z = " + z);
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