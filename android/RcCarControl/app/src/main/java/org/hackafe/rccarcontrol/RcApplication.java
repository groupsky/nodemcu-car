package org.hackafe.rccarcontrol;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.UUID;

/**
 * Created by groupsky on 04.02.15.
 */
public class RcApplication extends Application implements IMqttActionListener, Application.ActivityLifecycleCallbacks {

    private static final String TAG = "rc";
    private static final String SERVER_URI = "tcp://178.62.67.131:1883";
    private static final String USERNAME = "mosquitto";
    private static final String PASSWORD = "";
//    private static final String SERVER_URI = "tcp://m20.cloudmqtt.com:18860";
//    private static final String USERNAME = "remote";
//    private static final String PASSWORD = "hackafe";
    private static final String CLIENT_ID = "android-rc." + UUID.randomUUID().toString();
    private static final String TOPIC = "/hackafe-car";
    public static MqttAndroidClient MQTT;
    public static Car CAR;

    int created = 0;
    int started = 0;
    int resumed = 0;

    MqttConnectOptions mqttOptions;

    @Override
    public void onCreate() {
        super.onCreate();

        mqttOptions = new MqttConnectOptions();
        mqttOptions.setUserName(USERNAME);
        mqttOptions.setPassword(PASSWORD.toCharArray());

        MQTT = new MqttAndroidClient(this, SERVER_URI, CLIENT_ID);
        CAR = new Car(MQTT, TOPIC);

        registerActivityLifecycleCallbacks(this);
    }

    void logStats() {
        Log.d(TAG, "stats: " + created + "/" + started + "/" + resumed);
    }


    private void connect() {
        if (MQTT.isConnected()) return;
        try {
            MQTT.connect(mqttOptions, null, this);
        } catch (MqttException e) {
            Log.e(TAG, "Can't connect to MQTT broker", e);
            throw new RuntimeException(e);
        }
    }

    private void disconnect() {
        if (!MQTT.isConnected()) return;
        try {
            MQTT.disconnect();
        } catch (MqttException e) {
            Log.e(TAG, "Can't disconnect from MQTT broker", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onSuccess(IMqttToken iMqttToken) {
        Log.d(TAG, "success " + iMqttToken);
    }

    @Override
    public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
        Log.w(TAG, "failure " + iMqttToken, throwable);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        created++;
        logStats();
    }

    @Override
    public void onActivityStarted(Activity activity) {
        started++;
        logStats();
        connect();
    }

    @Override
    public void onActivityResumed(Activity activity) {
        resumed++;
        logStats();
    }

    @Override
    public void onActivityPaused(Activity activity) {
        resumed--;
        logStats();
    }

    @Override
    public void onActivityStopped(Activity activity) {
        started--;
        logStats();
        if (started <= 0)
            disconnect();
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        created--;
        logStats();
    }
}
