package com.example.krishna.bluetoothmanager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.Set;

public class NewBluetoothDevicePairActivity extends AppCompatActivity {

    public  static  final String PREF_FILE_NAME = "BLUETOOTH_MANAGER_SHARED_PREFS";
    public static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter bluetoothAdapter;
    private Spinner bluetoothSpinner;
    private Spinner audioPlayerSpinner;
    private TextView bluetoothSelectMsg;
    private SeekBar mVolumeSeekBar;
    private List<MusicPlayer> musicPlayers;
    private List<BluetoothDev> pairedDevices;
    private List<BluetoothDev> mStoredBluetoothDevices;
    private boolean isBluetoothPermissionDenied;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_bluetooth_device_pair);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_new);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bluetoothSelectMsg = (TextView)findViewById(R.id.bluetoothSelectLabel);
        //create bluetooth adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //TODO Check if bluetooth adapter is null and handle the cases for the devices that don't have bluetooth
        bluetoothSpinner = (Spinner) findViewById(R.id.bluetooth_device_spinner);

        mStoredBluetoothDevices = getStoredBluetoothDevicesFromCursor( );
        if(bluetoothAdapter.isEnabled() == false && isBluetoothPermissionDenied == false)
        {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        else if(isBluetoothPermissionDenied)
        {
            bluetoothSelectMsg.setText("Please enable bluetooth to see the devices paired.");
            bluetoothSpinner.setVisibility(View.INVISIBLE);
        }
        else {
            bluetoothSelectMsg.setText("Select your bluetooth device");
            Set<BluetoothDevice> pairedBluetoothDevices = bluetoothAdapter.getBondedDevices();
            populateBluetoothDevices(pairedBluetoothDevices);
            BluetoothDeviceAdapter spinnerArrayAdapter =
                    new BluetoothDeviceAdapter(this, android.R.layout.simple_spinner_dropdown_item, pairedDevices);
            bluetoothSpinner.setAdapter(spinnerArrayAdapter);
        }

        audioPlayerSpinner = (Spinner)findViewById(R.id.audio_player_spinner);
        populateMusicPlayers();
        MusicPlayerAdapter audioArrayAdapter =
                new MusicPlayerAdapter(this, R.layout.spinner_item, musicPlayers);
        audioArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        audioPlayerSpinner.setAdapter(audioArrayAdapter);

        mVolumeSeekBar = (SeekBar)findViewById(R.id.volume_seek_bar);

    }

    private List<BluetoothDev> getStoredBluetoothDevicesFromCursor( ) {
        List<BluetoothDev> storedBluetoothDevices = new ArrayList<>();

        BluetoothDBHelper dbHelper = new BluetoothDBHelper(this);
        Cursor cursor = BluetoothDBUtils.getBluetoothMediaPairs(dbHelper);

        while(cursor.moveToNext())
        {
            String bluetoothDeviceAddress = cursor.getString(
                    BluetoothDeviceMediaPlayerPairAdapter.COL_BLUETOOTH_DEVICE_ADDRESS);
            String bluetoothDeviceName = cursor.getString(
                    BluetoothDeviceMediaPlayerPairAdapter.COL_BLUETOOTH_DEVICE_NAME);
            int bluetoothDeviceType = cursor.getInt(
                    BluetoothDeviceMediaPlayerPairAdapter.COL_BLUETOOTH_DEVICE_TYPE);

            BluetoothDev storeBluetoothDevice = new BluetoothDev(
                    bluetoothDeviceAddress, bluetoothDeviceName, bluetoothDeviceType);
            storedBluetoothDevices.add(storeBluetoothDevice);
        }


        return storedBluetoothDevices;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode != REQUEST_ENABLE_BT)
        {
            return;
        }

        if(resultCode == RESULT_OK)
        {
            bluetoothSelectMsg.setText("Select your bluetooth device");
            Set<BluetoothDevice> pairedBluetoothDevices = bluetoothAdapter.getBondedDevices();
            populateBluetoothDevices(pairedBluetoothDevices);
            BluetoothDeviceAdapter spinnerArrayAdapter =
                    new BluetoothDeviceAdapter(this, android.R.layout.simple_spinner_dropdown_item, pairedDevices);
            bluetoothSpinner.setAdapter(spinnerArrayAdapter);
        }
        else if(resultCode == RESULT_CANCELED)
        {
            bluetoothSelectMsg.setText("Please enable bluetooth to see the devices paired.");
            bluetoothSpinner.setVisibility(View.INVISIBLE);
            isBluetoothPermissionDenied = true;
        }

    }

    /**
     * Populates paired bluetooth devices
     * @param pairedBluetoothDevices
     */
    private void populateBluetoothDevices(Set<BluetoothDevice> pairedBluetoothDevices) {
        if(bluetoothSpinner == null)
        {
            return;
        }

        pairedDevices = new ArrayList<>();

        BluetoothDBHelper dbHelper = new BluetoothDBHelper(this);
        Cursor cursor = BluetoothDBUtils.getBluetoothMediaPairs(dbHelper);
        for(BluetoothDevice pairedDevice : pairedBluetoothDevices)
        {
            BluetoothDev device = new BluetoothDev(pairedDevice.getAddress(), pairedDevice.getName(),
                    pairedDevice.getBluetoothClass().getDeviceClass());

            if(mStoredBluetoothDevices.contains(device))
            {
                continue;
            }

            pairedDevices.add(device);

        }
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

    /**
     * Pairs music player with bluetooth device
     * @param view
     */
    public void pairMusicPlayerWithBluetoothDevice(View view)
    {
        BluetoothDev selectedBluetoothDevice = (BluetoothDev)bluetoothSpinner.getSelectedItem();
        MusicPlayer selectedMusicPlayer = (MusicPlayer)audioPlayerSpinner.getSelectedItem();

        // Update the volume of the music player.
        selectedMusicPlayer.setPlayerVolume(mVolumeSeekBar.getProgress());

        DeviceMusicPlayerPair deviceMusicPlayerPair = new DeviceMusicPlayerPair(selectedBluetoothDevice, selectedMusicPlayer);

        BluetoothDBHelper dbHelper = new BluetoothDBHelper(this);
        long result = BluetoothDBUtils.insertBluetoothMediaPair(dbHelper, deviceMusicPlayerPair);

        if(result > 0)
        {
            Toast.makeText(this, selectedBluetoothDevice.getDeviceName()
                    + " is paired with " + selectedMusicPlayer.toString(), Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK, getIntent());
            finish();
        }
        else {
            Toast.makeText(this, "Error occurred while adding your " +
                    "media player preference.", Toast.LENGTH_SHORT).show();
        }
    }

}
