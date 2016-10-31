package com.example.egaprianto.testingsensors;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecordAllRAWSensorData extends AppCompatActivity implements SensorEventListener {
    private static final String FILENAME = "sensorRecord-";
    private SensorManager mSensorManager;
    private ArrayList<Sensor> sensorArrayList;
    private TextView textViewCountDown;
    private boolean isCapturing;
    private File file;
    private ArrayList<double[]> acceleroData;
    private ArrayList<double[]> gyroData;
    private long startCaptureTime;
    private long runningTime;
    private ArrayList<double[]> rotData;
    private ArrayList<double[]> rotVecData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("MASUK");
        super.onCreate(savedInstanceState);
        this.sensorArrayList = new ArrayList<Sensor>();
        setContentView(R.layout.activity_record_all_rawsensor_data);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        textViewCountDown = (TextView) findViewById(R.id.textViewCountDown);
        System.out.println("Sensor Manager = " + mSensorManager);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            //sensor accelero
            this.sensorArrayList.add(sensorGetter(Sensor.TYPE_ACCELEROMETER));
            //sensor Gyro
            this.sensorArrayList.add(sensorGetter(Sensor.TYPE_GYROSCOPE));
            //sensor Rotation Vector keluarinnya quaternion, tapi tidak make magnetic field
            this.sensorArrayList.add(sensorGetter(Sensor.TYPE_ROTATION_VECTOR));
        }
        System.out.println(sensorArrayList);

        int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        System.out.println("DEBUGDEBUGDEBUGDEBUGDEBUGDEBUG");
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
        isCapturing = false;
        System.out.println("DEBUGDEBUGDEBUGDEBUG");
        for (Sensor sensor :
                sensorArrayList) {
            mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (Sensor sensor :
                sensorArrayList) {
            mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if (isCapturing) {
            float[] copyData = new float[4];
            System.arraycopy(event.values, 0, copyData, 1, 3);
            copyData[0] = System.currentTimeMillis() - startCaptureTime;
            if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                this.acceleroData.add(convertFloatsToDoubles(copyData));
            } else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                this.gyroData.add(convertFloatsToDoubles(copyData));
            }else if (sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
                copyData = new float[6];
                System.arraycopy(event.values, 0, copyData, 1, event.values.length);
                copyData[0] = System.currentTimeMillis() - startCaptureTime;
                this.rotData.add(convertFloatsToDoubles(copyData));
                double theta = (Math.acos(event.values[3]))*2;
                double x = event.values[0]/ Math.sin(theta/2);
                double y = event.values[1]/ Math.sin(theta/2);
                double z = event.values[2]/ Math.sin(theta/2);
                double [] vecData = new double[5];
                vecData[0] = copyData[0];
                vecData[1] = x;
                vecData[2] = y;
                vecData[3] = z;
                vecData[4] = theta;
                this.rotVecData.add(vecData);
            }
            runningTime = (System.currentTimeMillis() - startCaptureTime);
            this.textViewCountDown.setText("Time : " + runningTime / 1000 + "sec");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void onStartButtonClicked(View view) throws InterruptedException {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        acceleroData = new ArrayList<double[]>();
        gyroData = new ArrayList<double[]>();
        rotData = new ArrayList<double[]>();
        rotVecData = new ArrayList<double[]>();
        String time = DateFormat.format("MM-dd-yyyyy-h|mm|ss-aa", System.currentTimeMillis()).toString();
        File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + File.separator + "DataSensors");
        root.mkdirs();
        file = new File(root, FILENAME + time + ".csv");
        try {
            System.out.println(file.getAbsolutePath());
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                textViewCountDown.setText("Count Down = " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                textViewCountDown.setText("Capturing Data!");
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();
                isCapturing = true;
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                startCaptureTime = System.currentTimeMillis();
            }
        }.start();

    }

    private Sensor sensorGetter(int input) {

        List<Sensor> sensorList = mSensorManager.getSensorList(input);
        Sensor sensor = null;
        for (int i = 0; i < sensorList.size(); i++) {
            sensor = sensorList.get(i);
        }
        return sensor;
    }

    private void writeData(BufferedWriter bw, ArrayList<double[]> data, String title, String columnTitle) throws IOException {
        bw.write(title);
        bw.newLine();
        bw.write(columnTitle);
        bw.newLine();
        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.get(i).length; j++) {
                bw.write(data.get(i)[j] + ",");
            }
            bw.newLine();
        }
        bw.newLine();
    }

    private double[] convertFloatsToDoubles(float[] input)
    {
        if (input == null)
        {
            return null; // Or throw an exception - your choice
        }
        double[] output = new double[input.length];
        for (int i = 0; i < input.length; i++)
        {
            output[i] = input[i];
        }
        return output;
    }

    public void onStopButtonClicked(View view) {
        isCapturing = false;
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(this.file));
            writeData(bw, acceleroData, "Accelerometer", "time,x,y,z");
            writeData(bw, gyroData, "Gyroscope", "time,x,y,z");
            writeData(bw, rotData, "Rotation Vector", "time,x*sin(theta/2),y*sin(theta/2),z*sin(theta/2),cos(theta/2),akurasi (dalam radians) (-1 jika tidak ada)");
            writeData(bw, rotVecData, "Rotation Vector by Vector and Angle", "time,x,y,z,theta");
            bw.newLine();
            bw.write("Running Time : " + runningTime + "ms");
            bw.flush();
            bw.close();
            file.createNewFile();

            textViewCountDown.setText("Data Captured! at " + file.getAbsolutePath());
            Log.d("File", "File is located at " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
