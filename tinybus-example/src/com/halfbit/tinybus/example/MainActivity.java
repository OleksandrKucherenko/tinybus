package com.halfbit.tinybus.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.halfbit.tinybus.Bus;
import com.halfbit.tinybus.Subscribe;
import com.halfbit.tinybus.TinyBus;
import com.halfbit.tinybus.wires.BatteryWire;
import com.halfbit.tinybus.wires.BatteryWire.BatteryLevelEvent;
import com.halfbit.tinybus.wires.BroadcastReceiverWire;
import com.halfbit.tinybus.wires.ConnectivityWire;
import com.halfbit.tinybus.wires.ConnectivityWire.ConnectionChangedEvent;
import com.halfbit.tinybus.wires.ShakeEventWire;
import com.halfbit.tinybus.wires.ShakeEventWire.ShakeEvent;

public class MainActivity extends Activity {

	private Bus mBus;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Create a bus and attach it to activity
		mBus = TinyBus.from(this)
			.wire(new ShakeEventWire())
			.wire(new ConnectivityWire())
			.wire(new BatteryWire())
			.wire(new BroadcastReceiverWire(
					Intent.ACTION_POWER_CONNECTED, 
					Intent.ACTION_POWER_DISCONNECTED));
	}

	@Override
	protected void onStart() {
		super.onStart();
		mBus.register(this);
	}
	
	@Override
	protected void onStop() {
		mBus.unregister(this);
		super.onStop();
	}

	@Subscribe
	public void onConnectivityEvent(ConnectionChangedEvent event) {
		if (event.isConnected()) {
			// check type
		}
	}

	@Subscribe
	public void onBatteryEvent(BatteryLevelEvent event) {
		Toast.makeText(this, "Battery: " + event.level + "%", Toast.LENGTH_SHORT).show();
	}
	
	@Subscribe
	public void onIntent(Intent intent) {
		final String action = intent.getAction();
		if (Intent.ACTION_POWER_CONNECTED.equals(action)) {
			Toast.makeText(this, "Power connected", Toast.LENGTH_SHORT).show();
			
		} else if (Intent.ACTION_POWER_DISCONNECTED.equals(action)) {
			Toast.makeText(this, "Power disconnected", Toast.LENGTH_SHORT).show();
			
		}
	}
	
	@Subscribe
	public void onShakeEvent(ShakeEvent event) {
		Toast.makeText(this, "~ Device has been shaken ~", Toast.LENGTH_SHORT).show();
	}
}
