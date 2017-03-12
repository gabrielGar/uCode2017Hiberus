package com.ubeacon.rts.ubeacon;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.ubeacon.rts.R;

import java.util.ArrayList;
import java.util.List;

import ibeacon.IBeaconIntentProcessor;
import ibeacon.IBeaconManager;
import ibeacon.service.IBeaconService;


/**
 * 
 * @author dyoung
 * 
 */
public class MainActivity extends Activity {
	protected static final String TAG = "MainActivity";
	public boolean arePermissionGranted;
	public static final int MY_PERMISSION_REQUEST = 5;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "oncreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		verifyBluetooth();
		if (android.os.Build.VERSION.SDK_INT < 23) {
			arePermissionGranted = true;
		} else {
            Log.i("PERMISOS","PERMISOOOOOOOOS");
			List<String> permissionsList = new ArrayList<>();

			if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
				permissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
			if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
				permissionsList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
			if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED)
				permissionsList.add(Manifest.permission.BLUETOOTH);
			if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED)
				permissionsList.add(Manifest.permission.BLUETOOTH_ADMIN);
			if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
				permissionsList.add(Manifest.permission.INTERNET);
			if (ContextCompat.checkSelfPermission(this, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED)
				permissionsList.add(Manifest.permission.VIBRATE);
			if (permissionsList.size() > 0)
				ActivityCompat.requestPermissions(this, permissionsList.toArray(new String[permissionsList.size()]), MY_PERMISSION_REQUEST);
			else {
				arePermissionGranted = true;
				//call load beacon scan
			}

		}

	}


	public void launchWebView(View view){
		Intent intt = new Intent(this,WebViewInfo.class);
		intt.putExtra("html","<html><p>Hoooooola</p></html>");
		this.startActivity(intt);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

		boolean tmpPermissionGranted = true;
		switch (requestCode) {
			case MY_PERMISSION_REQUEST:
				if (grantResults.length > 0) {
					for (int permission : grantResults) {
						if (permission != PackageManager.PERMISSION_GRANTED) {
							tmpPermissionGranted = false;
							break;
						}
					}
				} else {
					//  Denied
					tmpPermissionGranted = false;
                }

				if (!tmpPermissionGranted) {
					final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
					builder.setTitle("Permission");
					builder.setMessage("This permission is required to use the Social Retail SDK.");
					builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					});
					builder.setCancelable(false);
					builder.show();
				} else {
					//call load beacon
					arePermissionGranted = true;
				}
				break;
			default:
		}
	}

	public void onMonitoringClicked(View view) {
		Intent myIntent = new Intent(this, MonitoringActivity.class);
		this.startActivity(myIntent);
	}

	private void verifyBluetooth() {

		try {
			if (!IBeaconManager.getInstanceForApplication(this).checkAvailability()) {
				final AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Bluetooth not enabled");
				builder.setMessage("Please enable bluetooth in settings and restart this application.");
				builder.setPositiveButton(android.R.string.ok, null);
				builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface dialog) {
						finish();
						System.exit(0);
					}

				});
				builder.show();

			}
		} catch (RuntimeException e) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Bluetooth LE not available");
			builder.setMessage("Sorry, this device does not support Bluetooth LE.");
			builder.setPositiveButton(android.R.string.ok, null);
			builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					finish();
					System.exit(0);
				}

			});
			builder.show();

		}

	}

}
