package com.example.krishna.bluetoothmanager.receiver;

import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.widget.Toast;

import java.util.ArrayList;

public class BluetoothConnectionReceiver extends BroadcastReceiver {
    private static final String PREF_FILE_NAME = "BLUETOOTH_MANAGER_SHARED_PREFS";
    private static final String NOT_FOUND = "NOT_FOUND";

    public BluetoothConnectionReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = "android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED";

        if(action.equals(intent.getAction()))
        {
            if(intent.getIntExtra("android.bluetooth.adapter.extra.CONNECTION_STATE", 0) == BluetoothAdapter.STATE_CONNECTED) {
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String packageName = findMusicPlayerPackageNameForBluetoothDevice(context, bluetoothDevice);

                if(packageName.equals(NOT_FOUND)) {
                    Toast.makeText(context, "Music player not paired for this device.", Toast.LENGTH_LONG).show();
                    return;
                }

                openMusicPlayer(context, packageName);
                setMaxVolume(context);
            }
            else if(intent.getIntExtra("android.bluetooth.adapter.extra.PREVIOUS_CONNECTION_STATE", 0) == BluetoothAdapter.STATE_CONNECTED) {
                setMinVolume(context);
            }

        }

    }

    private String findMusicPlayerPackageNameForBluetoothDevice(Context context, BluetoothDevice bluetoothDevice) {

        String bluetoothDeviceName = bluetoothDevice.getName();
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPref.getString(bluetoothDeviceName, NOT_FOUND);
    }

    private void openMusicPlayer(Context context, String packageName)
    {
        Intent intentMusic = context.getPackageManager().getLaunchIntentForPackage(packageName);

        if(intentMusic == null)
        {
            Toast.makeText(context, "Can not start the selected application. Please choose a different application.", Toast.LENGTH_LONG).show();
            return;
        }

        intentMusic.addCategory(Intent.CATEGORY_LAUNCHER);
        intentMusic.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(context.getPackageManager().resolveActivity(intentMusic, PackageManager.MATCH_ALL) != null) {
            context.startActivity(intentMusic);
        }
        else
        {
            Toast.makeText(context, "Can not start the selected application. Please choose a different application.", Toast.LENGTH_LONG).show();
        }
    }

    private void stopMusicPlayer(Context context, String packageName)
    {
        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.killBackgroundProcesses(packageName);
    }

    public void setMaxVolume(Context context) {
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), AudioManager.FLAG_SHOW_UI);
    }

    public void setMinVolume(Context context) {
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_SHOW_UI);
    }
}
