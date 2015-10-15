package com.example.krishna.bluetoothmanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AbsListView.MultiChoiceModeListener;

import com.example.krishna.bluetoothmanager.data.BluetoothDBHelper;
import com.example.krishna.bluetoothmanager.data.BluetoothDBUtils;

public class MainActivity extends AppCompatActivity {

    private static final int ADD_REQUEST_CODE = 1;
    private static final int MODIFY_REQUEST_CODE = 2;
    private BluetoothDeviceMediaPlayerPairAdapter mAdapter;
    private Cursor mCursor;
    private BluetoothDBHelper mDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NewBluetoothDevicePairActivity.class);
                startActivityForResult(intent, ADD_REQUEST_CODE);
            }
        });

        mDbHelper = new BluetoothDBHelper(this);
        mCursor = BluetoothDBUtils.getBluetoothMediaPairs(mDbHelper);
        mAdapter = new BluetoothDeviceMediaPlayerPairAdapter(this, mCursor, 0);
        final ListView listView = (ListView)findViewById(R.id.listview_bluetooth_media_pair);
        listView.setAdapter(mAdapter);


        AdapterView.OnItemClickListener listViewItemClickListener = new AdapterView.OnItemClickListener() {
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
        };
        listView.setOnItemClickListener(listViewItemClickListener);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                final int checkedCount = listView.getCheckedItemCount();
                mode.setTitle(checkedCount + " selected");
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.menu_main1, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        SparseBooleanArray itemsSelected = listView.getCheckedItemPositions();
                        for (int i = 0; i < listView.getCount(); i++) {
                            if (itemsSelected.get(i)) {
                                Cursor listItemCursor = (Cursor) mAdapter.getItem(i);
                                String bluetoothDeviceAddress = listItemCursor.getString(
                                        BluetoothDeviceMediaPlayerPairAdapter.COL_BLUETOOTH_DEVICE_ADDRESS);
                                BluetoothDBUtils.deleteBluetoothMediaPair(mDbHelper, bluetoothDeviceAddress);
                            }
                        }


                        mode.finish();
                        return true;

                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // Update list_view adapter
                mCursor = BluetoothDBUtils.getBluetoothMediaPairs(mDbHelper);
                mAdapter.swapCursor(mCursor);
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if((requestCode == ADD_REQUEST_CODE || requestCode == MODIFY_REQUEST_CODE)
                && resultCode == RESULT_OK)
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
        BluetoothDBHelper dbHelper = new BluetoothDBHelper(this);
        mCursor = BluetoothDBUtils.getBluetoothMediaPairs(dbHelper);
        mAdapter.swapCursor(mCursor);
        mAdapter.notifyDataSetChanged();
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
