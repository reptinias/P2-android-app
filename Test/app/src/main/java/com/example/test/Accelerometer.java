package com.example.test;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Accelerometer {

    public interface Listener{
        void onTranslation(float transX, float transY, float transZ);
    }
    private  Listener listener;
    public void setListener(Listener l){
        listener = l;
    }

    private float[] gravity = new float[3];
    private float[] linear_acceleration = new float[3];

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private SensorEventListener accelerometerEventListener;

    private int sensitivity = 100;

    Accelerometer(Context context){
        sensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        accelerometerEventListener = new SensorEventListener(){
            @Override
            public void onSensorChanged(SensorEvent sensorEvent){
                final float alpha = 0.8f;

                // Isolate the force of gravity with the low-pass filter.
                gravity[0] = alpha * gravity[0] + (1 - alpha) * sensorEvent.values[0];
                gravity[1] = alpha * gravity[1] + (1 - alpha) * sensorEvent.values[1];
                gravity[2] = alpha * gravity[2] + (1 - alpha) * sensorEvent.values[2];

                // Remove the gravity contribution with the high-pass filter.
                linear_acceleration[0] = sensorEvent.values[0] - gravity[0];
                linear_acceleration[1] = sensorEvent.values[1] - gravity[1];
                linear_acceleration[2] = sensorEvent.values[2] - gravity[2];

                if(listener != null){
                    listener.onTranslation(
                            linear_acceleration[0], linear_acceleration[1], linear_acceleration[2]
                            );
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i){

            }
        };
    }

    public void register(){
        sensorManager.registerListener(accelerometerEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregister(){
        sensorManager.unregisterListener(accelerometerEventListener);
    }
}
