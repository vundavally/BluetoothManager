package com.example.krishna.bluetoothmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.krishna.bluetoothmanager.data.BluetoothDBHelper;
import com.example.krishna.bluetoothmanager.data.BluetoothDBUtils;
import com.example.krishna.bluetoothmanager.data.object.BluetoothDev;
import com.example.krishna.bluetoothmanager.data.object.DeviceMusicPlayerPair;
import com.example.krishna.bluetoothmanager.data.object.MusicPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ModifyBluetoothDevicePairActivity extends AppCompatActivity {

    private Spinner bluetoothSpinner;
    private Spinner audioPlayerSpinner;
    private SeekBar mVolumeSeekBar;
    private List<MusicPlayer> musicPlayers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_bluetooth_device_pair);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent modifyIntent = getIntent();
        String bluetoothDeviceName = modifyIntent.getStringExtra("BluetoothDeviceName");
        String bluetoothDeviceAddress = modifyIntent.getStringExtra("BluetoothDeviceAddress");
        int bluetoothDeviceType = modifyIntent.getIntExtra("BluetoothDeviceType", -1);
        String mediaPlayerName = modifyIntent.getStringExtra("MediaPlayerName");
        String mediaPlayerPackage = modifyIntent.getStringExtra("MediaPlayerPackageName");
        int mediaPlayerVolume = modifyIntent.getIntExtra("MediaPlayerVolume", 7);
        Drawable mediaPlayerDrawable = null;
        try {
            mediaPlayerDrawable = getPackageManager().getApplicationIcon(mediaPlayerPackage);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        List<BluetoothDev> pairedDevices = new ArrayList<>();
        pairedDevices.add(new BluetoothDev(bluetoothDeviceAddress, bluetoothDeviceName, bluetoothDeviceType));
        bluetoothSpinner = (Spinner) findViewById(R.id.bluetooth_device_spinner);
        BluetoothDeviceAdapter spinnerArrayAdapter =
                new BluetoothDeviceAdapter(this, android.R.layout.simple_spinner_dropdown_item, pairedDevices);
        bluetoothSpinner.setAdapter(spinnerArrayAdapter);
        bluetoothSpinner.setEnabled(false);

        audioPlayerSpinner = (Spinner)findViewById(R.id.audio_player_spinner);
        populateMusicPlayers();
        MusicPlayerAdapter audioArrayAdapter =
                new MusicPlayerAdapter(this, R.layout.spinner_item, musicPlayers);
        audioArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        audioPlayerSpinner.setAdapter(audioArrayAdapter);
        if(mediaPlayerDrawable != null) {
            int position = audioArrayAdapter.getPosition(
                    new MusicPlayer(mediaPlayerName, mediaPlayerPackage, mediaPlayerDrawable));
            audioPlayerSpinner.setSelection(position);
        }

        mVolumeSeekBar = (SeekBar) findViewById(R.id.volume_seek_bar);
        mVolumeSeekBar.setProgress(mediaPlayerVolume);
    }

    /**
     * Populates music players
     */
    private void populateMusicPlayers()
    {
        PackageManager packageManager = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        List<ResolveInfo> musicPlayersInfo = packageManager.queryBroadcastReceivers(intent, 0);

        musicPlayers = new ArrayList<>();
        for (ResolveInfo musicPlayerInfo : musicPlayersInfo) {

            String musicPlayerName = musicPlayerInfo.loadLabel(packageManager).toString();
            String musicPlayerPackageName = musicPlayerInfo.activityInfo.packageName;
            Drawable drawable = musicPlayerInfo.loadIcon(packageManager);

            MusicPlayer musicPlayer = new MusicPlayer(musicPlayerName, musicPlayerPackageName, drawable);
            musicPlayers.add(musicPlayer);
        }

        Collections.sort(musicPlayers, new Comparator<MusicPlayer>() {
            @Override
            public int compare(MusicPlayer lhs, MusicPlayer rhs) {
                return lhs.toString().compareTo(rhs.toString());
            }
        });
    }

    public void updateMediaPlayerForCurrentBluetoothDevice(View view)
    {
        BluetoothDev selectedBluetoothDevice = (BluetoothDev)bluetoothSpinner.getSelectedItem();
        MusicPlayer selectedMusicPlayer = (MusicPlayer)audioPlayerSpinner.getSelectedItem();
        // Update music player volume
        selectedMusicPlayer.setPlayerVolume(mVolumeSeekBar.getProgress());

        DeviceMusicPlayerPair deviceMusicPlayerPair = new DeviceMusicPlayerPair(selectedBluetoothDevice, selectedMusicPlayer);

        BluetoothDBHelper dbHelper = new BluetoothDBHelper(this);
        long numberOfRowsAffected = BluetoothDBUtils.updateBluetoothMediaPair(dbHelper, deviceMusicPlayerPair);

        if(numberOfRowsAffected > 0) {
            Toast.makeText(this, selectedBluetoothDevice.getDeviceName()
                    + " is paired with " + selectedMusicPlayer.toString(), Toast.LENGTH_SHORT).show();
            getIntent().putExtra("UpdatedRows", numberOfRowsAffected);
            setResult(RESULT_OK, getIntent());
            finish();
        } else
        {
            Toast.makeText(this, "Error occurred while updating your " +
                    "media player preference.", Toast.LENGTH_SHORT).show();
        }
    }

}
