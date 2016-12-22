package ega.ftis.unpar.com.sensorrecorder;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView mTextView; 
    private SensorManager mSensorManager;
    private Sensor mSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
