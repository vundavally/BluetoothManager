package com.example.krishna.bluetoothmanager;

import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

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
    public static final int COL_MEDIA_PLAYER_VOLUME = 6;

    public static class ViewHolder{
        public final ImageView mBluetoothDeviceIcon;
        public final TextView mBluetoothDeviceName;
        public final ImageView mMediaDeviceIcon;
        public final TextView mMediaDeviceName;
        public final ImageView mVolumeIcon;
        public final ProgressBar mVolumeProgressBar;

        public ViewHolder(View view)
        {
            mBluetoothDeviceIcon = (ImageView)view.findViewById(R.id.list_item_bluetooth_device_icon);
            mBluetoothDeviceName = (TextView)view.findViewById(R.id.list_item_bluetooth_device_name);
            mMediaDeviceIcon = (ImageView) view.findViewById(R.id.list_item_media_player_icon);
            mMediaDeviceName = (TextView)view.findViewById(R.id.list_item_media_player_name);
            mVolumeIcon = (ImageView)view.findViewById(R.id.volume_icon_id);
            mVolumeProgressBar = (ProgressBar)view.findViewById(R.id.volume_progress_bar);
        }
    }

    public BluetoothDeviceMediaPlayerPairAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_bluetooth_media_pair, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        int bluetoothDeviceType = cursor.getInt(COL_BLUETOOTH_DEVICE_TYPE);
        int bluetoothDeviceIconId;

        switch (bluetoothDeviceType)
        {
            case BluetoothClass.Device.AUDIO_VIDEO_HANDSFREE:
                bluetoothDeviceIconId = R.mipmap.ic_car;
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET:
                bluetoothDeviceIconId = R.mipmap.ic_headset;
                break;
            case BluetoothClass.Device.WEARABLE_WRIST_WATCH:
                bluetoothDeviceIconId = R.mipmap.ic_watch;
                break;
            default:
                bluetoothDeviceIconId = R.mipmap.ic_headset;
        }
        viewHolder.mBluetoothDeviceIcon.setImageResource(bluetoothDeviceIconId);

        String bluetoothDeviceName = cursor.getString(COL_BLUETOOTH_DEVICE_NAME);
        viewHolder.mBluetoothDeviceName.setText(bluetoothDeviceName);

        // Use package name to retrieve media player icon
        String mediaPlayerPackage = cursor.getString(COL_MEDIA_PLAYER_PACKAGE_NAME);
        Drawable mediaPlayerDrawable = null;
        try {
            mediaPlayerDrawable = context.getPackageManager().getApplicationIcon(mediaPlayerPackage);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if(mediaPlayerDrawable != null) {
            viewHolder.mMediaDeviceIcon.setImageDrawable(mediaPlayerDrawable);
        }
        else
        {
            // TODO change it to a fallback icon
            viewHolder.mMediaDeviceIcon.setImageResource(R.mipmap.ic_headset);
        }

        String mediaPlayerName = cursor.getString(COL_MEDIA_PLAYER_NAME);
        viewHolder.mMediaDeviceName.setText(mediaPlayerName);

        int progress = cursor.getInt(COL_MEDIA_PLAYER_VOLUME);
        viewHolder.mVolumeProgressBar.setProgress(progress);

        // Change volume icon if progress is zero.
        if(progress > 0){
            viewHolder.mVolumeIcon.setImageResource(R.mipmap.ic_volume_up);
        }
        else {
            viewHolder.mVolumeIcon.setImageResource(R.mipmap.ic_volume_off);
        }
    }


}
