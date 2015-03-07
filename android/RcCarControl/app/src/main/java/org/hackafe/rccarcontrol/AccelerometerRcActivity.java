package org.hackafe.rccarcontrol;

import android.content.Context;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


public class AccelerometerRcActivity extends ActionBarActivity implements View.OnTouchListener, SensorEventListener {

    private static final String TAG = "rc.acc";
    private SensorManager sensorManager;
    private Sensor sensor;
    private Sensor senAccelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer_rc);

        findViewById(R.id.btnForward).setOnTouchListener(this);
        findViewById(R.id.btnStop).setOnTouchListener(this);
        findViewById(R.id.btnBackward).setOnTouchListener(this);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        if (sensor != null) {
            Toast.makeText(this, "Using ROTATION_VECTOR", Toast.LENGTH_SHORT).show();
        } else {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            if (sensor != null) {
                Toast.makeText(this, "Gyroscope sensor found, using it", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No Gyroscope sensor found, using Accelerometer instead", Toast.LENGTH_SHORT).show();
                sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.btnForward:
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    RcApplication.CAR.forward();
                } else if (MotionEvent.ACTION_UP == event.getAction()) {
                    RcApplication.CAR.neutral();
                }
                break;
            case R.id.btnStop:
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    RcApplication.CAR.stop();
                } else if (MotionEvent.ACTION_UP == event.getAction()) {
                    RcApplication.CAR.neutral();
                }
                break;
            case R.id.btnBackward:
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    RcApplication.CAR.reverse();
                } else if (MotionEvent.ACTION_UP == event.getAction()) {
                    RcApplication.CAR.neutral();
                }
                break;
            default:
                break;
        }

        return false;
    }

    private int lastHeading = 0;

    @Override
    public void onSensorChanged(SensorEvent event) {
        String s = "Values: ";
        for (float val: event.values)
            s += val+", ";
        Log.d(TAG, s);

        float x = event.values[0]*4;
        int heading = Math.round(Math.copySign(x*x, event.values[0])*100);

        Log.d(TAG, "Heading: "+heading);
        if (heading != lastHeading) {
            lastHeading = heading;
            if (heading < -2) {
                RcApplication.CAR.left(-heading);
            } else if (heading > 2) {
                RcApplication.CAR.right(heading);
            } else {
                RcApplication.CAR.central();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
