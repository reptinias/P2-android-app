package com.example.test;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Gyroscope {

    public interface Listener{
        void onRotation(float rotaX, float rotaY, float rotaZ);
    }
    private Listener listener;
    public void setListener(Listener l){
        listener = l;
    }

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private SensorEventListener accelerometerEventListener;

    private int sensitivity = 10;

    Gyroscope(Context context){
        sensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        accelerometerEventListener = new SensorEventListener(){
            @Override
            public void onSensorChanged(SensorEvent sensorEvent){
                if(listener != null){
                    listener.onRotation(sensorEvent.values[0] * sensitivity, sensorEvent.values[1] * sensitivity,
                            sensorEvent.values[2] * sensitivity);
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
