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

public class SensorGeomagnetoticRotationActivity extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mGeomagSensor;
    private TextView textViewXData;
    private TextView textViewYData;
    private TextView textViewZData;

    private float lastX;
    private float lastY;
    private float lastZ;

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geomagnetic_rotation_sensor);
        textViewXData = (TextView) findViewById(R.id.textViewXData);
        textViewYData = (TextView) findViewById(R.id.textViewYData);
        textViewZData = (TextView) findViewById(R.id.textViewZData);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            List<Sensor> geomagSensors = mSensorManager.getSensorList(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR);
            mGeomagSensor = null;
            if (geomagSensors != null) {
                System.out.print("ADA SENSORNYA");
                for (int i = 0; i < geomagSensors.size(); i++) {
                    mGeomagSensor = geomagSensors.get(i);
                }
            }
        }
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.
        // Do something with this sensor value.
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
        lastX = x;
        lastY = y;
        lastZ = z;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mGeomagSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }


}