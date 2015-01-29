package org.hackafe.rccarcontrol;

import java.util.UUID;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class ControlActivity extends Activity implements OnTouchListener {

	public static final String TAG = "ControlActivity";

	String topic = "/hackafe-car";
	String content = "r";
	int qos = 0;
	String broker = "tcp://broker.mqtt-dashboard.com:1883";
	String clientId = UUID.randomUUID().toString();
	MemoryPersistence persistence = new MemoryPersistence();

	MqttClient mqttClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control);

		findViewById(R.id.btnLeft).setOnTouchListener(this);
		findViewById(R.id.btnRight).setOnTouchListener(this);
		findViewById(R.id.btnForward).setOnTouchListener(this);
		findViewById(R.id.btnBackward).setOnTouchListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		connect();
	}

	@Override
	protected void onPause() {
		super.onPause();
		disconnect();
	}

	private void connect() {
		try {
			mqttClient = new MqttClient(broker, clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			Log.d(TAG, "Connecting to broker: " + broker);
			mqttClient.connect(connOpts);
			Log.d(TAG, "Connected");
		} catch (MqttException e) {
			Log.d(TAG, "Error while connecting to broker", e);
		}
	}

	private void disconnect() {
		try {
			Log.d(TAG, "Disconnecting from broker: " + broker);
			mqttClient.disconnect();
			Log.d(TAG, "Disconnected");
		} catch (MqttException e) {
			Log.d(TAG, "Error while disconnecting from broker", e);
		}
	}

	private void sendMessage(String content) {
		try {
			Log.d(TAG, "Publishing message: " + content);
			MqttMessage message = new MqttMessage(content.getBytes());
			;
			message.setQos(qos);
			mqttClient.publish(topic, message);
			Log.d(TAG, "message published");
		} catch (MqttException e) {
			Log.d(TAG, "Error while publishing a message", e);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		switch (v.getId()) {
		case R.id.btnLeft:
			if (MotionEvent.ACTION_DOWN == event.getAction()) {
				sendMessage("l");
			} else if (MotionEvent.ACTION_UP == event.getAction()) {
				sendMessage("c");
			}
			break;
		case R.id.btnRight:
			if (MotionEvent.ACTION_DOWN == event.getAction()) {
				sendMessage("r");
			} else if (MotionEvent.ACTION_UP == event.getAction()) {
				sendMessage("c");
			}
			break;
		case R.id.btnForward:
			if (MotionEvent.ACTION_DOWN == event.getAction()) {
				sendMessage("f");
			} else if (MotionEvent.ACTION_UP == event.getAction()) {
				sendMessage("n");
			}
			break;
		case R.id.btnBackward:
			if (MotionEvent.ACTION_DOWN == event.getAction()) {
				sendMessage("b");
			} else if (MotionEvent.ACTION_UP == event.getAction()) {
				sendMessage("n");
			}
			break;
		default:
			break;
		}
		return false;
	}

}
