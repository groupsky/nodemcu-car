package org.hackafe.rccarcontrol;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class ControlActivity extends Activity implements OnTouchListener {

	public static final String TAG = "ControlActivity";
	
	MqttManager mqttManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control);

		findViewById(R.id.btnLeft).setOnTouchListener(this);
		findViewById(R.id.btnRight).setOnTouchListener(this);
		findViewById(R.id.btnForward).setOnTouchListener(this);
		findViewById(R.id.btnBackward).setOnTouchListener(this);
		mqttManager = new MqttManager();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mqttManager.connect();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mqttManager.disconnect();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		switch (v.getId()) {
		case R.id.btnLeft:
			if (MotionEvent.ACTION_DOWN == event.getAction()) {
				mqttManager.sendMessage("l");
			} else if (MotionEvent.ACTION_UP == event.getAction()) {
				mqttManager.sendMessage("c");
			}
			break;
		case R.id.btnRight:
			if (MotionEvent.ACTION_DOWN == event.getAction()) {
				mqttManager.sendMessage("r");
			} else if (MotionEvent.ACTION_UP == event.getAction()) {
				mqttManager.sendMessage("c");
			}
			break;
		case R.id.btnForward:
			if (MotionEvent.ACTION_DOWN == event.getAction()) {
				mqttManager.sendMessage("f");
			} else if (MotionEvent.ACTION_UP == event.getAction()) {
				mqttManager.sendMessage("n");
			}
			break;
		case R.id.btnBackward:
			if (MotionEvent.ACTION_DOWN == event.getAction()) {
				mqttManager.sendMessage("b");
			} else if (MotionEvent.ACTION_UP == event.getAction()) {
				mqttManager.sendMessage("n");
			}
			break;
		default:
			break;
		}
		return false;
	}

}
