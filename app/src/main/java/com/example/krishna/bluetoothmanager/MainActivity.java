package com.example.krishna.bluetoothmanager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AbsListView.MultiChoiceModeListener;

import com.example.krishna.bluetoothmanager.data.BluetoothDbHelper;
import com.example.krishna.bluetoothmanager.data.BluetoothDBUtils;

public class MainActivity extends AppCompatActivity {

    private static final int ADD_REQUEST_CODE = 1;
    private static final int MODIFY_REQUEST_CODE = 2;
    private BluetoothDeviceMediaPlayerPairAdapter mAdapter;
    private Cursor mCursor;
    private BluetoothDbHelper mDbHelper;
    private RecyclerView mRecyclerView;
    private ImageView mBluetoothIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final Activity activity = this;

        mDbHelper = new BluetoothDbHelper(this);
        mCursor = BluetoothDBUtils.getBluetoothMediaPairs(mDbHelper);
        mAdapter = new BluetoothDeviceMediaPlayerPairAdapter(this, new BluetoothDeviceMediaPlayerPairAdapter.BluetoothDeviceAdapterOnclickListener() {
            @Override
            public void onClick(BluetoothDeviceMediaPlayerPairAdapter.ViewHolder viewHolder, Cursor cursor) {

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
                    intent.putExtra("MediaPlayerVolume",
                            cursor.getInt(BluetoothDeviceMediaPlayerPairAdapter.COL_MEDIA_PLAYER_VOLUME));
                    ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            activity, viewHolder.mBluetoothDeviceIcon, getString(R.string.bluetooth_device_icon_transition_name)
                    );
                    ActivityCompat.startActivityForResult(MainActivity.this, intent, MODIFY_REQUEST_CODE, activityOptions.toBundle());
                }
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_bluetooth_media_pair);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);


        // init swipe to dismiss logic
        ItemTouchHelper swipeToDismissTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // callback for drag-n-drop, false to skip this feature
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // callback for swipe to dismiss, removing item from data and adapter

                if(direction != ItemTouchHelper.RIGHT)
                {
                    return;
                }

                Cursor listItemCursor = mAdapter.getCursor();
                listItemCursor.moveToPosition(viewHolder.getAdapterPosition());

                String bluetoothDeviceAddress = listItemCursor.getString(
                        BluetoothDeviceMediaPlayerPairAdapter.COL_BLUETOOTH_DEVICE_ADDRESS);
                BluetoothDBUtils.deleteBluetoothMediaPair(mDbHelper, bluetoothDeviceAddress);

                mCursor = BluetoothDBUtils.getBluetoothMediaPairs(mDbHelper);
                mAdapter.swapCursor(mCursor);
                mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        });
        swipeToDismissTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void launchPairNewDeviceIntent(View view)
    {
        Intent intent = new Intent(this, NewBluetoothDevicePairActivity.class);
        ActivityOptionsCompat activityOptions =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this);

        ActivityCompat.startActivityForResult(this, intent, ADD_REQUEST_CODE, activityOptions.toBundle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if((requestCode == ADD_REQUEST_CODE || requestCode == MODIFY_REQUEST_CODE)
                && resultCode == RESULT_OK)
        {
            mCursor = BluetoothDBUtils.getBluetoothMediaPairs(mDbHelper);
            mAdapter.swapCursor(mCursor);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCursor = BluetoothDBUtils.getBluetoothMediaPairs(mDbHelper);
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
