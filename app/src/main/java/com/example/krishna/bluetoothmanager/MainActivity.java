package com.example.krishna.bluetoothmanager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;
    private Spinner bluetoothSpinner;
    private Spinner audioPlayerSpinner;
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

        //create bluetooth adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedBluetoothDevices = bluetoothAdapter.getBondedDevices();

        bluetoothSpinner = (Spinner)findViewById(R.id.bluetooth_device_spinner);

        populateBluetoothDeviceNames(pairedBluetoothDevices);

        audioPlayerSpinner = (Spinner)findViewById(R.id.audio_player_spinner);
        populateMusicPlayers();
    }

    private void populateBluetoothDeviceNames(Set<BluetoothDevice> pairedBluetoothDevices) {
        if(bluetoothSpinner == null)
        {
            return;
        }

        ArrayList<String> pairedDevicesArray = new ArrayList<>();
        for(BluetoothDevice pairedDevice : pairedBluetoothDevices)
        {
            pairedDevicesArray.add(pairedDevice.getName());
        }


        ArrayAdapter<String> spinnerArrayAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, pairedDevicesArray);
        bluetoothSpinner.setAdapter(spinnerArrayAdapter);
    }

    private void populateMusicPlayers()
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, "1");
//        Uri uri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, "1");
//        intent.setData(uri);
//        intent.setType("audio/*");
        intent.setDataAndType(uri, "audio/*");
        List<ResolveInfo> audioApps = getPackageManager().queryIntentActivities(intent, 0);
        ArrayList<String> audioAppNames = new ArrayList<>();
        for (ResolveInfo rInfo : audioApps) {
            audioAppNames.add(rInfo.loadLabel(getPackageManager()).toString());
        }

        ArrayAdapter<String> audioArrayAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, audioAppNames);
        audioPlayerSpinner.setAdapter(audioArrayAdapter);
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
