package com.example.krishna.bluetoothmanager;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.krishna.bluetoothmanager.data.object.BluetoothDev;
import com.example.krishna.bluetoothmanager.data.object.DeviceMusicPlayerPair;
import com.example.krishna.bluetoothmanager.data.object.MusicPlayer;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public  static  final String PREF_FILE_NAME = "BLUETOOTH_MANAGER_SHARED_PREFS";
    public static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter bluetoothAdapter;
    private Spinner bluetoothSpinner;
    private Spinner audioPlayerSpinner;
    private TextView bluetoothSelectMsg;
    private List<MusicPlayer> musicPlayers;
    private List<BluetoothDev> pairedDevices;
    private boolean isBluetoothPermissionDenied;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.setVisibility(View.INVISIBLE);

        bluetoothSelectMsg = (TextView)findViewById(R.id.bluetoothSelectLabel);
        //create bluetooth adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothSpinner = (Spinner) findViewById(R.id.bluetooth_device_spinner);
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
            populateBluetoothDeviceNames(pairedBluetoothDevices);
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
            populateBluetoothDeviceNames(pairedBluetoothDevices);
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void populateBluetoothDeviceNames(Set<BluetoothDevice> pairedBluetoothDevices) {
        if(bluetoothSpinner == null)
        {
            return;
        }

        pairedDevices = new ArrayList<>();
        for(BluetoothDevice pairedDevice : pairedBluetoothDevices)
        {
//            switch (deviceClass)
//            {
//                case BluetoothClass.Device.AUDIO_VIDEO_HANDSFREE:
//
//                    break;
//                case BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET:
//                    break;
//                case BluetoothClass.Device.WEARABLE_WRIST_WATCH:
//                    break;
//            }

            BluetoothDev device = new BluetoothDev(pairedDevice.getName(),
                    pairedDevice.getBluetoothClass().getDeviceClass());
            pairedDevices.add(device);
        }
    }

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



            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(musicPlayerPackageName, PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES);

                ActivityInfo[] activites = packageInfo.activities;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            MusicPlayer musicPlayer = new MusicPlayer(musicPlayerName, musicPlayerPackageName, drawable);
            musicPlayers.add(musicPlayer);
        }

        Collections.sort(musicPlayers, new Comparator<MusicPlayer>() {
            @Override
            public int compare(MusicPlayer lhs, MusicPlayer rhs) {
                return lhs.toString().compareTo(rhs.toString());
            }
        });

//        ArrayAdapter<MusicPlayer> audioArrayAdapter =
//                new ArrayAdapter<MusicPlayer>(this, android.R.layout.simple_spinner_dropdown_item, musicPlayers);
//        audioPlayerSpinner.setAdapter(audioArrayAdapter);
    }

    public void pairMusicPlayerWithBluetoothDevice(View view)
    {
        String selectedBluetoothDevice = (String)bluetoothSpinner.getSelectedItem();
        MusicPlayer selectedMusicPlayer = (MusicPlayer)audioPlayerSpinner.getSelectedItem();

        DeviceMusicPlayerPair deviceMusicPlayerPair = new DeviceMusicPlayerPair(selectedBluetoothDevice, selectedMusicPlayer);

        SharedPreferences sharedPref = getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        //TODO try to save all the object using JSON
        //Reference: http://stackoverflow.com/questions/7145606/how-android-sharedpreferences-save-store-object
        editor.putString(selectedBluetoothDevice, selectedMusicPlayer.getPackageName());
        editor.commit();

        Snackbar.make(view, selectedBluetoothDevice + " is paired with " + selectedMusicPlayer.toString(), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
