package com.example.krishna.bluetoothmanager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity {

    private static final int MODIFY_REQUEST_CODE = 1;
    private BluetoothDeviceMediaPlayerPairAdapter mAdapter;
    private Cursor mCursor;
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
                Intent intent = new Intent(getApplicationContext(), PairNewBluetoothDeviceActivity.class);
                startActivity(intent);
            }
        });

        BluetoothDBHelper dbHelper = new BluetoothDBHelper(this);
        mCursor = BluetoothDBUtils.getBluetoothMediaPairs(dbHelper);
        mAdapter = new BluetoothDeviceMediaPlayerPairAdapter(this, mCursor, 0);
        ListView listView = (ListView)findViewById(R.id.listview_bluetooth_media_pair);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Cursor cursor = (Cursor)adapterView.getItemAtPosition(position);
                if(cursor != null)
                {
                    Intent intent = new Intent(getApplicationContext(), ModifyBluetoothDevicePairActivity.class);
                    intent.putExtra("BluetoothDeviceName",
                            cursor.getString(BluetoothDeviceMediaPlayerPairAdapter.COL_BLUETOOTH_DEVICE_NAME));
                    intent.putExtra("BluetoothDeviceAddress",
                            cursor.getString(BluetoothDeviceMediaPlayerPairAdapter.COL_BLUETOOTH_DEVICE_ADDRESS));
                    intent.putExtra("BluetoothDeviceType",
                            cursor.getInt(BluetoothDeviceMediaPlayerPairAdapter.COL_BLUETOOTH_DEVICE_TYPE));
                    intent.putExtra("MediaPlayerName",
                            cursor.getString(BluetoothDeviceMediaPlayerPairAdapter.COL_MEDIA_PLAYER_NAME));
                    intent.putExtra("MediaPlayerPackageName",
                            cursor.getString(BluetoothDeviceMediaPlayerPairAdapter.COL_MEDIA_PLAYER_PACKAGE_NAME));
                    startActivityForResult(intent, MODIFY_REQUEST_CODE);
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MODIFY_REQUEST_CODE && resultCode == RESULT_OK)
        {
            BluetoothDBHelper dbHelper = new BluetoothDBHelper(this);
            mCursor = BluetoothDBUtils.getBluetoothMediaPairs(dbHelper);
            mAdapter.swapCursor(mCursor);
            mAdapter.notifyDataSetChanged();
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
