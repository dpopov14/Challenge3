package com.example.challenge3;

import android.content.Context;
import android.content.res.AssetManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mSensorAccelerometer;
    private Sensor mSensorGyroscope;
    private Sensor mSensorLinearAccelerometer;
    private Sensor mSensorMagnetometer;
    Double[] AccelerometerData = new Double[3];
    Double[] GyroscopeData = new Double[3];
    Double[] LinearAccelerometerData = new Double[3];
    Double[] MagnetometerData = new Double[3];
    Classifier cls = null;
    AtomicBoolean run = new AtomicBoolean(true);
    TextView activityText;


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


        //get sensors
        mSensorAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensorLinearAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mSensorMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);


        //Get the error string:
        String sensor_error = getResources().getString(R.string.error_no_sensor);


        // deserialize model




        // we need those for creating new instances later
        // order of attributes/classes needs to be exactly equal to those used for training
        final Attribute attributeAccelx = new Attribute("Wrist_Ax");
        final Attribute attributeAccely = new Attribute("Wrist_pocket_Ay");
        final Attribute attributeAccelz = new Attribute("Wrist_Az");
        final Attribute attributeLinx = new Attribute("Wrist_Lx");
        final Attribute attributeLiny = new Attribute("Wrist_Ly");
        final Attribute attributeLinz = new Attribute("Wrist_Lz");
        final Attribute attributeGyrox = new Attribute("Wrist_Gx");
        final Attribute attributeGyroy = new Attribute("Wrist_Gy");
        final Attribute attributeGyroz = new Attribute("Wrist_Gz");
        final Attribute attributeMagx = new Attribute("Wrist_Mx");
        final Attribute attributeMagy = new Attribute("Wrist_My");
        final Attribute attributeMagz = new Attribute("Wrist_Mz");

        final List<String> classes = new ArrayList<String>() {
            {
                add("walking"); // cls nr 1
                add("standing"); // cls nr 2
                add("jogging"); // cls nr 3
                add("sitting"); // cls nr 4
                add("biking"); // cls nr 5
                add("upstairs"); // cls nr 6
                add("downstairs"); // cls nr 7
            }
        };

        // Instances(...) requires ArrayList<> instead of List<>...
        ArrayList<Attribute> attributeList = new ArrayList<Attribute>() {
            {
                add(attributeAccelx);
                add(attributeAccely);
                add(attributeAccelz);
                add(attributeLinx);
                add(attributeLiny);
                add(attributeLinz);
                add(attributeGyrox);
                add(attributeGyroy);
                add(attributeGyroz);
                add(attributeMagx);
                add(attributeMagy);
                add(attributeMagz);
                Attribute attributeClass = new Attribute("@@class@@", classes);
                add(attributeClass);
            }
        };



        //When the start recording button is clicked
        button = findViewById(R.id.start_recording);

        button.setOnClickListener(v -> {
            //start the sensors
            run.set(true);
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






            //start the measuring thread

            activityText = (TextView) findViewById(R.id.textView);
            Thread ActivityClassifierThread = new Thread(new Runnable() {
                public void run()
                {
                    while(run.get()){
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //will periodically check the list with samples and create an instance
                        Instances dataUnpredicted = new Instances("CurrentInstance",
                                attributeList, 1);
                        // last feature is target variable
                        dataUnpredicted.setClassIndex(dataUnpredicted.numAttributes() - 1);

                        // create new instance: this one should fall into the setosa domain

                        DenseInstance newInstance = new DenseInstance(dataUnpredicted.numAttributes()) {
                            {
                                setValue(attributeAccelx, AccelerometerData[0]);
                                setValue(attributeAccely, AccelerometerData[1]);
                                setValue(attributeAccelz, AccelerometerData[2]);
                                System.out.println(GyroscopeData[0]);
                                setValue(attributeLinx, GyroscopeData[0]);
                                setValue(attributeLiny, GyroscopeData[1]);
                                setValue(attributeLinz, GyroscopeData[2]);
                                setValue(attributeGyrox, LinearAccelerometerData[0]);
                                setValue(attributeGyroy, LinearAccelerometerData[1]);
                                setValue(attributeGyroz, LinearAccelerometerData[2]);
                                setValue(attributeMagx, MagnetometerData[0]);
                                setValue(attributeMagy, MagnetometerData[1]);
                                setValue(attributeMagz, MagnetometerData[2]);

                            }
                        };

                        newInstance.setDataset(dataUnpredicted);
                        //will classify samples
                        try {
                            AssetManager assetManager = getAssets();
                            try {
                                cls= (Classifier) weka.core.SerializationHelper.read(assetManager.open("J48.model"));
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            double result = cls.classifyInstance(newInstance);
                            String className = classes.get(new Double(result).intValue());
                            if(className == null){
                                System.out.println("Classname is null");

                            }
                            System.out.println(className);
                            activityText.setText(className);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }});
            ActivityClassifierThread.start();
        });


        //When the stop recording button is clicked
        button = findViewById(R.id.stop_recording);
        button.setOnClickListener(v -> {
            run.set(false);
            mSensorManager.unregisterListener(this);
            System.out.println("Sensors stopped");
//            for(float[] a : MagnetometerData){
////                System.out.println("Row: " + a[0]+ " "+ a[1]+ " " + a[2]);
////            }

            System.out.println("AccData: " + AccelerometerData.toString());
            System.out.println("GyroData: " + GyroscopeData.toString());
            System.out.println("LinearData: " + LinearAccelerometerData.toString());
            System.out.println("MagnetData: " + MagnetometerData.toString());

        });
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();

        //Collect current x,y,z data
        Double[] currentData = new Double[3];
        for(int i = 0;  i < 3; i++){
            currentData[i] = Double.parseDouble(new Float(sensorEvent.values[i]).toString());
        }
//        Double[] currentData = Double.parseDouble(sensorEvent.values.toString());

        switch(sensorType){
            //Event came from the Accelerometer
            case Sensor.TYPE_ACCELEROMETER:
                AccelerometerData = currentData;
                //Handle Accelerometer
//                    AccelerometerData.add(currentData);
                break;
            case Sensor.TYPE_GYROSCOPE:
                    //Handle Gyroscope
//                    GyroscopeData.add(currentData);
                    GyroscopeData = currentData;

                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                //Handle Linear Accelerometer
//                    LinearAccelerometerData.add(currentData);
                LinearAccelerometerData = currentData;
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                //Handle Magnetometer
//                MagnetometerData.add(currentData);
                MagnetometerData = currentData;
                break;
            default:
                //do nothing;
        }

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }













}