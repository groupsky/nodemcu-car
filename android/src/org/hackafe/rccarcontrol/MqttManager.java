package org.hackafe.rccarcontrol;

import java.util.UUID;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import android.util.Log;

public class MqttManager {

	private static final String TAG = "MqttManager";
	
	String topic = "/hackafe-car";
	String content = "r";
	int qos = 0;
	String broker = "tcp://broker.mqtt-dashboard.com:1883";
	String clientId = UUID.randomUUID().toString();
	MemoryPersistence persistence = new MemoryPersistence();

	MqttClient mqttClient;
	
	public void connect() {
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

	public void disconnect() {
		try {
			Log.d(TAG, "Disconnecting from broker: " + broker);
			mqttClient.disconnect();
			Log.d(TAG, "Disconnected");
		} catch (MqttException e) {
			Log.d(TAG, "Error while disconnecting from broker", e);
		}
	}

	public void sendMessage(String content) {
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
}
