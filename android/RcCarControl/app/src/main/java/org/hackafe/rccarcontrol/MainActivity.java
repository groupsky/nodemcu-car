package org.hackafe.rccarcontrol;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((Button)findViewById(R.id.simple_rc_btn)).setOnClickListener(this);
        ((Button)findViewById(R.id.analog_rc_btn)).setOnClickListener(this);
        ((Button)findViewById(R.id.accelerometer_rc_btn)).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.simple_rc_btn:
                startActivity(new Intent(this, SimpleRcActivity.class));
                break;
            case R.id.analog_rc_btn:
                startActivity(new Intent(this, AnalogRcActivity.class));
                break;
            case R.id.accelerometer_rc_btn:
                startActivity(new Intent(this, AccelerometerRcActivity.class));
                break;
        }
    }
}
