package com.example.krishna.bluetoothmanager;

import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Krishna on 10/13/2015.
 */
public class BluetoothDeviceMediaPlayerPairAdapter extends CursorAdapter {
    public static final int COL_ID = 0;
    public static final int COL_BLUETOOTH_DEVICE_ADDRESS = 1;
    public static final int COL_BLUETOOTH_DEVICE_NAME = 2;
    public static final int COL_BLUETOOTH_DEVICE_TYPE = 3;
    public static final int COL_MEDIA_PLAYER_NAME = 4;
    public static final int COL_MEDIA_PLAYER_PACKAGE_NAME = 5;

    public BluetoothDeviceMediaPlayerPairAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_bluetooth_media_pair, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int bluetoothDeviceType = cursor.getInt(COL_BLUETOOTH_DEVICE_TYPE);

        ImageView bluetoothDeviceIcon = (ImageView)view.findViewById(R.id.list_item_bluetooth_device_icon);
        switch (bluetoothDeviceType)
        {
            case BluetoothClass.Device.AUDIO_VIDEO_HANDSFREE:
                bluetoothDeviceIcon.setImageResource(R.mipmap.ic_car);
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET:
                bluetoothDeviceIcon.setImageResource(R.mipmap.ic_headset);
                break;
            case BluetoothClass.Device.WEARABLE_WRIST_WATCH:
                bluetoothDeviceIcon.setImageResource(R.mipmap.ic_watch);
                break;
        }

        String bluetoothDeviceName = cursor.getString(COL_BLUETOOTH_DEVICE_NAME);
        TextView bluetoothDeviceNameView = (TextView)view.findViewById(R.id.list_item_bluetooth_device_name);
        bluetoothDeviceNameView.setText(bluetoothDeviceName);

        // Use package name to retrieve media player icon
        String mediaPlayerPackage = cursor.getString(COL_MEDIA_PLAYER_PACKAGE_NAME);
        Drawable mediaPlayerDrawable = null;
        try {
            mediaPlayerDrawable = context.getPackageManager().getApplicationIcon(mediaPlayerPackage);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if(mediaPlayerDrawable != null) {
            ImageView mediaPlayerIcon = (ImageView) view.findViewById(R.id.list_item_media_player_icon);
            mediaPlayerIcon.setImageDrawable(mediaPlayerDrawable);
        }

        String mediaPlayerName = cursor.getString(COL_MEDIA_PLAYER_NAME);
        TextView mediaPlayerNameView = (TextView)view.findViewById(R.id.list_item_media_player_name);
        mediaPlayerNameView.setText(mediaPlayerName);
    }


}
