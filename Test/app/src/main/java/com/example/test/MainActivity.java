package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {

    private float rThreshold = 10.0f;
    private float tThreshold = 10.0f;
    private static int steps;
    private Accelerometer accelerometer;
    private Gyroscope gyroscope;
    private boolean phoneTranslate;
    private boolean phoneRotate;


    public static final String EXTRA_MESSAGE = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accelerometer = new Accelerometer(this);
        gyroscope = new Gyroscope(this);

        accelerometer.setListener(new Accelerometer.Listener() {
            @Override
            public void onTranslation(float transX, float transY, float transZ) {
                if(transX > tThreshold || transY > tThreshold || transZ > tThreshold){
                    phoneTranslate = false;
                }if(transX < -tThreshold || transY < -tThreshold || transZ < -tThreshold){
                    phoneTranslate = false;
                }else{
                    phoneTranslate = true;
                }
                //stepIncrease();
            }
        });

        gyroscope.setListener(new Gyroscope.Listener() {
            @Override
            public void onRotation(float rotaX, float rotaY, float rotaZ) {
                if(rotaX > rThreshold || rotaY > rThreshold || rotaZ > rThreshold){
                    phoneRotate = true;
                }else if(rotaX < -rThreshold || rotaY < -rThreshold ||rotaZ < -rThreshold){
                    phoneRotate = true;
                }else{
                    phoneRotate = false;
                }
                stepIncrease();
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();

        accelerometer.register();
        gyroscope.register();
    }

    @Override
    public void onPause(){
        super.onPause();

        accelerometer.unregister();
        gyroscope.unregister();
    }

    private void stepIncrease(){
        if(phoneRotate && !phoneTranslate){
            getWindow().getDecorView().setBackgroundColor(Color.BLUE);
            steps++;
            TextView textView = findViewById(R.id.stepText);
            textView.setText("Steps taken" + Integer.toString(steps));
        }else{
            getWindow().getDecorView().setBackgroundColor(Color.RED);
        }
    }

    private void debugger(){
        Log.d("my1Tag", Boolean.toString(phoneRotate));
        Log.d("my2Tag", Boolean.toString(phoneTranslate));
        Log.d("my3Tag", Integer.toString(steps));
    }

    //Called when the user taps the Send button
    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}
