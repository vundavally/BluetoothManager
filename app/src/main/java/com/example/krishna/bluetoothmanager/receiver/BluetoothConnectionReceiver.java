package com.example.krishna.bluetoothmanager.receiver;

import android.app.ActivityManager;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.krishna.bluetoothmanager.BluetoothDeviceMediaPlayerPairAdapter;
import com.example.krishna.bluetoothmanager.data.BluetoothDBHelper;
import com.example.krishna.bluetoothmanager.data.BluetoothDBUtils;
import com.example.krishna.bluetoothmanager.data.object.MusicPlayer;

import java.util.ArrayList;

public class BluetoothConnectionReceiver extends BroadcastReceiver {
    private static final String PREF_FILE_NAME = "BLUETOOTH_MANAGER_SHARED_PREFS";
    private static final String NOT_FOUND = "NOT_FOUND";
    private static final String LOG_TAG = "ConnectionReceiverLog";

    public BluetoothConnectionReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED;
        if(action.equals(intent.getAction()))
        {
            if(intent.getIntExtra(BluetoothProfile.EXTRA_STATE, 0) == BluetoothProfile.STATE_CONNECTED)
            {
                Log.v(LOG_TAG, "A2dp connected.");
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                MusicPlayer mediaPlayer = findMusicPlayerForBluetoothDevice(context, bluetoothDevice);

                if(mediaPlayer == null) {
                    Toast.makeText(context, "Music player not paired for this device.", Toast.LENGTH_LONG).show();
                    return;
                }

                openMusicPlayer(context, mediaPlayer.getPackageName());
                setMusicPlayerVolume(context, mediaPlayer.getPlayerVolume());
            }else if(intent.getIntExtra(BluetoothProfile.EXTRA_PREVIOUS_STATE, 0) == BluetoothAdapter.STATE_CONNECTED) {
//                setMinVolume(context);
            }
        }
    }

    private MusicPlayer findMusicPlayerForBluetoothDevice(Context context, BluetoothDevice bluetoothDevice) {

        BluetoothDBHelper dbHelper = new BluetoothDBHelper(context);
        Cursor cursor = BluetoothDBUtils.getMediaPlayerByDeviceAddress(dbHelper, bluetoothDevice.getAddress());
        if(cursor.moveToFirst()) {
            String mediaPlayerPackage = cursor.getString(
                    BluetoothDeviceMediaPlayerPairAdapter.COL_MEDIA_PLAYER_PACKAGE_NAME);
            String mediaPlayerName = cursor.getString(
                    BluetoothDeviceMediaPlayerPairAdapter.COL_MEDIA_PLAYER_NAME);
            int mediaPlayerVolume = cursor.getInt(
                    BluetoothDeviceMediaPlayerPairAdapter.COL_MEDIA_PLAYER_VOLUME);

            return new MusicPlayer(mediaPlayerName, mediaPlayerPackage, mediaPlayerVolume);
        } else
        {
            return null;
        }
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
            context.startActivity(intentMusic, null);
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

    public void setMusicPlayerVolume(Context context, int musicPlayerVolume) {
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volumeControlPercent = (musicPlayerVolume / 100);
        int maxProgressBarVolume = 15;
        int currentVolumeToSet = (int) ((maxVolume * musicPlayerVolume)/maxProgressBarVolume);
        Log.v("TELL_ME_VOLUME", "Passed in Volume: " + String.valueOf(musicPlayerVolume));
        Log.v("TELL_ME_VOLUME", "Stream Max Volume: " + String.valueOf(maxVolume));
        Log.v("TELL_ME_VOLUME", "Current Volume: " + String.valueOf(currentVolumeToSet));
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolumeToSet, AudioManager.FLAG_SHOW_UI);
    }

    public void setMinVolume(Context context) {
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_SHOW_UI);
    }
}
