package org.hackafe.rccarcontrol;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import org.eclipse.paho.client.mqttv3.MqttException;


public class SimpleRcActivity extends ActionBarActivity implements View.OnTouchListener {

    private static final String TAG = "rc.simple";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_rc);

		findViewById(R.id.btnLeft).setOnTouchListener(this);
		findViewById(R.id.btnRight).setOnTouchListener(this);
		findViewById(R.id.btnForward).setOnTouchListener(this);
		findViewById(R.id.btnBackward).setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        		switch (v.getId()) {
		case R.id.btnLeft:
			if (MotionEvent.ACTION_DOWN == event.getAction()) {
				RcApplication.CAR.left();
			} else if (MotionEvent.ACTION_UP == event.getAction()) {
				RcApplication.CAR.central();
			}
			break;
		case R.id.btnRight:
			if (MotionEvent.ACTION_DOWN == event.getAction()) {
				RcApplication.CAR.right();
			} else if (MotionEvent.ACTION_UP == event.getAction()) {
				RcApplication.CAR.central();
			}
			break;
		case R.id.btnForward:
			if (MotionEvent.ACTION_DOWN == event.getAction()) {
				RcApplication.CAR.forward();
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
}
