package com.example.krishna.bluetoothmanager;

import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.Toast;

public class BluetoothConnectionReceiver extends BroadcastReceiver {
    public BluetoothConnectionReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = "android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED";
        String saavnPackageName = "com.saavn.android";

        if(action.equals(intent.getAction()))
        {
            if(intent.getIntExtra("android.bluetooth.adapter.extra.CONNECTION_STATE", 0) == BluetoothAdapter.STATE_CONNECTED) {
                Toast.makeText(context, "Starting saavn app", Toast.LENGTH_SHORT).show();
                openMusicPlayer(context, saavnPackageName);
                setMaxVolume(context);
            }
            else if(intent.getIntExtra("android.bluetooth.adapter.extra.PREVIOUS_CONNECTION_STATE", 0) == BluetoothAdapter.STATE_CONNECTED) {
                setMinVolume(context);
            }
        }

    }

    private void openMusicPlayer(Context context, String packageName)
    {
        Intent intentMusic = context.getPackageManager().getLaunchIntentForPackage(packageName);
        intentMusic.addCategory(Intent.CATEGORY_LAUNCHER);
        intentMusic.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentMusic);
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
