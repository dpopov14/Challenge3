package com.example.challenge3;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mSensorAccelerometer;
    private Sensor mSensorGyroscope;
    private Sensor mSensorLinearAccelerometer;
    private Sensor mSensorMagnetometer;

    List<float[]> AccelerometerData = new ArrayList<float[]>();
    List<float[]> GyroscopeData = new ArrayList<float[]>();
    List<float[]> LinearAccelerometerData = new ArrayList<float[]>();
    List<float[]> MagnetometerData = new ArrayList<float[]>();





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Button button;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get instance of a sensor manager
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //Get a list of all sensors
        List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        StringBuilder sensorText = new StringBuilder();

        for (Sensor currentSensor : sensorList ) {
            sensorText.append(currentSensor.getName()).append(
                    System.getProperty("line.separator"));
        }
        //Print all sensor data
        System.out.println(sensorText);



        mSensorAccelerometer =
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensorLinearAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mSensorMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);


        //Get the error string:
        String sensor_error = getResources().getString(R.string.error_no_sensor);


        //When the start recording button is clicked
        button = findViewById(R.id.start_recording);
        button.setOnClickListener(v -> {
//            this.onStart();
            if (mSensorAccelerometer != null) {
                mSensorManager.registerListener(this, mSensorAccelerometer,
                        SensorManager.SENSOR_DELAY_NORMAL);
            }
            if (mSensorGyroscope != null) {
                mSensorManager.registerListener(this, mSensorGyroscope,
                        SensorManager.SENSOR_DELAY_NORMAL);
            }
            if (mSensorLinearAccelerometer != null) {
                mSensorManager.registerListener(this, mSensorLinearAccelerometer,
                        SensorManager.SENSOR_DELAY_NORMAL);
            }
            if (mSensorMagnetometer != null) {
                mSensorManager.registerListener(this, mSensorMagnetometer,
                        SensorManager.SENSOR_DELAY_NORMAL);
            }
        });

        //When the stop recording button is clicked
        button = findViewById(R.id.stop_recording);
        button.setOnClickListener(v -> {
//            this.onStop();
            mSensorManager.unregisterListener(this);

            System.out.println("KUUUUUUUUUUUR");
//        System.out.println(AccelerometerData.toString());

            for(float[] a : MagnetometerData){
                System.out.println("Row: " + a[0]+ " "+ a[1]+ " " + a[2]);
            }

        });




//        first commit
    }

    @Override
    protected void onStart() {
        super.onStart();

//        if (mSensorAccelerometer != null) {
//            mSensorManager.registerListener(this, mSensorAccelerometer,
//                    SensorManager.SENSOR_DELAY_NORMAL);
//        }
//        if (mSensorGyroscope != null) {
//            mSensorManager.registerListener(this, mSensorGyroscope,
//                    SensorManager.SENSOR_DELAY_NORMAL);
//        }
//        if (mSensorLinearAccelerometer != null) {
//            mSensorManager.registerListener(this, mSensorLinearAccelerometer,
//                    SensorManager.SENSOR_DELAY_NORMAL);
//        }
//        if (mSensorMagnetometer != null) {
//            mSensorManager.registerListener(this, mSensorMagnetometer,
//                    SensorManager.SENSOR_DELAY_NORMAL);
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        mSensorManager.unregisterListener(this);
//
//        System.out.println("KUUUUUUUUUUUR");
////        System.out.println(AccelerometerData.toString());
//
//        for(float[] a : MagnetometerData){
//            System.out.println("Row: " + a[0]+ " "+ a[1]+ " " + a[2]);
//        }
//
//
////        System.out.println(GyroscopeData.toString());
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();

        //Collect current x,y,z data
        float[] currentData = sensorEvent.values;
        switch(sensorType){
            //Event came from the Accelerometer
            case Sensor.TYPE_ACCELEROMETER:
                //Handle Accelerometer
                    AccelerometerData.add(currentData);
                break;
            case Sensor.TYPE_GYROSCOPE:
                    //Handle Gyroscope
                    GyroscopeData.add(currentData);
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                //Handle Linear Accelerometer
                    LinearAccelerometerData.add(currentData);
                    break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                //Handle Magnetometer
                MagnetometerData.add(currentData);
                break;
            default:
                //do nothing;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}