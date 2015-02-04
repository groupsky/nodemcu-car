package org.hackafe.rccarcontrol;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by groupsky on 04.02.15.
 */
public class Car {

    private static final String TAG = "rc.car";
    final IMqttAsyncClient mqtt;
    final String topic;

    public Car(IMqttAsyncClient mqtt, String topic) {
        this.mqtt = mqtt;
        this.topic = topic;
    }

    public void left() {
        publish("l");
    }

    public void right() {
        publish("r");
    }

    public void forward() {
        publish("f");
    }

    public void reverse() {
        publish("b");
    }

    public void left(int power) {
        publish("l" + power);
    }

    public void right(int power) {
        publish("r");
    }

    public void forward(int power) {
        publish("f" + power);
    }

    public void reverse(int power) {
        publish("b" + power);
    }

    public void neutral() {
        publish("n");
    }

    public void central() {
        publish("c");
    }

    public void stop() {
        publish("s");
    }

    private void publish(String msg) {
        try {
            mqtt.publish(topic, msg.getBytes(), 0, false);
        } catch (MqttException e) {
            Log.e(TAG, "problem sending " + msg + " through MQTT", e);
            throw new RuntimeException(e);
        }
    }
}
